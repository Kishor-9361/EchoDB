package com.echodb.structures;

public class SplayTreeManualTest {
    public static void main(String[] args) {
        System.out.println("Starting SplayTree Manual Test...");
        
        SplayTree<String, String> tree = new SplayTree<>();
        
        // Test 1: Put and Get
        tree.put("a", "alpha");
        tree.put("b", "beta");
        tree.put("c", "gamma");
        
        assert "alpha".equals(tree.get("a")) : "Test 1 failed: Expected alpha";
        assert "beta".equals(tree.get("b")) : "Test 1 failed: Expected beta";
        assert "gamma".equals(tree.get("c")) : "Test 1 failed: Expected gamma";
        System.out.println("Test 1 Passed: Put and Get");

        // Test 2: Update
        tree.put("a", "new_alpha");
        assert "new_alpha".equals(tree.get("a")) : "Test 2 failed: Expected new_alpha";
        assertEquals(3, tree.getSize(), "Test 2 failed: Expected size 3");
        System.out.println("Test 2 Passed: Update");

        // Test 3: Splay effect (Internal behavior)
        // Note: We can't easily check root without exposing it, 
        // but we can check if it still works after many splays.
        for (int i = 0; i < 100; i++) {
            tree.put("key" + i, "val" + i);
        }
        assert "val50".equals(tree.get("key50")) : "Test 3 failed: Key 50 missing";
        System.out.println("Test 3 Passed: Large insertions and retrieval");

        System.out.println("All Tests Passed!");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " (Expected: " + expected + ", Actual: " + actual + ")");
        }
    }
}
