# E-commerce Product Catalog Filter

## 📌 Project Overview
The **E-commerce Product Catalog Filter System** is an algorithmic and data structure-focused project developed for the Data Structures and Algorithms (CSD201) course. Modern e-commerce platforms contain thousands of products, making it difficult for users to find items quickly. This system is designed to efficiently manage, search, filter, and sort product data, maintaining high performance and scalability even when handling large datasets of over 50,000 products.

## ⚙️ Core Modules & System Architecture
The system is decomposed into four main functional modules. Based on our Conceptual Framework, each module is powered by specific data structures to optimize time and space complexity:

### 🔍 1. Search Engine (Tree-Based Search)
* **Responsibility:** Processes keyword-based product searching and exact ID lookups.
* **Data Structure:** Utilizes a **Binary Search Tree (BST)** (`BinarySearchTree<Product>`).
* **Performance:** Achieves **O(log n)** time complexity for search, insertion, and traversal operations, efficiently replacing O(n) linear scans.

### 🎯 2. Product Filtering System (Linear Scan with Early-Exit)
* **Responsibility:** Allows users to narrow down products dynamically by category, price bounds, brand, and rating.
* **Data Structure & Algorithm:** Utilizes an `ArrayList<Product>` for sequential filtering. 
* **Performance:** Implements an early-exit pagination strategy, reducing the worst-case O(n) complexity to an efficient **O(M)** (where M is the number of checked items until the page is full).

### ⚡ 3. Product Sorting System (Divide and Conquer)
* **Responsibility:** Sorts the product catalog dynamically based on Price, Popularity (views), and user Rating.
* **Data Structure & Algorithm:** Implements an **Iterative Quick Sort** algorithm directly on an `ArrayList<Product>`. It utilizes a custom `Stack<Integer>` to track sub-array boundaries, entirely eliminating recursion overhead.
* **Performance:** Achieves an optimal average time complexity of **O(n log n)** leveraging fast O(1) index-based swapping.

### 🕒 4. Recently Viewed Products (Chronological Tracking)
* **Responsibility:** Maintains the user's browsing history for quick access and seamless shopping convenience.
* **Data Structure:** Employs a hybrid approach combining a **Doubly LinkedList** and a **HashMap** (`HashMap<String, Node>`).
* **Performance:** This combination ensures that duplicate prevention, node retrieval, chronological insertions, and sequence updates are executed in strictly **O(1)** constant time.

## 🔬 Research Focus & Performance Experiment
A core academic component of this project is empirical performance testing. 

* **Research Question:** *How does the choice between `TreeMap` and `ArrayList` affect the efficiency of price range filtering operations in an e-commerce product catalog with more than 50,000 products?*
* **Hypothesis:** While sequential filtering with an `ArrayList` is sufficient for small lists, transitioning to a tree-structured data organization (`TreeMap`) is expected to demonstrate significantly faster logarithmic-time filtering. This experiment evaluates the scalability limitations of linear data traversal versus the enhanced responsiveness of tree-based filtering in real-world scenarios.
