package com.digdes.school;

import com.digdes.main.Commands;
import com.digdes.main.ChangesClass;

import java.util.*;


public class JavaSchoolStarter {
    public JavaSchoolStarter() {
    }

    private final Commands commands = new Commands();
    private final List<Map<String, Object>> arrayList = new ArrayList<>();

    public List<Map<String, Object>> execute(String request) throws Exception {
        try {
            if (containsIgnore(request, commands.getINSERT())) arrayList.add(insertMethod(request));
            if (containsIgnore(request, commands.getSELECT())) return selectMethod(request);
            if (containsIgnore(request, commands.getUPDATE())) updateMethod(request);
            if (containsIgnore(request, commands.getDELETE())) deleteMethod(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return arrayList;
    }

    private void deleteMethod(String req) {
        String[] request = ChangesClass.checkNon_ExistRequest(req);
        int separator = checkSeparators(req);

        for (Map<String, Object> offmap : arrayList) {
            int count = 0;
            for (String s : request) {
                String key = s.substring(0, s.indexOf(ChangesClass.findSymbol(s)));
                String value = s.substring(s.lastIndexOf(ChangesClass.findSymbol(s)) + ChangesClass.findSymbol(s).length());
                for (Map.Entry<String, Object> n3 : offmap.entrySet()) {
                    if (key.equalsIgnoreCase(n3.getKey()) && comparisonOperators(s, n3.getValue().toString(), value, offmap, req)) {
                        count = counter(req,offmap, count, s, key, n3);
                        if (separator == 1 && count == request.length / 2) {
                            arrayList.remove(offmap);
                            return;
                        }
                        if ((separator == 2 || separator == 3 || separator == 4) && count == request.length) {
                            count = 0;
                            n3.setValue("-1");
                        }
                    }
                }
            }
        }
        Iterator<Map<String, Object>> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            for (Map.Entry<String, Object> iter : iterator.next().entrySet()) {
                if (iter.getValue().toString().equalsIgnoreCase("-1")) iterator.remove();
            }
        }
    }
    private ArrayList<Map<String, Object>> selectMethod(String req) {
        String[] request = ChangesClass.checkNon_ExistRequest(req);
        ArrayList<Map<String, Object>> newArrayList = new ArrayList<>();
        int separator = checkSeparators(req);

        if (separator == 1 || separator == 2 || separator == 3 || separator == 4) {
            for (Map<String, Object> offmap : arrayList) {
                int count2 = 0;
                for (String s : request) {
                    String key = s.substring(0, s.indexOf(ChangesClass.findSymbol(s)));
                    for (Map.Entry<String, Object> entry : offmap.entrySet()) {
                        count2 = counter(req, offmap, count2, s, key, entry);
                        if (separator == 1 && count2 == request.length / 2) {
                            Map<String, Object> map2 = new HashMap<>(offmap);
                            newArrayList.add(map2);
                            return newArrayList;
                        } else {
                            if (count2 == request.length) {
                                count2 = 0;
                                Map<String, Object> map2 = new HashMap<>(offmap);
                                newArrayList.add(map2);
                            }
                        }
                    }
                }
            }
        }
        return newArrayList;
    }


    private int counter(String req, Map<String, Object> offmap, int count2, String s, String key, Map.Entry<String, Object> entry) {
        int count;
        String n3key = entry.getKey();
        boolean numeric = n3key.equalsIgnoreCase(commands.getAGE()) || n3key.equalsIgnoreCase(commands.getCOST()) || n3key.equalsIgnoreCase(commands.getID());
        if (offmap.containsKey(key)) {
            if (Objects.equals(n3key, key)) {
                if (numeric) {
                    double num = getaDouble(s.substring(s.indexOf(ChangesClass.findSymbol(s))));
                    double n3num = Double.parseDouble(entry.getValue().toString());
                    count = ChangesClass.comparisonSymbols(s, n3num, num);
                    count2 = count == 1 ? count2 + 1 : 0;
                }
                if (n3key.equalsIgnoreCase(commands.getACTIVE())) {
                    count2 = entry.getValue().toString().equalsIgnoreCase(s.substring(s.indexOf(ChangesClass.findSymbol(s)) + 1)) ? count2 + 1 : 0;
                }
                if (n3key.equalsIgnoreCase(commands.getLASTNAME())) {
                    count2 = ChangesClass.checkPattern(req, offmap) ? count2 + 1 : 0;
                }
            }
        }
        return count2;
    }

    private void updateMethod(String req) {
        String[] request = ChangesClass.checkNon_ExistRequest(req);
        String where = getWhere(req);
        if (containsIgnore(req, commands.getWHERE())) {
            String key = where.substring(0, where.indexOf(ChangesClass.findSymbol(where)));
            String value = where.substring(where.lastIndexOf(ChangesClass.findSymbol(where)) + ChangesClass.findSymbol(where).length());
            for (Map<String, Object> map : arrayList) {
                if (map.containsKey(key)) loop_Insert_Update(request, map, key, value, req);
            }
        } else {
            for (Map<String, Object> map : arrayList) {
                loop_Insert_Update(request, map, "", "", "");
            }
        }
    }

    private Map<String, Object> insertMethod(String request) {
        Map<String, Object> map = new HashMap<>();
        String[] req = ChangesClass.checkNon_ExistRequest(request);
        loop_Insert_Update(req, map, "", "", "");
        return map;
    }


    private void loop_Insert_Update(String[] request, Map<String, Object> map, String key, String val, String req) {
        for (String s : request) {
            String value = s.substring(s.lastIndexOf(ChangesClass.findSymbol(s)) + ChangesClass.findSymbol(s).length());
            if (!key.isEmpty()) {
                for (Map.Entry<String, Object> n3 : map.entrySet()) {
                    if (containsIgnore(req, value)) {
                        if (key.equalsIgnoreCase(n3.getKey()) && ChangesClass.checkPattern(req, map) && containsIgnore(req, commands.getLASTNAME())) {
                            insert_Update_changes(map, s, value);
                        } else if (key.equalsIgnoreCase(n3.getKey()) && n3.getValue().toString().equalsIgnoreCase(val)){
                            insert_Update_changes(map, s, value);
                        }
                    }
                }
            }
            if (key.isEmpty()) insert_Update_changes(map, s, value);
        }
    }

    private void insert_Update_changes(Map<String, Object> map, String s, String value) {
        if (containsIgnore(s, commands.getID())) map.put(commands.getID(), getaLong(value));
        if (containsIgnore(s, commands.getAGE())) map.put(commands.getAGE(), getaLong(value));
        if (containsIgnore(s, commands.getCOST())) map.put(commands.getCOST(), getaDouble(value));
        if (containsIgnore(s, commands.getACTIVE())) map.put(commands.getACTIVE(), value.equals("true"));
        if (containsIgnore(s, commands.getLASTNAME())) map.put(commands.getLASTNAME(), value);
    }

    private int checkSeparators(String req) {
        if (containsIgnore(req, commands.getWHERE()) && containsIgnore(req, commands.getOR())) return 1; //where + or
        if (containsIgnore(req, commands.getWHERE()) && containsIgnore(req, commands.getAND())) return 2; //where + and
        if (containsIgnore(req, commands.getWHERE()) && (!containsIgnore(req, commands.getOR()) && !containsIgnore(req, commands.getAND())))
            return 3; //where
        if (!containsIgnore(req, commands.getWHERE()) && (!containsIgnore(req, commands.getOR()) && !containsIgnore(req, commands.getAND())))
            return 4; //
        return 0;
    }

    private static Double getaDouble(String ss) {
        String findSub = ss.replaceAll("[^0-9.,]", "").trim();
        return Double.parseDouble(findSub);
    }

    private static Long getaLong(String ss) {
        String findSub = ss.replaceAll("[^0-9.,]", "").trim();
        return Long.parseLong(findSub);
    }

    private String getWhere(String req) {
        String where = "";
        if (containsIgnore(req, commands.getLIKE())) {
            where = req.substring(req.indexOf(commands.getWHERE()) + commands.getWHERE().length()).trim();
            String where2 = where.replaceAll(commands.getLIKE(), "=");
            return where2.replaceAll(commands.getSecondRegex(), "");
        }
        if (containsIgnore(req, commands.getILIKE())) {
            where = req.substring(req.indexOf(commands.getWHERE()) + commands.getWHERE().length()).trim();
            String where2 = where.replaceAll(commands.getILIKE(), "=");
            return where2.replaceAll(commands.getSecondRegex(), "");
        }
        where = req.substring(req.indexOf(commands.getWHERE()) + commands.getWHERE().length()).trim();
        return where.replaceAll(commands.getSecondRegex(), "");
    }
    private boolean comparisonOperators(String s, String value1, String value2, Map<String, Object> offmap, String req) {
        if (containsIgnore(s, commands.getLASTNAME())) {
            return ChangesClass.checkPattern(req, offmap);
        }
        if (containsIgnore(s, commands.getACTIVE())) {
            return value1.equalsIgnoreCase(value2);
        }
        int i = ChangesClass.comparisonSymbols(s, Double.parseDouble(value1), Double.parseDouble(value2));
        return i == 1;
    }

    private static boolean containsIgnore(String s1, String s2) {
        return s1.toLowerCase().contains(s2.toLowerCase());
    }
}
