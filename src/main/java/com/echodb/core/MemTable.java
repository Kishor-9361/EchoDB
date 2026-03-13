package com.echodb.core;

import com.echodb.structures.SplayTree;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * MemTable is the in-memory buffer that stores recent writes.
 * It uses a SplayTree for self-adjusting indexing and ensures thread-safety.
 */
public class MemTable {
    private final SplayTree<String, byte[]> tree;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final long maxByteSize;
    private long currentByteSize;

    public MemTable(long maxByteSize) {
        this.tree = new SplayTree<>();
        this.maxByteSize = maxByteSize;
        this.currentByteSize = 0;
    }

    /**
     * Adds a key-value pair to the MemTable.
     * @return true if the MemTable should be flushed after this write.
     */
    public boolean put(String key, byte[] value) {
        lock.writeLock().lock();
        try {
            // Estimate size change (very basic: key bytes + value bytes)
            // In a real DB, this would be more precise.
            currentByteSize += (key.length() * 2L) + value.length;
            tree.put(key, value);
            return currentByteSize >= maxByteSize;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Retrieves a value from the MemTable.
     */
    public byte[] get(String key) {
        lock.readLock().lock();
        try {
            return tree.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public long getByteSize() {
        return currentByteSize;
    }

    public boolean isFull() {
        return currentByteSize >= maxByteSize;
    }

    public int getCount() {
        return tree.getSize();
    }
}
