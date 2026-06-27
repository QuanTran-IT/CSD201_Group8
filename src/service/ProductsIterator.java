package service;

import model.Product;

public interface ProductsIterator {
    boolean hasNext();
    Product next();
}
