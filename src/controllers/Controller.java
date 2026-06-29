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
import service.ProductsIterator;
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
    private boolean isSaved;
    //data for all system
    private java.util.List<model.Product> systemProductList;

    public Controller() {
    filterEngine = new FilterEngine();
    searchEngine = new SearchEngine();
    historyManager = new ViewedProductsHistory();
    productSorter = new ProductSorter();
    consoleView = new ConsoleView();

    systemProductList = utils.FileUtils.loadProducts();
    if (systemProductList == null) {
        systemProductList = new java.util.ArrayList<>();
        System.out.println("[System] No data file found. Starting with empty list.");
    }
    java.util.Collections.shuffle(systemProductList);
    // Cả 2 engine nhận chung systemProductList
    searchEngine.setProductList(systemProductList);
    filterEngine.setProductList(systemProductList);
    
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



    private void filterProduct() {
//        filterEngine.setProductList(systemProductList);

        consoleView.displayFilterMenu();

        int choice = utils.Inputter.getChoice(
                "  Choice: ", "  Out of range!", "  Invalid!", 1, 4);

        java.util.List<model.Product> result = new java.util.ArrayList<>();

        switch (choice) {
            case 1:
                double min = utils.Inputter.getDoubleAllowEmpty("  Min price $ (Enter=0): ", 0);
                double max = utils.Inputter.getDoubleAllowEmpty("  Max price $ (Enter=no limit): ", Double.MAX_VALUE);
                result = filterEngine.filterByDouble("price", min, max);
                break;
            case 2:
                double minRating = utils.Inputter.getDoubleAllowEmpty("  Min rating 0.0-5.0 (Enter=0): ", 0);
                result = filterEngine.filterByDouble("rating", minRating, 5.0);
                break;
            case 3:
                String category = utils.Inputter.getString("  Category: ");
                result = filterEngine.filterByString("category", category);
                break;
            case 4:
                String brand = utils.Inputter.getString("  Brand: ");
                result = filterEngine.filterByString("brand", brand);
                break;
        }

        if (result.isEmpty()) {
            System.out.println("\n  No products found.");
        } else {
            consoleView.displayProductList(result, 1, result.size());
            System.out.printf("  %d result(s) found.%n", result.size());
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
                        historyManager.addViewedProduct(foundProduct); 
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
                            historyManager.addViewedProduct(p);
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
        System.out.println("\n--- Sort Products ---");
        System.out.println("1. Sort by PRICE (Ascending: Lowest to Highest)");
        System.out.println("2. Sort by RATING (Descending: 5 Stars to 1 Star)");
        System.out.println("3. Sort by POPULARITY (Descending: Most Viewed to Least Viewed)");
        System.out.print("Enter your choice: ");

        int sortChoice = consoleView.getChoiceInput();

        if (systemProductList == null || systemProductList.isEmpty()) {
            System.out.println("Product list is empty, cannot sort.");
            return;
        }
        ArrayList<Product> currentList = new ArrayList<>(systemProductList);
        switch (sortChoice) {
            case 1:
                productSorter.sortProducts(currentList, (Product p1, Product p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
                System.out.println("\nSuccessfully sorted by PRICE (Ascending)!");
                break;

            case 2:
                productSorter.sortProducts(currentList, (Product p1, Product p2) -> Double.compare(p2.getRating(), p1.getRating()));
                System.out.println("\nSuccessfully sorted by RATING (Descending)!");
                break;

            case 3:
                productSorter.sortProducts(currentList, (Product p1, Product p2) -> Integer.compare(p2.getViews(), p1.getViews()));
                System.out.println("\nSuccessfully sorted by POPULARITY (Descending)!");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
                return; 
        }

        System.out.println("\n--- Preview (Top 10 products) ---");
             int limit = Math.min(currentList.size(), 10);
        java.util.List<Product> top10 = currentList.subList(0, limit);

        // Truyền danh sách đã cắt gọn sang cho hàm của bạn cậu hiển thị
        consoleView.displayProductList(top10, 1, 10);
        
        utils.Inputter.getString("\nPress Enter to continue...");
    }
 
 
    private void manageViewedHistory() {
        while (true) {
        consoleView.displayHistoryMenu();

        int choice = utils.Inputter.getChoice(
            "  Choice: ", "  Out of range!", "  Invalid!", 1, 5);

        switch (choice) {
            case 1:
                if (historyManager.isEmpty()) {
                    System.out.println("\n  History is empty.");
                } else {
                    System.out.println("\n  Recently viewed (most recent first):");
                    ProductsIterator iterator = historyManager.iterator();
                    int index = 1;
                    while (iterator.hasNext()) {
                        System.out.printf("  #%d. %s%n", index++, iterator.next());
                    }
                }
                break;


            case 2:
                String removeId = utils.Inputter.getString("  Enter Product ID to remove: ");
                historyManager.removeViewedProduct(removeId);
                System.out.println("  Removed from history: " + removeId);
                break;

            case 3:
                historyManager.clear();
                System.out.println("  History cleared.");
                break;

            case 4:
                return;
        }

        utils.Inputter.getString("\n  Press Enter to continue...");
    }
    }

    private void saveFile() {
        System.out.println("\n--- Save Data ---");

        boolean success = utils.FileUtils.saveProducts(systemProductList);

        if (success) {
            isSaved = true;
            System.out.println("Saved " + systemProductList.size() + " products successfully.");
        } else {
            System.out.println("Failed to save file. Please try again.");
        }

        utils.Inputter.getString("\nPress Enter to continue...");
    }

    private void exitProgram() {
        if (!isSaved) {
            boolean wantSave = utils.Inputter.confirmYesNo(
                    "You have unsaved data. Save before exiting? (y/n): ");
            if (wantSave) {
                saveFile();
            }
        }

        System.out.println("Thank you for using the system. Goodbye!");
        
    }
}
