# EchoDB: Manual Testing Guide

Now that you have Maven installed, testing the project is much easier. You no longer need long `javac` commands.

---

## 🚀 How to Run All Tests (The Easy Way)
Since you have Maven, you can run all tests with a single command from the project root (`e:\SEM 4\ADSA\EchoDB`):
```powershell
# Run all tests using Maven
mvn test
```

If you want to run specific tests or compile manually (as a fallback), see the sections below.

---

## 📂 Manual Compilation & Execution (Fallback)
If Maven is not working, you can still use the direct Java commands:
```powershell
# Compile everything
javac -d bin src/main/java/com/echodb/structures/*.java src/main/java/com/echodb/core/*.java src/test/java/com/echodb/structures/*ManualTest.java src/test/java/com/echodb/core/*ManualTest.java

# Run Splay Tree Test
java -cp bin -ea com.echodb.structures.SplayTreeManualTest

# Run B+ Tree Test
java -cp bin -ea com.echodb.structures.BPlusTreeManualTest

# Run Fibonacci Heap Test
java -cp bin -ea com.echodb.structures.FibonacciHeapManualTest

# Run MemTable Test
java -cp bin -ea com.echodb.core.MemTableManualTest
```
*(Note: `-ea` stands for "Enable Assertions", which is required to see the test failures.)*

---

## 📂 Individual Component Tests

### 1. Splay Tree (The Adaptive Layer)
Tests insertion, retrieval, and the self-adjusting root behavior.
```powershell
javac -d bin src/main/java/com/echodb/structures/SplayTree.java src/test/java/com/echodb/structures/SplayTreeManualTest.java
java -cp bin -ea com.echodb.structures.SplayTreeManualTest
```

### 2. B+ Tree (The Disk Index)
Tests multi-level insertion (splitting nodes) and retrieval.
```powershell
javac -d bin src/main/java/com/echodb/structures/BPlusTree.java src/test/java/com/echodb/structures/BPlusTreeManualTest.java
java -cp bin -ea com.echodb.structures.BPlusTreeManualTest
```

### 3. MemTable (The Thread-Safe Buffer)
Tests size-tracking, thread-locks, and the "flush-needed" trigger.
```powershell
javac -d bin src/main/java/com/echodb/structures/SplayTree.java src/main/java/com/echodb/core/MemTable.java src/test/java/com/echodb/core/MemTableManualTest.java
java -cp bin -ea com.echodb.core.MemTableManualTest
```

### 4. Fibonacci Heap (The Compaction Priority)
Tests high-efficiency priority queuing for background tasks.
```powershell
javac -d bin src/main/java/com/echodb/structures/FibonacciHeap.java src/test/java/com/echodb/structures/FibonacciHeapManualTest.java
java -cp bin -ea com.echodb.structures.FibonacciHeapManualTest
```

---

## 🛡️ What to Look For
- **Success**: You should see `All Tests Passed!` in the terminal.
- **Failure**: If an assertion fails, the program will crash with an `AssertionError`. This means the algorithm has a bug.
