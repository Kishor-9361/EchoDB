# EchoDB: Reality Check & Scoping

It’s great that you’re thinking about the scale! You aren't "beating Google"—you’re demonstrating that you understand the **foundations** Google uses. Here is how we make this possible in 2 months.

---

## 🛑 What Not To Do (The "Google Scale" Traps)
To stay within 2 months, we **skip** these features of production databases:
1.  **Network Protocol**: No TCP/REST interface (it’s an "Embedded" library, just like SQLite).
2.  **Advanced Concurrency Control**: No complex multi-user locking (we use a single-writer, multi-reader model).
3.  **Distributed Logic**: No sharding or replication across servers.
4.  **SQL Parser**: No SQL support (it’s a simple `Get(key)`, `Put(key, value)` Key-Value store).

---

## ✅ The "Mini-Project" Core (The 2-Month Scope)
Focus strictly on the algorithms in your syllabus. This makes the project look massive but stay manageable.

| Week | Milestone | Purpose |
| :--- | :--- | :--- |
| **1-2** | **The MemTable** | Implement the **Splay Tree** to handle in-memory writes. |
| **3-4** | **The SSTable & B+Tree** | Persistent storage logic. Save sorted data to files and use a **B+Tree** index for fast lookups. |
| **5-6** | **The Compaction Engine** | Implement the background "Merge" process. Use **Fibonacci Heaps** to pick which files to merge. |
| **7** | **Compression** | Integrate the **Huffman Coding** into the file-writing layer. |
| **8** | **Testing & Benchmarks** | Run tests comparing Splay Tree performance vs. a standard Binary Search Tree. |

---

## 🏆 Why It’s "Possible" but "Impressive"
- **It’s Possible** because the "Core" logic of a database takes about 2,000–3,000 lines of code. This is very doable in 60 days for a student who knows basic DSA.
- **It’s Impressive** because most students don't touch low-level file I/O or custom memory management. By using **Splay Trees** and **Huffman**, you show you aren't just using `Dictionary<string, string>`, but building it from the ground up.

**Think of it like this:**
Google’s LevelDB is a Formula 1 car. Your EchoDB is a high-performance Go-Kart you built yourself using the same engine principles. You aren't racing the F1 car in the same league, but you're showing everyone you know exactly how the engine works.
