package com.echodb.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic B+Tree implementation for the EchoDB SSTable index.
 * B+Trees are optimized for disk-based storage and range queries.
 */
public class BPlusTree<K extends Comparable<K>, V> {
    private final int m; // Order of the tree
    private Node root;

    public BPlusTree(int m) {
        this.m = m;
        this.root = new LeafNode();
    }

    private abstract class Node {
        List<K> keys = new ArrayList<>();
        abstract boolean isFull();
    }

    private class InternalNode extends Node {
        List<Node> children = new ArrayList<>();

        @Override
        boolean isFull() {
            return keys.size() >= m;
        }
    }

    private class LeafNode extends Node {
        List<V> values = new ArrayList<>();
        LeafNode next;

        @Override
        boolean isFull() {
            return keys.size() >= m;
        }
    }

    public void put(K key, V value) {
        Node newChild = insert(root, key, value);
        if (newChild != null) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(getSmallestKey(newChild));
            newRoot.children.add(root);
            newRoot.children.add(newChild);
            root = newRoot;
        }
    }

    private Node insert(Node n, K key, V value) {
        if (n instanceof LeafNode) {
            LeafNode leaf = (LeafNode) n;
            int idx = Collections.binarySearch(leaf.keys, key);
            if (idx >= 0) {
                leaf.values.set(idx, value);
                return null;
            } else {
                int insertPos = -(idx + 1);
                leaf.keys.add(insertPos, key);
                leaf.values.add(insertPos, value);
                if (leaf.isFull()) return splitLeaf(leaf);
                return null;
            }
        } else {
            InternalNode internal = (InternalNode) n;
            int idx = Collections.binarySearch(internal.keys, key);
            int childIdx = (idx >= 0) ? idx + 1 : -(idx + 1);
            Node newChild = insert(internal.children.get(childIdx), key, value);
            if (newChild != null) {
                K newKey = getSmallestKey(newChild);
                int keyIdx = Collections.binarySearch(internal.keys, newKey);
                int insertPos = -(keyIdx + 1);
                internal.keys.add(insertPos, newKey);
                internal.children.add(insertPos + 1, newChild);
                if (internal.isFull()) return splitInternal(internal);
            }
            return null;
        }
    }

    private Node splitLeaf(LeafNode leaf) {
        int mid = m / 2;
        LeafNode newNode = new LeafNode();
        newNode.keys.addAll(leaf.keys.subList(mid, m));
        newNode.values.addAll(leaf.values.subList(mid, m));
        leaf.keys.subList(mid, m).clear();
        leaf.values.subList(mid, m).clear();
        newNode.next = leaf.next;
        leaf.next = newNode;
        return newNode;
    }

    private Node splitInternal(InternalNode internal) {
        int mid = m / 2;
        InternalNode newNode = new InternalNode();
        K upKey = internal.keys.get(mid);
        newNode.keys.addAll(internal.keys.subList(mid + 1, m));
        newNode.children.addAll(internal.children.subList(mid + 1, m + 1));
        internal.keys.subList(mid, m).clear();
        internal.children.subList(mid + 1, m + 1).clear();
        return newNode;
    }

    private K getSmallestKey(Node n) {
        while (!(n instanceof LeafNode)) {
            n = ((InternalNode) n).children.get(0);
        }
        return n.keys.get(0);
    }

    public V get(K key) {
        LeafNode leaf = findLeaf(root, key);
        int idx = Collections.binarySearch(leaf.keys, key);
        return (idx >= 0) ? leaf.values.get(idx) : null;
    }

    private LeafNode findLeaf(Node n, K key) {
        if (n instanceof LeafNode) return (LeafNode) n;
        InternalNode internal = (InternalNode) n;
        int idx = Collections.binarySearch(internal.keys, key);
        int childIdx = (idx >= 0) ? idx + 1 : -(idx + 1);
        return findLeaf(internal.children.get(childIdx), key);
    }
}
