package com.echodb.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

/**
 * WAL (Write-Ahead Log) provides durability by logging operations before 
 * applying them to the in-memory structure.
 */
public class WAL implements AutoCloseable {
    private final File logFile;
    private DataOutputStream out;
    private static final byte OP_PUT = 1;
    private static final byte OP_DELETE = 2;

    public WAL(String directory, String filename) throws IOException {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.logFile = new File(dir, filename);
        this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(logFile, true)));
    }

    /**
     * Logs a 'put' operation.
     */
    public synchronized void logPut(String key, byte[] value) throws IOException {
        out.writeByte(OP_PUT);
        out.writeUTF(key);
        out.writeInt(value.length);
        out.write(value);
        out.flush();
    }

    /**
     * Logs a 'delete' operation.
     */
    public synchronized void logDelete(String key) throws IOException {
        out.writeByte(OP_DELETE);
        out.writeUTF(key);
        out.flush();
    }

    /**
     * Replays the log into a handler.
     */
    public void replay(BiConsumer<String, byte[]> putHandler, java.util.function.Consumer<String> deleteHandler) throws IOException {
        if (!logFile.exists()) return;

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(logFile)))) {
            while (in.available() > 0) {
                byte op = in.readByte();
                String key = in.readUTF();
                if (op == OP_PUT) {
                    int valLen = in.readInt();
                    byte[] value = new byte[valLen];
                    in.readFully(value);
                    putHandler.accept(key, value);
                } else if (op == OP_DELETE) {
                    deleteHandler.accept(key);
                }
            }
        } catch (EOFException e) {
            // Reached end of file
        }
    }

    /**
     * Clears the WAL (e.g., after a MemTable flush).
     */
    public void clear() throws IOException {
        close();
        if (logFile.exists()) {
            logFile.delete();
        }
        this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(logFile, true)));
    }

    @Override
    public void close() throws IOException {
        if (out != null) {
            out.close();
            out = null;
        }
    }
}
