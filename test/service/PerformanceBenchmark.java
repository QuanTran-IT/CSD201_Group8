package service;

import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PerformanceBenchmark
 *
 * Empirically compares FilterEngineArrayList vs FilterEngineTreeMap
 * for price-range filtering across six dataset sizes.
 *
 * Usage  : Run main() — results are printed to stdout in a plain table.
 * Metric : Average execution time (ms) over 5 trials per input size.
 *
 * Input sizes tested: 100, 500, 1000, 5000, 10000, 50000
 */
public class PerformanceBenchmark {

    // ── Configuration ──────────────────────────────────────────────────────────
    private static final int[]   INPUT_SIZES = {100, 500, 1000, 5000, 10000, 50000};
    private static final int     TRIALS      = 5;
    private static final double  MIN_PRICE   = 500.0;
    private static final double  MAX_PRICE   = 1500.0;
    private static final Random  RNG         = new Random(42); // fixed seed = reproducible

    // ── Entry point ────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("  Performance Benchmark: ArrayList vs TreeMap (Price Filter)");

        System.out.printf("%-18s %-22s %-22s%n",
                "Input Size (n)", "ArrayList Avg (ms)", "TreeMap Avg (ms)");
      

        for (int n : INPUT_SIZES) {
            List<Product> products = generateProducts(n);

            double arrayListAvg = benchmarkArrayList(products);
            double treeMapAvg   = benchmarkTreeMap(products);

            System.out.printf("%-18d %-22.4f %-22.4f%n", n, arrayListAvg, treeMapAvg);
        }


        System.out.println("Note: Each value is the average of " + TRIALS + " trials.");
        System.out.println("Query: filterByDouble(\"price\", 500.0, 1500.0)");
    }

    // ── Benchmark helpers ──────────────────────────────────────────────────────

    /**
     * Benchmarks FilterEngineArrayList.
     * Measures ONLY the filterByDouble() call (not the load/sort phase).
     */
    private static double benchmarkArrayList(List<Product> products) {
        FilterEngineArrayList engine = new FilterEngineArrayList();
        engine.setProductList(new ArrayList<>(products)); // load phase (not timed)

        long total = 0;
        for (int t = 0; t < TRIALS; t++) {
            long start = System.nanoTime();
            engine.filterByDouble("price", MIN_PRICE, MAX_PRICE);
            long end   = System.nanoTime();
            total += (end - start);
        }
        return (total / (double) TRIALS) / 1_000_000.0; // ns → ms
    }

    /**
     * Benchmarks FilterEngineTreeMap.
     * Measures ONLY the filterByDouble() call (not the load/insert phase).
     */
    private static double benchmarkTreeMap(List<Product> products) {
        FilterEngineTreeMap engine = new FilterEngineTreeMap();
        engine.setProductList(new ArrayList<>(products)); // load phase (not timed)

        long total = 0;
        for (int t = 0; t < TRIALS; t++) {
            long start = System.nanoTime();
            engine.filterByDouble("price", MIN_PRICE, MAX_PRICE);
            long end   = System.nanoTime();
            total += (end - start);
        }
        return (total / (double) TRIALS) / 1_000_000.0; // ns → ms
    }

    // ── Data generation ────────────────────────────────────────────────────────

    /**
     * Generates n random Product objects with prices in [100, 5000].
     */
    private static List<Product> generateProducts(int n) {
        String[] categories = {"Laptop", "Phone", "Tablet", "TV", "Camera"};
        String[] brands     = {"Apple", "Samsung", "Sony", "Dell", "LG", "Asus"};

        List<Product> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String id       = String.format("P%05d", i + 1);
            String name     = "Product_" + id;
            String category = categories[RNG.nextInt(categories.length)];
            double price    = 100.0 + RNG.nextDouble() * 4900.0; // [100, 5000]
            String brand    = brands[RNG.nextInt(brands.length)];
            double rating   = 1.0  + RNG.nextDouble() * 4.0;     // [1.0, 5.0]
            int    views    = RNG.nextInt(10000);
            list.add(new Product(id, name, category, price, brand, rating, views));
        }
        return list;
    }
}
