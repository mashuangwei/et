package com.msw.et.enums;

public enum DataSourceEnum {

    ET("et"),EASY("easy");

    private String value;

    DataSourceEnum(String value){this.value=value;}

    public String getValue() {
        return value;
    }
}
