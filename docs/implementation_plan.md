# EchoDB: Java Implementation Plan

EchoDB will be implemented as a high-performance Java library (JAR) that can be embedded into other applications.

## 📁 Project Structure (Maven Template)
```text
src/main/java/com/echodb/
├── core/                # Main Storage Engine logic
│   ├── StorageEngine.java
│   ├── MemTable.java
│   └── SSTableManager.java
├── structures/         # ADSA Data Structures
│   ├── SplayTree.java   (Unit I)
│   ├── BPlusTree.java   (Unit I)
│   └── FibonacciHeap.java (Unit I)
├── compress/           # Storage Optimization
│   ├── HuffmanCoder.java (Unit III)
│   └── LCSDeduplicator.java (Unit III)
└── util/
    ├── WAL.java         # Write-Ahead Log
    └── FileUtils.java
```

## 🛠️ Java Tech Stack
- **Build System**: Maven (recommended for dependency management).
- **Java Version**: 11 or higher (for better file I/O performance).
- **Core Library**: `java.nio` (New I/O) for high-performance memory-mapped files.

## 📅 8-Week Implementation Roadmap

### Phase 1: The Memory Layer (Weeks 1-2)
- **Goal**: Build the self-adjusting cache.
- **Tasks**:
    - Implement `SplayTree.java` with generics `<K extends Comparable<K>, V>`.
    - Implement the `MemTable` wrapper to handle concurrent access.
    - [Syllabus: Unit I]

### Phase 2: Persistence & Durability (Weeks 3-4)
- **Goal**: Make data survive crashes.
- **Tasks**:
    - Implement `WAL.java` (Append-only file storage).
    - Create `SSTable` writer that flushes MemTable to disk in sorted order.
    - Implement the `BPlusTree` index to be saved at the end of each SSTable file.
    - [Syllabus: Unit I]

### Phase 3: Background Optimization (Weeks 5-6)
- **Goal**: Manage disk space and speed up searches.
- **Tasks**:
    - Implement `FibonacciHeap.java` to prioritize which SSTables need merging.
    - Build the `Compactor` thread to merge multiple SSTables.
    - [Syllabus: Unit I & II]

### Phase 4: Data Compression (Weeks 7-8)
- **Goal**: "Disruptive" storage density.
- **Tasks**:
    - Implement `HuffmanCoder.java` to compress data blocks before writing to SSTables.
    - Write a simple benchmark suite to compare EchoDB performance vs. standard File storage.
    - [Syllabus: Unit III]

## ✅ Next Steps
1. Initialize the Maven project.
2. Create the `SplayTree` class—the heart of the adaptive engine.
