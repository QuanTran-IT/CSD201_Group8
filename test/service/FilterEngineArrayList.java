package service;

import java.util.ArrayList;
import java.util.List;
import model.Product;

/**
 * ArrayList-based Filter Engine.
 *
 * Strategy:
 *   - At load time, build 4 sorted ArrayList copies (by price, rating,
 *     category, brand) using QuickSort — O(n log n) once.
 *   - Each filter query runs a binary search to find the start index,
 *     then scans forward — O(log n + k) per query.
 *
 * Time Complexity:  setProductList = O(n log n) | filterBy* = O(log n + k)
 * Space Complexity: O(n) — 4 sorted copies held in memory
 */
public class FilterEngineArrayList {

    // ── Internal key extractor ─────────────────────────────────────────────────
    interface KeyExtractor {
        String getKey(Product p);
    }

    // ── State ──────────────────────────────────────────────────────────────────
    private List<Product>        productList = new ArrayList<>();
    private ArrayList<Product>   byPrice     = new ArrayList<>();
    private ArrayList<Product>   byRating    = new ArrayList<>();
    private ArrayList<Product>   byCategory  = new ArrayList<>();
    private ArrayList<Product>   byBrand     = new ArrayList<>();

    // ── Public API ─────────────────────────────────────────────────────────────

    /**
     * Loads the full product list and builds all four sorted indexes.
     * Must be called before any filter operation.
     */
    public void setProductList(List<Product> all) {
        if (all == null) return;
        productList = all;
        byPrice    = sort(all, p -> String.format("%012.2f", p.getPrice()));
        byRating   = sort(all, p -> String.format("%05.2f",  p.getRating()));
        byCategory = sort(all, p -> p.getCategory().toLowerCase());
        byBrand    = sort(all, p -> p.getBrand().toLowerCase());
    }

    /**
     * Adds a single product and rebuilds all sorted indexes.
     */
    public void addProduct(Product p) {
        if (p == null) return;
        productList.add(p);
        setProductList(productList);
    }

    public List<Product> getProductList() {
        return productList;
    }

    /**
     * Filters by a numeric field ("price" or "rating") within [min, max].
     */
    public List<Product> filterByDouble(String field, double min, double max) {
        if (field.equals("price")) {
            return getRange(byPrice,
                    String.format("%012.2f", min),
                    String.format("%012.2f", max),
                    p -> String.format("%012.2f", p.getPrice()));
        } else {
            return getRange(byRating,
                    String.format("%05.2f", min),
                    String.format("%05.2f", max),
                    p -> String.format("%05.2f", p.getRating()));
        }
    }

    /**
     * Filters by an exact string field ("category" or "brand").
     */
    public List<Product> filterByString(String field, String value) {
        String target = value.toLowerCase();
        if (field.equals("category")) {
            return getRange(byCategory, target, target,
                    p -> p.getCategory().toLowerCase());
        } else {
            return getRange(byBrand, target, target,
                    p -> p.getBrand().toLowerCase());
        }
    }

    // ── QuickSort (recursive) ──────────────────────────────────────────────────

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
                Product tmp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, tmp);
            }
        }
        Product tmp = list.get(i + 1);
        list.set(i + 1, list.get(hi));
        list.set(hi, tmp);
        return i + 1;
    }

    // ── Binary Search + Range Scan ─────────────────────────────────────────────

    /** Returns the leftmost index whose key >= target, or list.size() if none. */
    private int binarySearch(ArrayList<Product> list, String target, KeyExtractor key) {
        int lo = 0, hi = list.size() - 1, result = list.size();
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (key.getKey(list.get(mid)).compareTo(target) >= 0) {
                result = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return result;
    }

    /** Scans forward from the binary-search start index, collecting [min, max]. */
    private List<Product> getRange(ArrayList<Product> list,
                                   String min, String max, KeyExtractor key) {
        List<Product> result = new ArrayList<>();
        int start = binarySearch(list, min, key);
        for (int i = start; i < list.size(); i++) {
            String k = key.getKey(list.get(i));
            if (k.compareTo(max) > 0) break;
            result.add(list.get(i));
        }
        return result;
    }
}
