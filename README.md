# EchoDB 🚀

**EchoDB** is a high-performance, adaptive storage engine built for the **Advanced Data Structures & Algorithms (ADSA)** curriculum. It utilizes a **Log-Structured Merge-Tree (LSM-Tree)** architecture but disrupts the standard model by using **Self-Adjusting Algorithms**.

## 🌟 Key Features
- **Adaptive Memory Layer**: Uses a **Splay Tree** to reorganize hot data automatically.
- **Multi-Level Indexing**: Uses **B+Trees** for efficient range queries on disk.
- **Smart Compaction**: Uses **Fibonacci Heaps** to prioritize background merge tasks.
- **Embedded Compression**: Implements **Huffman Coding** directly into the storage cycle.

---

## 📂 Project Structure
```text
src/main/java/com/echodb/
├── core/                # Storage Engine logic (MemTable)
├── structures/         # ADSA Data Structures (Splay Tree, B+Tree, Fibonacci Heap)
├── compress/           # Storage Optimization (Huffman)
└── util/               # Durability Utilities (WAL)
```

## 🛠️ Getting Started

### Prerequisites
- **Java 11** or higher.
- **Maven** (configured in your Path).

### Run All Tests
To verify all implemented algorithms and core logic:
```bash
mvn test
```

### Manual Compilation
If Maven is not installed, you can use the provided script in `docs/TESTING.md`.

---

## 📈 Status: Phase 2 Complete
- [x] Splay Tree Implementation (Unit I)
- [x] B+Tree Indexing Logic (Unit I)
- [x] Fibonacci Heap Priority Queue (Unit I)
- [x] MemTable Buffer with thread-safety (Unit I)
- [x] Write-Ahead Log (WAL) for durability (Phase 2)
- [x] SSTable Persistence with Indexing (Phase 2)
- [x] Storage Engine Integration (Phase 2)
- [x] Maven Integration

---

## 📜 License
Academic Project - ADSA SEM 4.
