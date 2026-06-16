E-commerce Product Catalog Filter
📌 Project Overview
  This project is an algorithmic and data structure-focused system designed to efficiently manage, search, filter, and sort products for e-commerce platforms handling over 50,000 products. Developed for the Data Structures and Algorithms (CSD201) course, the system prioritizes high-performance retrieval and system scalability through optimal data organization patterns.  
⚙️ Core Modules & Data Architecture
The system is divided into four main functional modules, each utilizing specific data structures to maximize performance:
- 🔍 Search Engine (Hybrid Search):
    + Combines constant-time O(1) exact lookups using HashMap<String, Product> for product IDs with an ArrayList<Product> for flexible, linear sequential scanning of keywords.
- 🎯 Product Filtering System (Tree Traversal):
    + Allows users to narrow down products by category, price range, brand, and rating.
    + It leverages TreeMap structures to achieve logarithmic-time O(log n) efficiency for range-based queries, such as price ranges.
- ⚡ Product Sorting System (Divide and Conquer):
    + Sorts products dynamically by price, popularity, and user rating.
    + Implements an Iterative QuickSort algorithm directly on an ArrayList to perform fast index-based swapping, achieving an average time complexity of O(n log n).
- 🕒 Recently Viewed Products (Chronological Tracking):
    + Maintains the user's browsing history by utilizing a Doubly LinkedList paired with a HashMap.
    + This hybrid memory management ensures that node retrieval, chronological insertions, and sequence updates are executed in constant O(1) time.
🔬 Research Focus
- A core component of this project is the investigation of data structure efficiency under heavy loads. The primary research question evaluates the performance impact of using tree-structured data (TreeMap) versus linear data traversal (ArrayList) when executing price range filtering operations on a catalog with more than 50,000 products.
👨‍💻 Contributors
  - Đào Thị Ngọc Trâm (SE201480)
  - Trần Lê Anh Quân (SE200441)
  - Nguyễn Việt Tân (SE201036)
  - Trần Lê Khánh Toàn (SE201448) 
