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

    // HÀM SAVE: Ghi và Nén dữ liệu xuống file
    public static boolean saveProducts(List<Product> productList) {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             GZIPOutputStream gos = new GZIPOutputStream(fos); // Ép nén GZIP
             ObjectOutputStream oos = new ObjectOutputStream(gos)) {
             
            oos.writeObject(productList);
            System.out.println("[DataManager] Đã lưu và nén danh sách thành công vào " + FILE_PATH);
            return true;
            
        } catch (IOException e) {
            System.out.println("[DataManager] Lỗi khi lưu file: " + e.getMessage());
            return false;
        }
    }

    // HÀM LOAD: Giải nén và Đọc dữ liệu từ file lên List
    @SuppressWarnings("unchecked")
    public static List<Product> loadProducts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("[DataManager] File dữ liệu chưa tồn tại, sẽ trả về null.");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gis = new GZIPInputStream(fis); // Giải nén GZIP
             ObjectInputStream ois = new ObjectInputStream(gis)) {
             
            List<Product> loadedList = (List<Product>) ois.readObject();
            System.out.println("[DataManager] Đã tải thành công " + loadedList.size() + " sản phẩm.");
            return loadedList;
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[DataManager] Lỗi khi đọc file: " + e.getMessage());
            return null;
        }
    }
}
