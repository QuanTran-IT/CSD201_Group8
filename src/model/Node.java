/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Node {
    public Product product;  // Chứa dữ liệu của sản phẩm
    public Node prev;        // Con trỏ đến Node phía trước
    public Node next;        // Con trỏ đến Node phía sau

    // Constructor mặc định cho Node
    public Node(Product product) {
        this.product = product;
        this.prev = null;
        this.next = null;
    }

    
    public Node(Product product, Node prev, Node next) {
        this.product = product;
        this.prev = prev;
        this.next = next;
    }
}