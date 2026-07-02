/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.NodeBst;
import model.Product;

/**
 *
 * @author ADMIN
 */
public class SearchEngine {

    // Khai báo root cho Binary Search Tree
    private NodeBst root;

    public SearchEngine() {
        this.root = null;
    }

    // Insert Product into BST
    public void insert(Product x) {
        NodeBst p = new NodeBst(x);
        if (root == null) {
            root = p;
            return;
        }

        NodeBst current = root;
        NodeBst parent = null;

        while (current != null) {
            parent = current;
            // So sánh theo ID của Product
            if (x.getId().compareToIgnoreCase(current.productInfo.getId()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (x.getId().compareToIgnoreCase(parent.productInfo.getId()) < 0) {
            parent.left = p;
        } else {
            parent.right = p;
        }
    }

    //Search Product by ID
    public Product searchById(String id) {
        NodeBst current = root;

        while (current != null) {
            int compare = id.compareToIgnoreCase(current.productInfo.getId());

            if (compare == 0) {
                return current.productInfo;
            }

            if (compare < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null; // Không tìm thấy
    }

    // Search Product by Keyword (Helper method)
    // Chỉ match trên ID/Name (định danh) - category/brand là việc của FilterEngine
    // để tránh 2 tính năng Search vs Filter chồng chéo chức năng nhau.
    private void searchKeyword(NodeBst p, String keyword, ArrayList<Product> result) {
        if (p == null) {
            return;
        }

        // Duyệt In-order: Left -> Root -> Right
        searchKeyword(p.left, keyword, result);

        // Chuyển keyword về chữ thường để so sánh không phân biệt hoa/thường
        String lowerKey = keyword.toLowerCase();
        Product info = p.productInfo;

        // Kiểm tra xem keyword có khớp một phần với ID hoặc Tên không
        boolean matchName = info.getName().toLowerCase().contains(lowerKey);
        boolean matchId = info.getId().toLowerCase().contains(lowerKey);

        if (matchName || matchId) {
            result.add(info);
        }

        searchKeyword(p.right, keyword, result);
    }

    // Search Product by Keyword (Main method)
    public ArrayList<Product> searchByKeyword(String keyword) {
        ArrayList<Product> result = new ArrayList<>();
        searchKeyword(root, keyword, result);
        return result;
    }

    // Search Product by Keyword - bản có phân trang, tái dùng PageResult<T> của FilterEngine
    // để đồng bộ cách phân trang giữa Search và Filter.
    public service.FilterEngine.PageResult<Product> searchByKeywordPaged(String keyword, int page, int pageSize) {
        ArrayList<Product> all = searchByKeyword(keyword);

        int total = all.size();
        if (pageSize <= 0) {
            return new service.FilterEngine.PageResult<>(new ArrayList<>(), page, pageSize, total);
        }
        if (page < 1) {
            page = 1;
        }
        int from = (page - 1) * pageSize;
        if (from >= total) {
            return new service.FilterEngine.PageResult<>(new ArrayList<>(), page, pageSize, total);
        }
        int to = Math.min(from + pageSize, total);
        return new service.FilterEngine.PageResult<>(new ArrayList<>(all.subList(from, to)), page, pageSize, total);
    }

    //add data for engine
    public void setProductList(java.util.List<Product> all) {
        if (all == null) return;
        root = null; // reset cây cũ
        for (Product p : all) {
            insert(p);
        }
    }
}