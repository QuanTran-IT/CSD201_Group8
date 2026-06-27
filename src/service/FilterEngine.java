package service;

import java.util.ArrayList;
import java.util.List;
import model.Product;

public class FilterEngine {

    // Lưu trữ dữ liệu nội bộ của Engine
    private List<Product> productList = new ArrayList<>();

    // Hàm nhận dữ liệu từ Controller truyền sang
    public void setProductList(List<Product> allProducts) {
        if (allProducts != null) {
            this.productList = allProducts;
        }
    }

    // Thuật toán lọc (đã bỏ tham số List<Product> ở đầu)
    public List<Product> productFilter(String category, double minPrice,
            double maxPrice, String brand, double minRating,
            int pageNumber, int pageSize) {

        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than 0");
        }

        List<Product> result = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        int matchCount = 0;

        for (Product product : this.productList) {

            if (category != null && !product.getCategory().equalsIgnoreCase(category)) {
                continue;
            }
            if (product.getPrice() < minPrice) {
                continue;
            }
            if (product.getPrice() > maxPrice) {
                continue;
            }
            if (brand != null && !product.getBrand().equalsIgnoreCase(brand)) {
                continue;
            }
            if (product.getRating() < minRating) {
                continue;
            }

            if (matchCount >= offset) {
                result.add(product);
                if (result.size() == pageSize) {
                    break;
                }
            }

            matchCount++;
        }

        return result;
    }

    public void addProduct(Product product) {
        if (product == null) {
            return;
        }
        if (this.productList == null) {
            this.productList = new ArrayList<>();
        }
        this.productList.add(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}
