/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import java.util.Stack;
import java.util.ArrayList;
import model.Product;

/**
 * Handles dynamic product sorting using Iterative Quick Sort without recursion.
 */
public class ProductSorter {

    public void sortProducts(ArrayList<Product> productList, Comparator<Product> comparator) {
        int n = productList.size();
        if (n <= 1) return;

        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        stack.push(n - 1);

        while (!stack.isEmpty()) {
            int high = stack.pop();
            int low = stack.pop();

            int pivotIndex = partition(productList, low, high, comparator);

            if (pivotIndex - 1 > low) {
                stack.push(low);
                stack.push(pivotIndex - 1);
            }
            if (pivotIndex + 1 < high) {
                stack.push(pivotIndex + 1);
                stack.push(high);
            }
        }
    }
    private int partition(ArrayList<Product> productList, int low, int high, Comparator<Product> comparator) {
        Product pivot = productList.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Compare elements based on the injected Comparator logic
            if (comparator.compare(productList.get(j), pivot) <= 0) {
                i++;
                Product temp = productList.get(i);
                productList.set(i, productList.get(j));
                productList.set(j, temp);
            }
        }

        Product temp = productList.get(i + 1);
        productList.set(i + 1, productList.get(high));
        productList.set(high, temp);
        return i + 1;
    }
}
