package com.digdes.main;

public class Commands {
    private String INSERT = "insert";
    private String SELECT = "select";
    private String UPDATE = "update";
    private String DELETE = "delete";
    private String LASTNAME = "lastname";
    private String WHERE = "where";
    private String ID = "id";
    private String AGE = "age";
    private String COST = "cost";
    private String ACTIVE = "active";
    private String AND = "and";
    private String OR = "or";
    private String LIKE = "like";
    private String ILIKE = "ilike";
    private int commandValues = 13;

    private int commandWhere = 12;

    private String regex = "[^A-Za-zА-Яа-я0-9,.=%-]";

    private String secondRegex = "[^A-Za-zА-Яа-я0-9,.=%<>!-]";

    public Commands() {
    }
    public int getCommandWhere() {
        return commandWhere;
    }

    public void setCommandWhere(int commandWhere) {
        this.commandWhere = commandWhere;
    }

    public String getSecondRegex() {
        return secondRegex;
    }

    public void setSecondRegex(String secondRegex) {
        this.secondRegex = secondRegex;
    }

    public int getCommandValues() {
        return commandValues;
    }

    public void setCommandValues(int commandValues) {
        this.commandValues = commandValues;
    }

    public String getRegex() {
        return regex;
    }
    public String getINSERT() {
        return INSERT;
    }

    public String getSELECT() {
        return SELECT;
    }

    public String getUPDATE() {
        return UPDATE;
    }
    public String getDELETE() {
        return DELETE;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public String getWHERE() {
        return WHERE;
    }

    public String getID() {
        return ID;
    }

    public String getAGE() {
        return AGE;
    }

    public String getCOST() {
        return COST;
    }

    public String getACTIVE() {
        return ACTIVE;
    }

    public String getAND() {
        return AND;
    }

    public String getOR() {
        return OR;
    }

    public String getLIKE() {
        return LIKE;
    }

    public String getILIKE() {
        return ILIKE;
    }
}
