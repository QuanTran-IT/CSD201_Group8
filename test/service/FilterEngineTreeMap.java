package service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import model.Product;

/**
 * TreeMap-based Filter Engine.
 *
 * Strategy:
 *   - At load time, insert every product into four TreeMaps keyed by a
 *     formatted string (same format as the ArrayList version so results
 *     are identical).  Each TreeMap value is a list of products sharing
 *     that key (to handle duplicates, e.g. two products with price=99.99).
 *   - Filter queries use TreeMap.subMap() — a pure O(log n + k) operation
 *     with no prior full-sort needed.
 *
 * Time Complexity:  setProductList = O(n log n) | filterBy* = O(log n + k)
 * Space Complexity: O(n) — four TreeMaps, each holding n entries total
 *
 * Key difference vs. ArrayList version:
 *   - No explicit QuickSort pass at load time — TreeMap maintains order
 *     automatically via its Red-Black Tree during insertion.
 *   - subMap() navigates the tree in O(log n) directly to the range
 *     boundary, whereas ArrayList needs a binary search helper first.
 */
public class FilterEngineTreeMap {

    // ── State ──────────────────────────────────────────────────────────────────
    private List<Product>                       productList  = new ArrayList<>();
    private TreeMap<String, List<Product>>      byPrice      = new TreeMap<>();
    private TreeMap<String, List<Product>>      byRating     = new TreeMap<>();
    private TreeMap<String, List<Product>>      byCategory   = new TreeMap<>();
    private TreeMap<String, List<Product>>      byBrand      = new TreeMap<>();

    // ── Public API ─────────────────────────────────────────────────────────────

    /**
     * Loads the full product list and populates all four TreeMap indexes.
     * Must be called before any filter operation.
     */
    public void setProductList(List<Product> all) {
        if (all == null) return;
        productList = all;

        // Clear old indexes
        byPrice.clear();
        byRating.clear();
        byCategory.clear();
        byBrand.clear();

        for (Product p : all) {
            insert(byPrice,    String.format("%012.2f", p.getPrice()),    p);
            insert(byRating,   String.format("%05.2f",  p.getRating()),   p);
            insert(byCategory, p.getCategory().toLowerCase(),             p);
            insert(byBrand,    p.getBrand().toLowerCase(),                p);
        }
    }

    /**
     * Adds a single product and inserts it into all four indexes.
     * More efficient than ArrayList version — no full rebuild needed.
     */
    public void addProduct(Product p) {
        if (p == null) return;
        productList.add(p);
        insert(byPrice,    String.format("%012.2f", p.getPrice()),    p);
        insert(byRating,   String.format("%05.2f",  p.getRating()),   p);
        insert(byCategory, p.getCategory().toLowerCase(),             p);
        insert(byBrand,    p.getBrand().toLowerCase(),                p);
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
                    String.format("%012.2f", max));
        } else {
            return getRange(byRating,
                    String.format("%05.2f", min),
                    String.format("%05.2f", max));
        }
    }

    /**
     * Filters by an exact string field ("category" or "brand").
     */
    public List<Product> filterByString(String field, String value) {
        String target = value.toLowerCase();
        List<Product> result = new ArrayList<>();
        if (field.equals("category")) {
            List<Product> bucket = byCategory.get(target);
            if (bucket != null) result.addAll(bucket);
        } else {
            List<Product> bucket = byBrand.get(target);
            if (bucket != null) result.addAll(bucket);
        }
        return result;
    }

    // ── Internal helpers ───────────────────────────────────────────────────────

    /** Inserts a product into a TreeMap bucket list. */
    private void insert(TreeMap<String, List<Product>> map, String key, Product p) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
    }

    /**
     * Collects all products whose key falls within [minKey, maxKey] (inclusive).
     * TreeMap.subMap() returns a view of the map for the given key range,
     * giving O(log n) navigation to the boundary.
     */
    private List<Product> getRange(TreeMap<String, List<Product>> map,
                                   String minKey, String maxKey) {
        List<Product> result = new ArrayList<>();
        // subMap(fromKey, inclusive=true, toKey, inclusive=true)
        for (List<Product> bucket : map.subMap(minKey, true, maxKey, true).values()) {
            result.addAll(bucket);
        }
        return result;
    }
}
