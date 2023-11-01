package com.example.part1;

import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    public void test1() {
        AccountManager manager = new AccountManager();
        assertFalse(manager.login("a","b"));
        assertTrue(manager.addAccount("a","b"));
        assertTrue(manager.login("a","b"));
    }
    public void test2() {
        AccountManager manager = new AccountManager();
        assertTrue(manager.addAccount("a","b"));
        assertFalse(manager.login("a","a"));
        assertFalse(manager.login("a",""));
        assertTrue(manager.hasAccount("a"));
    }
    public void test3() {
        AccountManager manager = new AccountManager();
        assertFalse(manager.addAccount("", ""));
        assertFalse(manager.addAccount("a", ""));
        assertFalse(manager.addAccount("", "b"));
        assertFalse(manager.login("", ""));
        assertFalse(manager.login("a", "b"));
        assertTrue(manager.addAccount("a","b"));
        assertFalse(manager.addAccount("a","b"));
        assertFalse(manager.addAccount(null, null));
    }
}
