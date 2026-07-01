/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author ADMIN
 */

import java.util.HashMap;
import java.util.Iterator;
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
