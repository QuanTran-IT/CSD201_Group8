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
                    manageProductInformation(); 
                    break;
                case 2:
                    filterAndPaginate();        
                    break;
                case 3:
                    searchProducts();           
                    break;
                case 4:
                    sortProductList();          
                    break;
                case 5:
                    manageViewedHistory();      
                    break;
                case 6:
                    exitProgram();
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
        // Đọc dữ liệu...
    }

    private void manageProductInformation() {
        System.out.println("--- Manage Product Information ---");
    }

    private void filterAndPaginate() {
        System.out.println("--- Filter & Paginate Products ---");
    }

    private void searchProducts() {
        System.out.println("--- Search Products ---");
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

    private void exitProgram() {
        System.out.println("Exiting E-Commerce Product Management... Goodbye!");
    }
}
