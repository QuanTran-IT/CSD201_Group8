/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class NodeBst {
    public Product productInfo;  
    public NodeBst prev;        
    public NodeBst next;        


    public NodeBst(Product productInfo) {
        this.productInfo = productInfo;
        this.prev = null;
        this.next = null;
    }

    
    public NodeBst(Product productInfo, NodeBst prev, NodeBst next) {
        this.productInfo = productInfo;
        this.prev = prev;
        this.next = next;
    }
}