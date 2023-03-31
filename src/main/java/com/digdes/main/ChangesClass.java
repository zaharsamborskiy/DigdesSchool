package com.digdes.main;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangesClass {

    private static final Commands commands = new Commands();

    public static String[] checkNon_ExistRequest(String req) {
        String[] checker = getNewRequest(req);
        for (String s : checker) {
            String key = s.substring(0, s.indexOf(findSymbol(s)));
            String value = s.substring(s.lastIndexOf(findSymbol(s)) + findSymbol(s).length());
            if (containsIgnore(req, commands.getSELECT())) key = s.substring(0, s.indexOf(findSymbol(s)));
            if (!(key.equalsIgnoreCase(commands.getID()) || key.equalsIgnoreCase(commands.getLASTNAME()) || key.equalsIgnoreCase(commands.getAGE()) || key.equalsIgnoreCase(commands.getCOST()) || key.equalsIgnoreCase(commands.getACTIVE())))
                throw new IllegalArgumentException("not found table - " + key + " - ");
            if (value.isEmpty()) throw new IllegalArgumentException("empty value " + key + " - " + value);
            char[] chars = value.toCharArray();
            if (key.equalsIgnoreCase(commands.getID()) || key.equalsIgnoreCase(commands.getAGE()) || key.equalsIgnoreCase(commands.getCOST())) {
                for (char c : chars) {
                    if (c == '.' || c == ',') continue;
                    if (c == '-') throw new IllegalArgumentException("the key - " + key  + " - can't be negative");
                    if (!Character.isDigit(c)) throw new IllegalArgumentException("the key - " + key  + " - can have the format of digits");
                }
            }
            if (key.equalsIgnoreCase(commands.getACTIVE()) && !(value.equals("true") || value.equalsIgnoreCase("false"))) throw new IllegalArgumentException("the key - " + key  + " - was written incorrectly");
            if (key.equalsIgnoreCase(commands.getLASTNAME())) {
                for (char c : chars) {
                    if (c == '%') continue;
                    if (!Character.isLetter(c)) throw new IllegalArgumentException("the key - " + key  + " - can have the format of letter");
                }
            }
        }
        return checker;
    }

    private static String[] getNewRequest(String request) {
        if (containsIgnore(request, commands.getUPDATE())) return newUpdate(request);
        String req = request.replaceAll("\\s", "");
        if (containsIgnore(request, commands.getDELETE())) return newDelete_Select(req);
        if (containsIgnore(request, commands.getSELECT())) return newDelete_Select(req);
        String req2 = req.substring(commands.getCommandValues());
        String req3 = req2.replaceAll(commands.getRegex(), "").trim();
        return req3.split(",");
    }

    private static String[] newDelete_Select(String request) {
        String req = "";
        String req2;
        String req3;
        String req4;
        String req5;

        if (containsIgnore(request, commands.getWHERE())) req = request.substring(commands.getCommandWhere());
        if (!containsIgnore(request, commands.getWHERE())) req = request.substring((commands.getCommandValues() / 2));
        req2 = req.replaceAll(commands.getSecondRegex(), "").trim();

        if (containsIgnore(request, commands.getILIKE())) req2 = req2.replaceAll(commands.getILIKE(), "=");
        if (containsIgnore(request, commands.getLIKE())) req2 = req2.replaceAll(commands.getLIKE(), "=");

        if (!(containsIgnore(req2, commands.getAND()) || containsIgnore(req2, commands.getOR()))) return req2.split(",");
        else if (containsIgnore(req2, commands.getAND())) {
            req3 = req2.substring(0, req2.indexOf(commands.getAND()));
            req4 = req2.substring(req2.indexOf(commands.getAND()) + commands.getAND().length());
            req5 = String.join(",", req3, req4);
            return req5.split(",");
        } else if (containsIgnore(req2, commands.getOR())) {
            req3 = req2.substring(0, req2.indexOf(commands.getOR()));
            req4 = req2.substring(req2.indexOf(commands.getOR()) + commands.getOR().length());
            req5 = String.join(",", req3, req4);
            return req5.split(",");
        } else throw new IllegalArgumentException("invalid select/delete request");
    }

    private static String[] newUpdate(String req) {
        String req2 = "";
        String req3 = "";
        if (!containsIgnore(req, commands.getWHERE())) {
            req2 = req.replaceAll(commands.getRegex(), "").trim();
            req3 = req2.substring(commands.getCommandValues());
        }
        if (containsIgnore(req, commands.getWHERE())) {
            req2 = req.replaceAll(commands.getRegex(), "").trim();
            req3 = req2.substring(commands.getCommandValues(), req.lastIndexOf(commands.getWHERE()) - commands.getWHERE().length());
        }
        return req3.split(",");
    }


    public static boolean checkPattern(String req, Map<String, Object> map) {
        String value = getLike(req);
        String key = commands.getLASTNAME();
        String temp = value.replaceAll("%", "");
        Pattern pattern = Pattern.compile(value, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(req);

        while (matcher.find()) value = matcher.group(0);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                String n3value = entry.getValue().toString();
                pattern = Pattern.compile(n3value, Pattern.MULTILINE);
                matcher = pattern.matcher(req);

                if (!containsIgnore(req, "%")) {
                    if (containsIgnore(req, commands.getILIKE())) return containsIgnore(n3value, temp);
                    if (n3value.length() > temp.length()) return n3value.contains(temp);
                    return matcher.find() && value.startsWith(n3value) && value.endsWith(n3value);
                } else if (value.startsWith("%") && !value.endsWith("%")) {
                    if (containsIgnore(req, commands.getILIKE())) return containsIgnore(n3value, temp);
                    if (n3value.length() >= temp.length()) return n3value.contains(temp);
                    return matcher.find() && value.endsWith(n3value);
                }
                if (!value.startsWith("%") && value.endsWith("%")) {
                    if (containsIgnore(req, commands.getILIKE())) return containsIgnore(n3value, temp);
                    if (n3value.length() >= temp.length()) return n3value.contains(temp);
                    return matcher.find() && value.startsWith(n3value);
                }
                if (value.startsWith("%") && value.endsWith("%")) {
                    if (containsIgnore(req, commands.getILIKE())) return containsIgnore(n3value, temp);
                    if (n3value.length() >= temp.length()) return n3value.contains(temp);
                    return matcher.find();
                }
            }
        }
        return false;
    }

    private static boolean positionLike_iLike(String req) {
        String s = req.replaceAll(commands.getSecondRegex(), "");
        String s2;
        boolean containsComparison = containsIgnore(req, commands.getAND()) || containsIgnore(req, commands.getOR());
        if (containsIgnore(req, commands.getSELECT()) || containsIgnore(req, commands.getDELETE())) {
            if (containsIgnore(req, commands.getWHERE()) && containsComparison) {
                s2 = s.substring(commands.getCommandWhere());
                return s2.startsWith(commands.getLASTNAME()); //true -> lastname 1 pos
            } else if (containsIgnore(req, commands.getWHERE()) && !containsComparison) {
                s2 = s.substring(commands.getCommandWhere());
                return s2.startsWith(commands.getLASTNAME()); //true -> lastname 1 pos
            } else {
                s2 = s.substring(commands.getCommandValues() / 2);
                return s2.startsWith(commands.getLASTNAME());
            }
        }
        return false;
    }

    private static String getLike(String req) {
        String s = req.replaceAll(commands.getSecondRegex(), "");
        boolean containsOr_And = containsIgnore(req, commands.getAND()) || containsIgnore(req, commands.getOR());
        boolean containsLike_iLike = containsIgnore(req, commands.getLIKE()) || containsIgnore(req, commands.getILIKE());
        boolean iLike = containsIgnore(req, commands.getILIKE());
        String s2;

        if (containsLike_iLike && containsOr_And && containsIgnore(req, commands.getWHERE())) {
            if (positionLike_iLike(req)) {
                s2 = s.substring(commands.getCommandWhere());
                if (containsIgnore(req, commands.getOR())) {
                    if (iLike) return replaceAll(s2.substring(s2.indexOf(commands.getILIKE()) + commands.getILIKE().length(), s2.lastIndexOf(commands.getOR())));
                    return replaceAll(s2.substring(s2.indexOf(commands.getLIKE()) + commands.getLIKE().length(), s2.lastIndexOf(commands.getOR())));
                }
                if (iLike) return replaceAll(s2.substring(s2.indexOf(commands.getILIKE()) + commands.getILIKE().length(), s2.lastIndexOf(commands.getAND())));
                return replaceAll(s2.substring(s2.indexOf(commands.getLIKE()) + commands.getLIKE().length(), s2.lastIndexOf(commands.getAND())));
            }
            if (!positionLike_iLike(req)) {
                s2 = s.substring(commands.getCommandWhere());
                if (iLike) return replaceAll(s2.substring(s2.indexOf(commands.getILIKE()) + commands.getILIKE().length()));
                return replaceAll(s2.substring(s2.indexOf(commands.getLIKE()) + commands.getLIKE().length()));
            }
        }

        if (containsLike_iLike && !containsOr_And && containsIgnore(req, commands.getWHERE())) {
            s2 = s.substring(commands.getCommandWhere());
            if (iLike) return replaceAll(s2.substring(s2.indexOf(commands.getILIKE()) + commands.getILIKE().length()));
            return replaceAll(s2.substring(s2.indexOf(commands.getLIKE()) + commands.getLIKE().length()));
        }
        if (containsLike_iLike && !containsOr_And && !containsIgnore(req, commands.getWHERE())) {
            s2 = s.substring(commands.getCommandValues() / 2);
            if (iLike) return replaceAll(s2.substring(s2.indexOf(commands.getILIKE()) + commands.getILIKE().length()));
            return replaceAll(s2.substring(s2.indexOf(commands.getLIKE()) + commands.getLIKE().length()));
        }
        return "";
    }


    public static String findSymbol(String request) {
        for (int i = 0; i < request.length() - 1; i++) {
            if (request.charAt(i) == '>' && request.charAt(i + 1) == '=') return ">=";
            if (request.charAt(i) == '<' && request.charAt(i + 1) == '=') return "<=";
            if (request.charAt(i) == '!' && request.charAt(i + 1) == '=') return "!=";
            if (request.charAt(i) == '<') return "<";
            if (request.charAt(i) == '>') return ">";
            if (request.charAt(i) == '=') return "=";
        }
        throw new IllegalArgumentException("not found symbol");
    }

    public static int comparisonSymbols(String s, double n3num, double num) {
        if (findSymbol(s).equals("=") && n3num == num) return 1;
        if (findSymbol(s).equals("!=") && n3num != num) return 1;
        if (findSymbol(s).equals(">=") && n3num >= num) return 1;
        if (findSymbol(s).equals("<=") && n3num <= num) return 1;
        if (findSymbol(s).equals("<") && n3num < num) return 1;
        if (findSymbol(s).equals(">") && n3num > num) return 1;
        return 0;
    }
    private static String replaceLikeILike(String request, String result) {

        if (containsIgnore(request, commands.getILIKE())) return result.replace(commands.getILIKE(), "=");
        if (containsIgnore(request, commands.getLIKE())) return result.replace(commands.getLIKE(), "=");
        return "";
    }
    private static String replaceAll(String req) {
        return req.replaceAll("['’`‘]", "").trim();
    }
    private static boolean containsIgnore(String s1, String s2) {
        return s1.toLowerCase().contains(s2.toLowerCase());
    }
}
