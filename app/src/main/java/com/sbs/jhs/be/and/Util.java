package com.sbs.jhs.be.and;

public class Util {

    public static int getAsInt(Object id) {
        if (id instanceof Double) {
            return Integer.parseInt(String.valueOf(Math.round((Double) id)));
        } else if (id instanceof Integer) {
            return (Integer) id;
        } else if (id instanceof Long) {
            return (Integer) id;

        }
        return -1;
    }
}
