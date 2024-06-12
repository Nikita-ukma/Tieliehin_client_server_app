package Store;

import FGenerator.FakeMessageGenerator;
import models.Product;
import java.util.List;
import java.util.ArrayList;

public class Store {


    private final List<Product> products;
    public List<Product> getProducts() {
        return products;
    }

    public List<String> getProductGroups() {
        return productGroups;
    }

    private final List<String> productGroups;

    public Store() {
        this.products = new ArrayList<>();
        this.productGroups = new ArrayList<>();
        FakeMessageGenerator.addProducts(products);
    }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Im here");
    }

    public boolean hasProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) return true;
        }
    return false;
}

    public Product getProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) return product;
        }
    return null;
    }

    public void addProductGroup(String group) {
        productGroups.add(group);
    }
    public void addProductToGroup(Product product, String group) {
        product.setGroup(group);
    }
    public void listProductsByGroup(String group) {
        for (Product product : products) {
            if (product.getGroup().equals(group)) {
                System.out.println(product.getName());
            }
        }
    }
    public void printAllProducts() {
        for (Product product : products) {
            System.out.println(product.getName() + " " + product.getQuantity()+ " " +product.getGroup()+ " " +product.getPrice());
        }
    }
}