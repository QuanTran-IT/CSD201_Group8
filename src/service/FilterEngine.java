package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import model.Product;

public class FilterEngine {

    /** Kết quả phân trang.
     */
    public static class PageResult<T> {
        public final List<T> items;
        public final int page, pageSize, totalItems, totalPages;

        public PageResult(List<T> items, int page, int pageSize, int totalItems) {
            this.items = items;
            this.page = page;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = pageSize <= 0 ? 0 : (int) Math.ceil((double) totalItems / pageSize);
        }
    }

    private List<Product> productList = new ArrayList<>();
    private ArrayList<Product> byPrice    = new ArrayList<>();
    private ArrayList<Product> byRating   = new ArrayList<>();
    private ArrayList<Product> byCategory = new ArrayList<>();
    private ArrayList<Product> byBrand    = new ArrayList<>();

    private static final Comparator<Product> CMP_PRICE    = (a, b) -> Double.compare(a.getPrice(), b.getPrice());
    private static final Comparator<Product> CMP_RATING   = (a, b) -> Double.compare(a.getRating(), b.getRating());
    private static final Comparator<Product> CMP_CATEGORY = (a, b) -> a.getCategory().compareToIgnoreCase(b.getCategory());
    private static final Comparator<Product> CMP_BRAND    = (a, b) -> a.getBrand().compareToIgnoreCase(b.getBrand());

    public void setProductList(List<Product> all) {
        if (all == null) return;
        productList = all;
        byPrice    = sort(all, CMP_PRICE);
        byRating   = sort(all, CMP_RATING);
        byCategory = sort(all, CMP_CATEGORY);
        byBrand    = sort(all, CMP_BRAND);
    }

    public List<Product> getProductList() { return productList; }

    // ===================== QUICKSORT (pivot median-of-three) =====================

    private ArrayList<Product> sort(List<Product> all, Comparator<Product> cmp) {
        ArrayList<Product> list = new ArrayList<>(all);
        quickSort(list, 0, list.size() - 1, cmp);
        return list;
    }

    private void quickSort(ArrayList<Product> list, int lo, int hi, Comparator<Product> cmp) {
        if (lo >= hi) return;
        medianOfThree(list, lo, hi, cmp); // đưa pivot tốt nhất về vị trí hi
        Product pivot = list.get(hi);
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (cmp.compare(list.get(j), pivot) <= 0) swap(list, ++i, j);
        }
        swap(list, i + 1, hi);
        quickSort(list, lo, i, cmp);
        quickSort(list, i + 2, hi, cmp);
    }

    /** Sắp 3 phần tử lo/mid/hi rồi đưa trung vị (mid) về cuối đoạn làm pivot. */
    private void medianOfThree(ArrayList<Product> list, int lo, int hi, Comparator<Product> cmp) {
        int mid = (lo + hi) / 2;
        if (cmp.compare(list.get(lo), list.get(mid)) > 0) swap(list, lo, mid);
        if (cmp.compare(list.get(lo), list.get(hi)) > 0) swap(list, lo, hi);
        if (cmp.compare(list.get(mid), list.get(hi)) > 0) swap(list, mid, hi);
        swap(list, mid, hi);
    }

    private void swap(ArrayList<Product> list, int i, int j) {
        Product tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }

    // ===================== BINARY SEARCH =====================

    private List<Product> rangeByDouble(ArrayList<Product> list, double min, double max, ToDoubleFunction<Product> key) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (key.applyAsDouble(list.get(mid)) < min) lo = mid + 1; else hi = mid;
        }
        List<Product> result = new ArrayList<>();
        for (int i = lo; i < list.size() && key.applyAsDouble(list.get(i)) <= max; i++) result.add(list.get(i));
        return result;
    }

    private List<Product> rangeByString(ArrayList<Product> list, String target, Function<Product, String> key) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (key.apply(list.get(mid)).compareToIgnoreCase(target) < 0) lo = mid + 1; else hi = mid;
        }
        List<Product> result = new ArrayList<>();
        for (int i = lo; i < list.size() && key.apply(list.get(i)).equalsIgnoreCase(target); i++) result.add(list.get(i));
        return result;
    }

    public List<Product> filterByDouble(String field, double min, double max) {
        return field.equals("price")
                ? rangeByDouble(byPrice, min, max, Product::getPrice)
                : rangeByDouble(byRating, min, max, Product::getRating);
    }

    public List<Product> filterByString(String field, String value) {
        return field.equals("category")
                ? rangeByString(byCategory, value, Product::getCategory)
                : rangeByString(byBrand, value, Product::getBrand);
    }

    // ===================== PHÂN TRANG =====================

    private <T> PageResult<T> paginate(List<T> source, int page, int pageSize) {
        int total = source.size();
        if (pageSize <= 0) return new PageResult<>(new ArrayList<>(), page, pageSize, total);
        if (page < 1) page = 1;
        int from = (page - 1) * pageSize;
        if (from >= total) return new PageResult<>(new ArrayList<>(), page, pageSize, total);
        return new PageResult<>(new ArrayList<>(source.subList(from, Math.min(from + pageSize, total))), page, pageSize, total);
    }

    public PageResult<Product> getProductListPaged(int page, int pageSize) {
        return paginate(productList, page, pageSize);
    }

    public PageResult<Product> filterByDoublePaged(String field, double min, double max, int page, int pageSize) {
        return paginate(filterByDouble(field, min, max), page, pageSize);
    }

    public PageResult<Product> filterByStringPaged(String field, String value, int page, int pageSize) {
        return paginate(filterByString(field, value), page, pageSize);
    }
}
