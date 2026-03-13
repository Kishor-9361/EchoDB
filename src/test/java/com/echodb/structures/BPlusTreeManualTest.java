package com.echodb.structures;

public class BPlusTreeManualTest {
    public static void main(String[] args) {
        System.out.println("Starting BPlusTree Manual Test...");
        
        BPlusTree<Integer, String> tree = new BPlusTree<>(4);
        
        // Test 1: Insert and Retrieval
        for (int i = 0; i < 20; i++) {
            tree.put(i, "val" + i);
        }
        
        for (int i = 0; i < 20; i++) {
            assert ("val" + i).equals(tree.get(i)) : "Test 1 failed at key " + i;
        }
        System.out.println("Test 1 Passed: Multi-level Insert and Retrieval");

        // Test 2: Non-existent key
        assert tree.get(100) == null : "Test 2 failed: Expected null for non-existent key";
        System.out.println("Test 2 Passed: Non-existent key");

        System.out.println("All BPlusTree Tests Passed!");
    }
}
