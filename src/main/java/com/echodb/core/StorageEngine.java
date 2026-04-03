package com.echodb.core;

import com.echodb.util.WAL;
import java.io.IOException;
import java.util.TreeMap;

/**
 * StorageEngine is the primary entry point for EchoDB.
 * It manages the lifecycle of MemTable, WAL, and SSTables.
 */
public class StorageEngine implements AutoCloseable {
    private final MemTable memTable;
    private final WAL wal;
    private final SSTableManager sstableManager;
    private final String dataDir;

    public StorageEngine(String dataDir, long maxMemSize) throws IOException {
        this.dataDir = dataDir;
        this.memTable = new MemTable(maxMemSize);
        this.wal = new WAL(dataDir, "wal.log");
        this.sstableManager = new SSTableManager(dataDir);
        
        recover();
    }

    /**
     * Recovers the state from the WAL.
     */
    private void recover() throws IOException {
        wal.replay(
            (key, value) -> memTable.put(key, value),
            (key) -> { /* Delete not fully implemented in MemTable yet */ }
        );
    }

    /**
     * Persists a key-value pair.
     */
    public synchronized void put(String key, byte[] value) throws IOException {
        wal.logPut(key, value);
        boolean shouldFlush = memTable.put(key, value);
        
        if (shouldFlush) {
            flush();
        }
    }

    /**
     * Retrieves a value by key.
     */
    public byte[] get(String key) throws IOException {
        // 1. Check MemTable
        byte[] val = memTable.get(key);
        if (val != null) return val;
        
        // 2. Check SSTables
        return sstableManager.get(key);
    }

    /**
     * Flushes the MemTable to an SSTable.
     */
    private void flush() throws IOException {
        TreeMap<String, byte[]> data = memTable.getSortedData();
        sstableManager.flush(data);
        memTable.clear();
        wal.clear();
    }

    @Override
    public void close() throws IOException {
        wal.close();
    }
}
