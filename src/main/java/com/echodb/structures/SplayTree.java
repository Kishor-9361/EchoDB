package com.echodb.structures;

/**
 * A self-adjusting binary search tree (Splay Tree) for the EchoDB MemTable.
 * After every access, the accessed node is moved to the root to optimize for skewed workloads.
 * 
 * @param <K> The type of keys maintained by this tree (must be Comparable)
 * @param <V> The type of mapped values
 */
public class SplayTree<K extends Comparable<K>, V> {

    private Node<K, V> root;
    private int size;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Inserts or updates a key-value pair.
     */
    public void put(K key, V value) {
        if (root == null) {
            root = new Node<>(key, value);
            size++;
            return;
        }

        root = splay(root, key);

        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            root.value = value; // Update existing
        } else if (cmp < 0) {
            Node<K, V> newNode = new Node<>(key, value);
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            root = newNode;
            size++;
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            root = newNode;
            size++;
        }
    }

    /**
     * Retrieves a value by key. Moves the accessed node to the root.
     */
    public V get(K key) {
        if (root == null) return null;
        root = splay(root, key);
        return (key.compareTo(root.key) == 0) ? root.value : null;
    }

    /**
     * Internal splay operation: moves the key to the root.
     */
    private Node<K, V> splay(Node<K, V> h, K key) {
        if (h == null) return null;

        int cmp1 = key.compareTo(h.key);

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (h.left == null) return h;

            int cmp2 = key.compareTo(h.left.key);
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else return rotateRight(h);
        } else if (cmp1 > 0) {
            if (h.right == null) return h;

            int cmp2 = key.compareTo(h.right.key);
            if (cmp2 < 0) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) h.right = rotateRight(h.right);
            } else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else return rotateLeft(h);
        } else {
            return h;
        }
    }

    private Node<K, V> rotateRight(Node<K, V> h) {
        Node<K, V> x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node<K, V> rotateLeft(Node<K, V> h) {
        Node<K, V> x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Traverses the tree in-order and applies the consumer to each node.
     */
    public void forEach(java.util.function.BiConsumer<K, V> action) {
        forEach(root, action);
    }

    private void forEach(Node<K, V> h, java.util.function.BiConsumer<K, V> action) {
        if (h == null) return;
        forEach(h.left, action);
        action.accept(h.key, h.value);
        forEach(h.right, action);
    }
}
