/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import model.Product;

/**
 *
 * @author ADMIN
 */
public class FileUtils {
    private static final String FILE_PATH = "data/products_data.dat"; // File nhị phân
    private static final String HISTORY_FILE_PATH = "data/history_data.dat"; // File nhị phân cho Viewed History

    // ===================== GENERIC SAVE / LOAD (GZIP + Object Serialization) =====================
    // saveProducts/loadProducts và saveViewedHistory/loadViewedHistory đều làm đúng 1 việc:
    // ghi/đọc 1 List<Serializable> xuống file, nén GZIP. Gộp logic đó vào đây, chỉ khác nhau
    // ở filePath + label (dùng để log) truyền vào.

    private static <T extends Serializable> boolean saveList(String filePath, List<T> data, String label) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             GZIPOutputStream gos = new GZIPOutputStream(fos); // Ép nén GZIP
             ObjectOutputStream oos = new ObjectOutputStream(gos)) {

            oos.writeObject(data);
            System.out.println("[DataManager] Saved and Zipped " + label + " thành công vào " + filePath);
            return true;

        } catch (IOException e) {
            System.out.println("[DataManager] Saved error " + label + ": " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> List<T> loadList(String filePath, String label) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[DataManager] File " + label + " not exist, returning null.");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gis = new GZIPInputStream(fis); // Giải nén GZIP
             ObjectInputStream ois = new ObjectInputStream(gis)) {

            List<T> loaded = (List<T>) ois.readObject();
            System.out.println("[DataManager] Load successfully " + loaded.size() + " " + label + ".");
            return loaded;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[DataManager] Error while reading " + label + ": " + e.getMessage());
            return null;
        }
    }

    // ===================== PRODUCTS =====================

    public static boolean saveProducts(List<Product> productList) {
        return saveList(FILE_PATH, productList, "Product List");
    }

    public static List<Product> loadProducts() {
        return loadList(FILE_PATH, "Product");
    }

    // ===================== VIEWED HISTORY =====================
    // Thứ tự trong list: mới xem nhất -> cũ nhất

    public static boolean saveViewedHistory(List<Product> orderedHistory) {
        return saveList(HISTORY_FILE_PATH, orderedHistory, "viewed history");
    }

    public static List<Product> loadViewedHistory() {
        return loadList(HISTORY_FILE_PATH, "viewed history entries");
    }
}