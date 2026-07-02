/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author ADMIN
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.Product;

public class ViewedProductsHistory {
    private Node head;
    private HashMap<String, Node> productMap;

    public ViewedProductsHistory() {
        this.head = null;
        this.productMap = new HashMap<>();
    }

    public ProductsIterator iterator() {
        return new ProductsIterator(this.head);
    }

    /**
     * Xuất ra danh sách theo đúng thứ tự hiện tại (mới xem nhất -> cũ nhất).
     * Node là inner class không Serializable nên không thể ghi thẳng object
     * này xuống file; phải "phẳng hóa" ra List<Product> trước khi lưu.
     */
    public List<Product> toOrderedList() {
        List<Product> result = new ArrayList<>();
        Iterator<Product> it = iterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    /**
     * Nạp lại history từ 1 danh sách đã lưu (thứ tự: mới xem nhất -> cũ nhất).
     * Duyệt ngược rồi addViewedProduct từng phần tử để phần tử đầu danh sách
     * (mới nhất) được add sau cùng, đảm bảo nó nằm ở head sau khi restore.
     */
    public void restoreFromList(List<Product> orderedList) {
        clear();
        if (orderedList == null) {
            return;
        }
        for (int i = orderedList.size() - 1; i >= 0; i--) {
            addViewedProduct(orderedList.get(i));
        }
    }

    /**
     * Method to check if history is empty
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Method to clear history
     */
    public void clear() {
        this.head = null;
        this.productMap.clear();
    }

    /**
     * Method to add a viewed product to the most recent
     */
    public void addViewedProduct(Product product) {
        String productID = product.getId();
        Node node = this.productMap.get(productID);

        if (node != null) { // Product has already been viewed before
            if (node != head) { // Mitigate operations if the viewed product is already the most recent
                // Temporarily delete the Node from the Linked List
                this.removeNode(node);

                node.prev = null;
                node.next = head;
            }
        } else { // Product has not been viewed until now
            // Instantiate new Node for the Linked List and insert a new entry into the Hash Map
            node = new Node(product, null, this.head);
            this.productMap.put(productID, node);
        }

        if (node != head) {
            // Set the Node as the head of the Linked List
            if (!this.isEmpty())
                this.head.prev = node;

            this.head = node;
        }
    }

    /**
     * Method to remove a viewed product
     */
    public void removeViewedProduct(Product product) {
        String productID = product.getId();
        this.removeViewedProduct(productID);
    }

    /**
     * Method to remove a viewed product by ID
     */
    public void removeViewedProduct(String productID) {
        Node node = this.productMap.get(productID);

        if (node != null) {
            this.removeNode(node);
            this.productMap.remove(productID);
        }
    }

    /**
     * Private method to remove a node in the Linked List
     */
    private void removeNode(Node node) {
        if (node.prev != null) { // The Node is not the most recent
            node.prev.next = node.next;
        } else { // The Node is the most recent
            this.head = node.next;
        }

        if (node.next != null) { // The Node is not the least recent
            node.next.prev = node.prev;
        }
    }

    /**
     * Internal Node used in the Linked List
     */
    private static class Node {
        Product product;
        Node prev;
        Node next;

        Node(Product product, Node prev, Node next) {
            this.product = product;
            this.prev = prev;
            this.next = next;
        }
    }

    private class ProductsIterator implements Iterator<Product> {
        private Node current;

        ProductsIterator(Node head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            boolean result = false;

            if (this.current != null) {
                result = true;
            }

            return result;
        }

        @Override
        public Product next() {
            Product result = this.current.product;

            this.current = this.current.next;

            return result;
        }
    }
}