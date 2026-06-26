/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import model.Product;
import service.FilterEngine;
import service.ProductSorter;
import service.SearchEngine;
import service.ViewedProductsHistory;
import view.ConsoleView;

/**
 *
 * @author ADMIN
 */
public class Controller {

    private final FilterEngine filterEngine;
    private final SearchEngine searchEngine;
    private final ViewedProductsHistory historyManager;
    private final ProductSorter productSorter;
    private final ConsoleView consoleView;
    private final boolean isSaved;

    public Controller() {
        // Khởi tạo các engine và sorter
        filterEngine = new FilterEngine();
        searchEngine = new SearchEngine();
        historyManager = new ViewedProductsHistory();
        productSorter = new ProductSorter();
        consoleView = new ConsoleView();

        // Tải dữ liệu sản phẩm ban đầu vào hệ thống
        loadInitialData();
        isSaved = true;
    }

    public void startProgram() {
        while (true) {
            consoleView.callMainMenu();
            int choice = consoleView.getChoiceInput();

            switch (choice) {
                case 1:
                    filterAndPaginate();        // 1. Filter Products
                    break;
                case 2:
                    searchProducts();           // 2. Search Products (SearchEngine)
                    break;
                case 3:
                    sortProductList();          // 3. Sort Products
                    break;
                case 4:
                    manageViewedHistory();      // 4. Manage Viewed History
                    break;
                case 5:
                    exitProgram();              // 5. Quit program
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // =================================================================
    // CÁC HÀM XỬ LÝ LOGIC CHI TIẾT
    // =================================================================
    private void loadInitialData() {
        // 1. Khởi tạo danh sách các sản phẩm mẫu (Khớp với Constructor của Product.java)
        Product p1 = new Product("P003", "iPhone 14 Pro Max", "Smartphone", 1099.00, "Apple", 4.8, 1500);
        Product p2 = new Product("P001", "MacBook Air M2", "Laptop", 1199.00, "Apple", 4.9, 2500);
        Product p3 = new Product("P005", "Dell XPS 13 OLED", "Laptop", 1399.00, "Dell", 4.6, 1200);
        Product p4 = new Product("P002", "Samsung Galaxy S23 Ultra", "Smartphone", 999.00, "Samsung", 4.7, 1800);
        Product p5 = new Product("P004", "Sony WH-1000XM5", "Headphones", 399.00, "Sony", 4.5, 900);

        // 2. Thêm dữ liệu vào cây BST của SearchEngine
        // Mẹo nhỏ: Đưa phần tử trung vị "P003" vào trước để làm gốc (Root), 
        // sau đó thêm các phần tử khác sẽ giúp cây nhị phân BST cân bằng tốt hơn, tối ưu tốc độ tìm kiếm.
        searchEngine.insert(p1);
        searchEngine.insert(p2);
        searchEngine.insert(p3);
        searchEngine.insert(p4);
        searchEngine.insert(p5);

        System.out.println("[Hệ thống] Đã tự động nạp " + 5 + " sản phẩm mẫu vào SearchEngine!");
    }

    private void manageProductInformation() {
        System.out.println("--- Manage Product Information ---");
    }

    private void filterAndPaginate() {
        System.out.println("--- Filter & Paginate Products ---");
    }

    private void searchProducts() {
        while (true) {
            System.out.println("\n----------------- SEARCH ENGINE -----------------");
            System.out.println("1. Search Product by ID");
            System.out.println("2. Search Product by Keyword (Name, Category, Brand)");
            System.out.println("3. Back to Main Menu");
            System.out.println("-------------------------------------------------");

            // Sử dụng utils.Inputter để lấy lựa chọn của người dùng
            int choice = utils.Inputter.getIntAllowEmpty("Enter your choice: ", "Invalid choice!", 1);

            // Nếu người dùng nhấn Enter trống (trả về -1 theo hàm getIntAllowEmpty)
            if (choice == -1) {
                System.out.println("Please enter a choice.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Search Product by ID ---");
                    String id = utils.Inputter.getString("Enter Product ID to search: ");

                    // Gọi hàm tìm kiếm đích danh bằng ID trong SearchEngine (Tốc độ O(log n))
                    model.Product foundProduct = searchEngine.searchById(id);

                    if (foundProduct != null) {
                        System.out.println("\n[FOUND] Product details:");
                        System.out.println(foundProduct.toString());
                    } else {
                        System.out.println("No product found with ID: " + id);
                    }
                    break;

                case 2:
                    System.out.println("\n--- Search Product by Keyword ---");
                    String keyword = utils.Inputter.getString("Enter keyword to search: ");

                    // Gọi hàm tìm kiếm theo từ khóa đa trường (ID, Tên, Category, Brand)
                    java.util.ArrayList<model.Product> results = searchEngine.searchByKeyword(keyword);

                    if (results.isEmpty()) {
                        System.out.println("No products found with keyword: " + keyword);
                    } else {
                        System.out.println("\n[FOUND] " + results.size() + " product(s):");
                        for (model.Product p : results) {
                            System.out.println(p.toString());
                        }
                    }
                    break;

                case 3:
                    System.out.println("Returning to Main Menu...");
                    return; // Thoát khỏi hàm searchProducts(), quay lại Main Menu của vòng lặp startProgram()

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void sortProductList() {
        System.out.println("--- Sort Products ---");

        // GỢI Ý LOGIC CODE SẼ NẰM Ở ĐÂY:
        // 1. Lấy danh sách sản phẩm hiện tại (ví dụ lấy từ filterEngine)
        // List<Product> currentList = filterEngine.getProductList();
        // 2. Hỏi user muốn xếp theo giá hay rating...
        // Comparator<Product> criteria = ... (tạo comparator tương ứng)
        // 3. Gọi productSorter để xử lý:
        // productSorter.sortProducts(currentList, criteria);
        // 4. In danh sách đã sắp xếp ra màn hình
    }

    private void manageViewedHistory() {
        System.out.println("--- Viewed Products History ---");
    }

    private void exitProgram() {
        System.out.println("Exiting E-Commerce Product Management... Goodbye!");
    }
}
