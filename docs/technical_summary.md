# EchoDB: Technical Project Summary

### 1. Problem Statement
General-purpose database engines often struggle with high write latency and inefficient handling of "hot data" (frequently accessed keys). Traditional static indexing structures like standard B-Trees or Hash Maps do not adapt to varying data access patterns, leading to suboptimal memory utilization and increased disk I/O in write-heavy workloads (e.g., IoT logging, real-time analytics).

### 2. Proposed Solution
**EchoDB** is an adaptive, high-throughput storage engine built on a **Log-Structured Merge-Tree (LSM)** architecture. The solution introduces "structural intelligence" by:
- Utilizing **Splay Trees** for the in-memory MemTable, allowing the database to physically reorganize itself to prioritize frequently accessed data.
- Implementing a multi-layered storage strategy that transforms random writes into sequential disk I/O.
- Integrating **Adaptive Huffman Coding** to compress data during background thread cycles, ensuring high storage density without sacrificing lookup speed.

### 3. Tech Stack Options
- **C (Hardcore / Highest Marks)**: Provides absolute control over disk blocks and memory. Shows the highest level of technical skill to your staff.
- **Java (Professional / Production Grade)**: Many famous databases (Cassandra, HBase) are in Java. Excellent for handling background threads (compaction).
- **Python (Logic & Algorithm Focus)**: Best if you want to focus 100% on the data structure logic (Splay Trees, B+Trees) without worrying about memory pointer bugs.

**The Approach (Algorithm Integration):**
1. **The In-Memory Layer (RAM)**: Data is initially written to a **Splay Tree**. Unlike standard trees, the Splay Tree moves the most recently used keys to the root, significantly reducing lookup time for "hot" data sets.
2. **The Persistence Layer (Disk)**: Data is flushed to immutable **SSTables**. To prevent full-file scans, a micro **B+Tree index** is embedded in the footer of each data block, allowing for $O(\log n)$ disk-based range queries.
3. **The Reliability Layer**: A **Write-Ahead Log (WAL)** is implemented to ensure data durability. In the event of a crash, the WAL is "replayed" to reconstruct the MemTable state.
4. **The Maintenance Layer (Compaction)**: A background processes manages file merging. We use a **Fibonacci Heap** to prioritize files for compaction based on age and deletion density, and apply **Huffman Coding** (Greedy Algorithm) to compress the resulting merged files.
