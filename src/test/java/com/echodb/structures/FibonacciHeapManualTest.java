package com.echodb.structures;

public class FibonacciHeapManualTest {
    public static void main(String[] args) {
        System.out.println("Starting FibonacciHeap Manual Test...");
        
        FibonacciHeap<String> heap = new FibonacciHeap<>();
        
        // Test 1: Insert and Extract Min
        heap.insert("Task1", 10.0);
        heap.insert("Task2", 5.0);
        heap.insert("Task3", 15.0);
        heap.insert("Task4", 2.0);
        
        FibonacciHeap.Node<String> minNode = heap.extractMin();
        assert "Task4".equals(minNode.data) : "Test 1 failed: Expected Task4";
        assert heap.getSize() == 3 : "Test 1 failed: Expected size 3";
        
        minNode = heap.extractMin();
        assert "Task2".equals(minNode.data) : "Test 1 failed: Expected Task2";
        
        System.out.println("Test 1 Passed: Basic Insert and Extract Min");

        // Test 2: Large insertions
        for (int i = 0; i < 50; i++) {
            heap.insert("BatchTask" + i, (double) i);
        }
        assert heap.extractMin().key == 0.0 : "Test 2 failed: Expected key 0.0";
        System.out.println("Test 2 Passed: Large insertions");

        System.out.println("All FibonacciHeap Tests Passed!");
    }
}
