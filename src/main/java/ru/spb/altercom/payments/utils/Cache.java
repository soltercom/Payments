package ru.spb.altercom.payments.utils;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    public final static String PERSON = "PERSON";
    public final static String PERSON_ID = "PERSON_ID";
    public final static String DATE = "DATE";

    private final Map<String, Object> data = new HashMap<>();

    private final static Cache instance = new Cache();

    private Cache() {}

    public static Cache getInstance() {
        return instance;
    }

    public void add(String key, Object value) {
        if (value != null) {
            data.put(key, value);
        }
    }

    public Object get(String key) {
        var value = data.get(key);
        data.remove(key);
        return value;
    }

}
