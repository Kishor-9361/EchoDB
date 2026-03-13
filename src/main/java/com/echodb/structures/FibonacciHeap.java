package com.echodb.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fibonacci Heap implementation for EchoDB compaction task prioritization.
 * Provides O(1) amortized insertion and O(log n) amortized extraction.
 */
public class FibonacciHeap<T> {

    private Node<T> min;
    private int size;

    public static class Node<T> {
        T data;
        double key;
        Node<T> parent, left, right, child;
        int degree;
        boolean mark;

        Node(T data, double key) {
            this.data = data;
            this.key = key;
            this.left = this;
            this.right = this;
        }
    }

    public Node<T> insert(T data, double key) {
        Node<T> node = new Node<>(data, key);
        if (min != null) {
            node.left = min;
            node.right = min.right;
            min.right = node;
            node.right.left = node;
            if (key < min.key) min = node;
        } else {
            min = node;
        }
        size++;
        return node;
    }

    public Node<T> extractMin() {
        Node<T> z = min;
        if (z != null) {
            if (z.child != null) {
                Node<T> firstChild = z.child;
                Node<T> curr = firstChild;
                do {
                    Node<T> next = curr.right;
                    curr.left = min;
                    curr.right = min.right;
                    min.right = curr;
                    curr.right.left = curr;
                    curr.parent = null;
                    curr = next;
                } while (curr != firstChild);
            }
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            size--;
        }
        return z;
    }

    private void consolidate() {
        int maxDegree = (int) (Math.log(size) / Math.log(1.618)) + 1;
        List<Node<T>> a = new ArrayList<>(maxDegree);
        for (int i = 0; i < maxDegree; i++) a.add(null);

        Node<T> curr = min;
        int numRoots = 0;
        if (curr != null) {
            numRoots++;
            curr = curr.right;
            while (curr != min) {
                numRoots++;
                curr = curr.right;
            }
        }

        while (numRoots > 0) {
            int d = curr.degree;
            Node<T> next = curr.right;
            while (d < a.size() && a.get(d) != null) {
                Node<T> y = a.get(d);
                if (curr.key > y.key) {
                    Node<T> temp = curr;
                    curr = y;
                    y = temp;
                }
                link(y, curr);
                a.set(d, null);
                d++;
            }
            if (d >= a.size()) {
                for (int i = a.size(); i <= d; i++) a.add(null);
            }
            a.set(d, curr);
            curr = next;
            numRoots--;
        }

        min = null;
        for (Node<T> node : a) {
            if (node != null) {
                if (min == null) {
                    min = node;
                    node.left = node;
                    node.right = node;
                } else {
                    node.left = min;
                    node.right = min.right;
                    min.right = node;
                    node.right.left = node;
                    if (node.key < min.key) min = node;
                }
            }
        }
    }

    private void link(Node<T> y, Node<T> x) {
        y.left.right = y.right;
        y.right.left = y.left;
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }
        x.degree++;
        y.mark = false;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return min == null;
    }
}
