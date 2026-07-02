/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import java.util.Iterator;
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
    private boolean isSaved;
    //data for all system
    private java.util.List<model.Product> systemProductList;
    private static final int FILTER_PAGE_SIZE = 10;

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
        consoleView.displayFilterMenu();

        int choice = utils.Inputter.getChoice(
                "  Choice: ", "  Out of range!", "  Invalid!", 1, 4);

        String field;
        boolean isDouble;
        double min = 0, max = 0;
        String text = null;

        switch (choice) {
            case 1:
                field = "price";
                min = utils.Inputter.getDoubleAllowEmpty("  Min price $ (Enter=0): ", 0);
                max = utils.Inputter.getDoubleAllowEmpty("  Max price $ (Enter=no limit): ", Double.MAX_VALUE);
                isDouble = true;
                break;
            case 2:
                field = "rating";
                min = utils.Inputter.getDoubleAllowEmpty("  Min rating 0.0-5.0 (Enter=0): ", 0);
                max = utils.Inputter.getDoubleAllowEmpty("  Max rating min - 5.0 (Enter=0): ", 0);;
                isDouble = true;
                break;
            case 3:
                field = "category";
                text = utils.Inputter.getString("  Category: ");
                isDouble = false;
                break;
            default:
                field = "brand";
                text = utils.Inputter.getString("  Brand: ");
                isDouble = false;
                break;
        }

        int page = 1;
        while (true) {
            service.FilterEngine.PageResult<model.Product> r = isDouble
                    ? filterEngine.filterByDoublePaged(field, min, max, page, FILTER_PAGE_SIZE)
                    : filterEngine.filterByStringPaged(field, text, page, FILTER_PAGE_SIZE);

            if (r.totalItems == 0) {
                System.out.println("\n  No products found.");
                break;
            }

            consoleView.displayProductList(r.items, page, FILTER_PAGE_SIZE);
            System.out.printf("  Page %d/%d  -  %d result(s) found.%n", r.page, r.totalPages, r.totalItems);

            if (r.totalPages <= 1) {
                break;
            }

            String nav = utils.Inputter.getStringRegex(
                    "  [n] Next  [p] Previous  Enter to exit: ",
                    "  Invalid input! Only 'n', 'p' or Enter is allowed.",
                    "[npNP]"
            ).toLowerCase();

            if (nav.isEmpty()) {
                break;
            } else if (nav.equals("n")) {
                if (page < r.totalPages) {
                    page++;
                } else {
                    System.out.println("  Already at the last page.");
                }
            } else { // nav.equals("p")
                if (page > 1) {
                    page--;
                } else {
                    System.out.println("  Already at the first page.");
                }
            }
        }
    }

    private void searchProducts() {
        while (true) {
            System.out.println("\n----------------- SEARCH ENGINE -----------------");
            System.out.println("1. Search Product by ID");
            System.out.println("2. Search Product by name/keyword");
            System.out.println("3. Back to Main Menu");
            System.out.println("-------------------------------------------------");

            int choice = utils.Inputter.getIntAllowEmpty("Enter your choice: ", "Invalid choice!", 1);

            if (choice == -1) {
                System.out.println("Please enter a choice.");
                continue;
            }

            if (choice == 3) {
                System.out.println("Returning to Main Menu...");
                return; // Thoát hẳn menu Search
            }

            // Biến dùng chung để chứa dữ liệu tìm được từ cả 2 case
            java.util.List<model.Product> results = new java.util.ArrayList<>();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Search Product by ID ---");
                    String id = utils.Inputter.getString("Enter Product ID to search: ");
                    model.Product foundProduct = searchEngine.searchById(id);
                    
                    if (foundProduct != null) {
                        results.add(foundProduct); // Cho sản phẩm tìm được vào list
                    }
                    break;

                case 2:
                    System.out.println("\n--- Search Product by Keyword ---");
                    String keyword = utils.Inputter.getString("Enter keyword to search: ");
                    // Lấy toàn bộ danh sách kết quả
                    results = searchEngine.searchByKeyword(keyword);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    continue; // Nếu nhập sai thì quay lại in Menu Search luôn, bỏ qua phân trang
            }

            // =========================================================
            // XỬ LÝ PHÂN TRANG NẰM NGOÀI SWITCH (GIỐNG FILTER)
            // =========================================================
            
            if (results.isEmpty()) {
                System.out.println("\n  No products found.");
                continue; // Không có kết quả thì quay lại in Menu Search
            }

            int page = 1;
            int totalItems = results.size();
            int totalPages = (int) Math.ceil((double) totalItems / FILTER_PAGE_SIZE);

            while (true) {
                int start = (page - 1) * FILTER_PAGE_SIZE;
                int end = Math.min(start + FILTER_PAGE_SIZE, totalItems);

                // Cắt list để lấy data của trang hiện tại
                java.util.List<model.Product> pageItems = results.subList(start, end);

                // Hiển thị sản phẩm
                consoleView.displayProductList(pageItems, page, FILTER_PAGE_SIZE);
                System.out.printf("  Page %d/%d  -  %d result(s) found.%n", page, totalPages, totalItems);

                // Lưu lịch sử xem cho các sản phẩm đang hiển thị
                for (model.Product p : pageItems) {
                    historyManager.addViewedProduct(p);
                }

                if (totalPages <= 1) {
                    break; // Thoát vòng lặp phân trang, quay lại menu Search
                }

                // Xử lý nhập liệu điều hướng
                String nav = utils.Inputter.getStringRegex(
                        "  [n] Next  [p] Previous  Enter to exit: ",
                        "  Invalid input! Only 'n', 'p' or Enter is allowed.",
                        "[npNP]"
                ).toLowerCase();

                if (nav.isEmpty()) {
                    break; // Nhấn Enter để thoát phân trang
                } else if (nav.equals("n")) {
                    if (page < totalPages) {
                        page++;
                    } else {
                        System.out.println("  Already at the last page.");
                    }
                } else { // nav.equals("p")
                    if (page > 1) {
                        page--;
                    } else {
                        System.out.println("  Already at the first page.");
                    }
                }
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
                        Iterator<Product> iterator = historyManager.iterator();
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
        boolean historySuccess = utils.FileUtils.saveViewedHistory(historyManager.toOrderedList());

        if (success && historySuccess) {
            isSaved = true;
            System.out.println("Saved " + systemProductList.size() + " products and viewed history successfully.");
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
