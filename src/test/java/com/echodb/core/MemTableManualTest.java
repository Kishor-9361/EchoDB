package com.echodb.core;

public class MemTableManualTest {
    public static void main(String[] args) {
        System.out.println("Starting MemTable Manual Test...");
        
        // 1KB limit
        MemTable memTable = new MemTable(1024);
        
        // Test 1: Put and Size Tracking
        String key = "test_key";
        byte[] val = "test_value".getBytes();
        boolean needsFlush = memTable.put(key, val);
        
        assert "test_value".equals(new String(memTable.get(key))) : "Test 1 failed: Retrieval";
        assert !needsFlush : "Test 1 failed: Should not need flush yet";
        assert memTable.getByteSize() > 0 : "Test 1 failed: Size tracking";
        System.out.println("Test 1 Passed: Put and Size Tracking");

        // Test 2: Flush Trigger
        for (int i = 0; i < 50; i++) {
            needsFlush = memTable.put("key" + i, new byte[20]);
        }
        assert needsFlush || memTable.isFull() : "Test 2 failed: Should trigger flush";
        System.out.println("Test 2 Passed: Flush Trigger");

        System.out.println("All MemTable Tests Passed!");
    }
}
