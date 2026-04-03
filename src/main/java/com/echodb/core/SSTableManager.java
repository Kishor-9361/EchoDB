package com.echodb.core;

import com.echodb.structures.BPlusTree;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SSTableManager handles the creation and lookup of Sorted String Tables (SSTables).
 */
public class SSTableManager {
    private final String dataDirectory;
    private int nextFileId = 0;

    public SSTableManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Scan for existing files to set nextFileId
        File[] files = dir.listFiles((d, name) -> name.startsWith("sstable_") && name.endsWith(".db"));
        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                int id = Integer.parseInt(name.substring(8, name.length() - 3));
                if (id >= nextFileId) {
                    nextFileId = id + 1;
                }
            }
        }
    }

    /**
     * Flushes a MemTable to a new SSTable file.
     * @param data The data from the MemTable (should be sorted by key).
     */
    public void flush(TreeMap<String, byte[]> data) throws IOException {
        String filename = "sstable_" + nextFileId++ + ".db";
        File file = new File(dataDirectory, filename);
        
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            // Index to store key -> offset
            List<IndexEntry> indexEntries = new ArrayList<>();
            long currentOffset = 0;

            // Write entries
            for (Map.Entry<String, byte[]> entry : data.entrySet()) {
                String key = entry.getKey();
                byte[] value = entry.getValue();
                
                indexEntries.add(new IndexEntry(key, currentOffset));
                
                // Write key length, key, value length, value
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                out.writeInt(keyBytes.length);
                out.write(keyBytes);
                out.writeInt(value.length);
                out.write(value);
                
                currentOffset += 4 + keyBytes.length + 4 + value.length;
            }

            // Write index block
            long indexOffset = currentOffset;
            out.writeInt(indexEntries.size());
            for (IndexEntry entry : indexEntries) {
                byte[] kBytes = entry.key.getBytes(StandardCharsets.UTF_8);
                out.writeInt(kBytes.length);
                out.write(kBytes);
                out.writeLong(entry.offset);
            }

            // Write index offset at the very end (8 bytes)
            out.writeLong(indexOffset);
        }
    }

    private static class IndexEntry {
        String key;
        long offset;
        IndexEntry(String key, long offset) { this.key = key; this.offset = offset; }
    }

    /**
     * Searches for a key across all SSTables (search them in reverse order of creation).
     */
    public byte[] get(String key) throws IOException {
        for (int id = nextFileId - 1; id >= 0; id--) {
            File f = new File(dataDirectory, "sstable_" + id + ".db");
            if (!f.exists()) continue;

            byte[] val = searchInSSTable(f, key);
            if (val != null) return val;
        }
        return null;
    }

    private byte[] searchInSSTable(File file, String key) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long fileLength = raf.length();
            if (fileLength < 8) return null;

            // Read index offset from the end
            raf.seek(fileLength - 8);
            long indexOffset = raf.readLong();
            
            // Go to index and read entries
            raf.seek(indexOffset);
            int entryCount = raf.readInt();
            
            // For simplicity, we read the index into memory for search.
            // In a real DB, we'd use the B+Tree nodes on disk for O(log N) without full loading.
            for (int i = 0; i < entryCount; i++) {
                int kLen = raf.readInt();
                byte[] kBytes = new byte[kLen];
                raf.readFully(kBytes);
                String k = new String(kBytes, StandardCharsets.UTF_8);
                long offset = raf.readLong();
                
                if (k.equals(key)) {
                    // Jump to data offset
                    raf.seek(offset);
                    int dataKLen = raf.readInt();
                    raf.skipBytes(dataKLen); // Skip key
                    int vLen = raf.readInt();
                    byte[] vBytes = new byte[vLen];
                    raf.readFully(vBytes);
                    return vBytes;
                }
            }
        }
        return null;
    }
}
