/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import model.Product;
import service.FilterEngine;
import service.ProductSorter;
import service.SearchEngine;
import service.Comparator;
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
    //data for all system
    private java.util.List<model.Product> systemProductList;

    public Controller() {
        // Khởi tạo các engine và sorter
        filterEngine = new FilterEngine();
        searchEngine = new SearchEngine();
        historyManager = new ViewedProductsHistory();
        productSorter = new ProductSorter();
        consoleView = new ConsoleView();
        systemProductList = utils.FileUtils.loadProducts();
        if (systemProductList == null) {
            systemProductList = new java.util.ArrayList<>();
        }
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
                    filterProduct();        // 1. Filter Products
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
                    saveFile();      // 4. Manage Viewed History
                    break;
                case 6:
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

   private void filterProduct() {
    // Sync FilterEngine với data mới nhất (đề phòng có thêm/xóa sản phẩm)
    filterEngine.setProductList(systemProductList);
    consoleView.displayFilterMenu();

    // ── Thu thập tiêu chí lọc ──────────────────────────────────────────────
    String category = utils.Inputter.getStringRegex(
        "  Category (Laptop/Smartphone/Headphones, Enter=All): ",
        "  Letters only!", "[A-Za-z]+");
    if (category.isEmpty()) category = null;

    double minPrice = utils.Inputter.getDoubleAllowEmpty(
        "  Min price $ (Enter=0): ", 0);
    double maxPrice = utils.Inputter.getDoubleAllowEmpty(
        "  Max price $ (Enter=no limit): ", Double.MAX_VALUE);

    String brand = utils.Inputter.getStringRegex(
        "  Brand (Apple/Samsung/Dell/Sony, Enter=All): ",
        "  Letters only!", "[A-Za-z]+");
    if (brand.isEmpty()) brand = null;

    double minRating = utils.Inputter.getDoubleAllowEmpty(
        "  Min rating 0-5 (Enter=0): ", 0);

    // ── Vòng lặp phân trang ────────────────────────────────────────────────
    final int PAGE_SIZE = 3;
    int pageNumber = 1;

    while (true) {
        java.util.List<model.Product> results = filterEngine.productFilter(
            category, minPrice, maxPrice, brand, minRating, pageNumber, PAGE_SIZE);

        consoleView.displayProductList(results, pageNumber, PAGE_SIZE);

        // Hết kết quả
        if (results.isEmpty()) {
            if (pageNumber == 1) System.out.println("  No products match your criteria.");
            else { System.out.println("  No more products."); pageNumber--; }
            break;
        }

        boolean isLastPage = (results.size() < PAGE_SIZE);

        // Hiển thị nav phù hợp với trang hiện tại
        System.out.print("  Options: ");
        if (!isLastPage)  System.out.print("[N]ext  ");
        if (pageNumber > 1) System.out.print("[P]rev  ");
        System.out.println("[Q]uit");

        String nav = utils.Inputter.getString("  Choice: ").toUpperCase();

        switch (nav) {
            case "N":
                if (isLastPage) System.out.println("  Already on last page.");
                else pageNumber++;
                break;
            case "P":
                if (pageNumber > 1) pageNumber--;
                else System.out.println("  Already on first page.");
                break;
            case "Q":
                System.out.println("  Returning to main menu...");
                return;
            default:
                System.out.println("  Invalid choice.");
        }
    }

    utils.Inputter.getString("\n  Press Enter to continue...");
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
        
        System.out.println("1. Sort by PRICE (Ascending: Lowest to Highest)");
        System.out.println("2. Sort by RATING (Descending: 5 Stars to 1 Star)");
        System.out.println("3. Sort by POPULARITY (Descending: Most Viewed to Least Viewed)");
        System.out.print("Enter your choice: ");
        
        int sortChoice = consoleView.getChoiceInput(); 
        
        // 1. Lấy danh sách sản phẩm hiện tại từ FilterEngine
        ArrayList<Product> currentList = (ArrayList<Product>) filterEngine.getProductList();
        
        if (currentList == null || currentList.isEmpty()) {
            System.out.println("Danh sách sản phẩm đang trống, không thể sắp xếp.");
            return;
        }

        // 2. Controller điều phối: Truyền Comparator tự tạo vào cho ProductSorter
        switch (sortChoice) {
            case 1:
                productSorter.sortProducts(currentList, new service.Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Double.compare(p1.getPrice(), p2.getPrice());
                    }
                });
                System.out.println("Đã sắp xếp danh sách theo Giá tăng dần!");
                break;
                
            case 2:
                productSorter.sortProducts(currentList, new service.Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Double.compare(p2.getRating(), p1.getRating()); 
                    }
                });
                System.out.println("Đã sắp xếp danh sách theo Đánh giá giảm dần!");
                break;
                
            case 3:
                productSorter.sortProducts(currentList, new service.Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        return Integer.compare(p2.getViews(), p1.getViews()); 
                    }
                });
                System.out.println("Đã sắp xếp danh sách theo Lượt xem giảm dần!");
                break;
                
            default:
                System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại!");
                return;
    }
    }
    private void manageViewedHistory() {
        System.out.println("--- Viewed Products History ---");
    }

    private void saveFile() {

    }

    private void exitProgram() {
        
    }
}
