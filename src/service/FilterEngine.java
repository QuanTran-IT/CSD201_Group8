package service;

import java.util.ArrayList;
import java.util.List;
import model.Product;

public class FilterEngine {

    interface KeyExtractor {
        String getKey(Product p);
    }

    private List<Product> productList = new ArrayList<>();

    private ArrayList<Product> byPrice    = new ArrayList<>();
    private ArrayList<Product> byRating   = new ArrayList<>();
    private ArrayList<Product> byCategory = new ArrayList<>();
    private ArrayList<Product> byBrand    = new ArrayList<>();



    public void setProductList(List<Product> all) {
        if (all == null) return;
        productList = all;
        byPrice    = sort(all, p -> String.format("%012.2f", p.getPrice()));
        byRating   = sort(all, p -> String.format("%05.2f",  p.getRating()));
        byCategory = sort(all, p -> p.getCategory().toLowerCase());
        byBrand    = sort(all, p -> p.getBrand().toLowerCase());
    }

    public void addProduct(Product p) {
        if (p == null) return;
        productList.add(p);
        setProductList(productList);
    }

    public List<Product> getProductList() { return productList; }

   

    private ArrayList<Product> sort(List<Product> all, KeyExtractor key) {
        ArrayList<Product> list = new ArrayList<>(all);
        quickSort(list, 0, list.size() - 1, key);
        return list;
    }

    private void quickSort(ArrayList<Product> list, int lo, int hi, KeyExtractor key) {
        if (lo < hi) {
            int pi = partition(list, lo, hi, key);
            quickSort(list, lo, pi - 1, key);
            quickSort(list, pi + 1, hi, key);
        }
    }

    private int partition(ArrayList<Product> list, int lo, int hi, KeyExtractor key) {
        String pivot = key.getKey(list.get(hi));
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (key.getKey(list.get(j)).compareTo(pivot) <= 0) {
                i++;
                Product tmp = list.get(i); list.set(i, list.get(j)); list.set(j, tmp);
            }
        }
        Product tmp = list.get(i + 1); list.set(i + 1, list.get(hi)); list.set(hi, tmp);
        return i + 1;
    }

  

    private int binarySearch(ArrayList<Product> list, String target, KeyExtractor key) {
        int lo = 0, hi = list.size() - 1, result = list.size();
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (key.getKey(list.get(mid)).compareTo(target) >= 0) { result = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return result;
    }

 
    private List<Product> getRange(ArrayList<Product> list, String min, String max, KeyExtractor key) {
        List<Product> result = new ArrayList<>();
        int start = binarySearch(list, min, key);
        for (int i = start; i < list.size(); i++) {
            String k = key.getKey(list.get(i));
            if (k.compareTo(max) > 0) break;
            result.add(list.get(i));
        }
        return result;
    }



    public List<Product> filterByDouble(String field, double min, double max) {
        if (field.equals("price"))
            return getRange(byPrice,  String.format("%012.2f", min), String.format("%012.2f", max), p -> String.format("%012.2f", p.getPrice()));
        else
            return getRange(byRating, String.format("%05.2f",  min), String.format("%05.2f",  max), p -> String.format("%05.2f",  p.getRating()));
    }

    public List<Product> filterByString(String field, String value) {
        String target = value.toLowerCase();
        if (field.equals("category"))
            return getRange(byCategory, target, target, p -> p.getCategory().toLowerCase());
        else
            return getRange(byBrand,    target, target, p -> p.getBrand().toLowerCase());
    }
}