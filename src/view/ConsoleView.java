/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import java.util.List;
import utils.Inputter;
import model.Product;    

/**
 *
 * @author ADMIN
 */
public class ConsoleView {

    private ArrayList<String> menu;
    private String border = "==================================================================================================================";

    public ConsoleView() {

        menu = new ArrayList<>();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void printHeader(String title) {
        System.out.println("\n" + border);
        System.out.println("                                     " + title.toUpperCase() + "          ");
        System.out.println(border);
    }

    public void addItem(String item) {
        menu.add(item);
    }


    public void showMenu() {
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i));
        }
    }

    public void callMainMenu() {
        menu.clear();
        addItem("Filter Products (FilterEngine)");
        addItem("Search Products  (SearchEngine)");
        addItem("Sort Products by Criteria (ProductSorter)");
        addItem("Manage Viewed History (ViewedProductsHistory)");
        addItem("Quit program");
        printHeader("E-commerce Product Catalog Filter");
        showMenu();
        System.out.println(border);
    }

    public int getChoiceInput() {
        return Inputter.getChoice("Enter your choice:", "Just 1-> " + menu.size(), "Invalid!", 1, menu.size());
    }

   public void displayFilterMenu() {
    printHeader("Filter Products");
    System.out.println("  Enter criteria below. Press [Enter] to skip any field.");
    System.out.println(border);
}

public void displayProductList(List<Product> products, int pageNumber, int pageSize) {
    System.out.println(border);
    System.out.printf("  [Page %d]  %d result(s)%n", pageNumber, products.size());
    System.out.println(border);
    if (products.isEmpty()) {
        System.out.println("  (No products on this page)");
    } else {
        for (int i = 0; i < products.size(); i++) {
            int rowNum = (pageNumber - 1) * pageSize + i + 1;
            System.out.printf("  #%d. %s%n", rowNum, products.get(i));
        }
    }
    System.out.println(border);
}
}

