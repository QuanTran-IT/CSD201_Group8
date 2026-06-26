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
    private void searchKeyword(NodeBst p, String keyword, ArrayList<Product> result) {
        if (p == null) {
            return;
        }

        // Duyệt In-order: Left -> Root -> Right
        searchKeyword(p.left, keyword, result);

        // Chuyển keyword về chữ thường để so sánh không phân biệt hoa/thường
        String lowerKey = keyword.toLowerCase();
        Product info = p.productInfo;

        // Kiểm tra xem keyword có khớp một phần với ID, Tên, Category hoặc Brand không
        boolean matchName = info.getName().toLowerCase().contains(lowerKey);
        boolean matchId = info.getId().toLowerCase().contains(lowerKey);
        boolean matchCategory = info.getCategory().toLowerCase().contains(lowerKey);
        boolean matchBrand = info.getBrand().toLowerCase().contains(lowerKey);

        if (matchName || matchId || matchCategory || matchBrand) {
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
}
