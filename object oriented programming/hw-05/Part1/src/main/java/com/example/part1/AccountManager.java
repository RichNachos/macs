package com.example.part1;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, String> dataBase;

    // Constructor
    public AccountManager() {
        dataBase = new HashMap<String, String>();
    }

    // Returns true if key name isn't in Map and returns true if successfully added, otherwise false
    public boolean addAccount(String name, String pass) {
        if (name == null || pass == null || name.length() == 0 || pass.length() == 0)
            return false;
        if (dataBase.containsKey(name))
            return false;
        dataBase.put(name,pass);
        return true;
    }

    // Returns true of name and pass match existing key-value pair in Map, otherwise false
    public boolean login(String name, String pass) {
        if (name == null || pass == null || name.length() == 0 || pass.length() == 0)
            return false;
        if (!dataBase.containsKey(name))
            return false;
        if (dataBase.get(name).equals(pass))
            return true;
        return false;
    }

    // Returns if key with name exists in Map
    public boolean hasAccount(String name) {
        return dataBase.containsKey(name);
    }
}
