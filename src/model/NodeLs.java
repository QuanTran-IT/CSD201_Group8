/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */

    public class NodeLs {
    private Product productInfo;
    private NodeLs prev;
    private NodeLs next;

  
    public NodeLs(Product productInfo) {
        this.productInfo = productInfo;
        this.prev = null;
        this.next = null;
    }

    public NodeLs(Product productInfo, NodeLs prev, NodeLs next) {
        this.productInfo = productInfo;
        this.prev = prev;
        this.next = next;
    }
    
}

