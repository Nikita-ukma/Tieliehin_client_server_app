package entity;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private String description;
    private ProductGroup productGroup;
    private double price;
    private int amount;
    private String manufacturer;

    public Product(int id, String name, String description, String manufacturer, int amount, double price, ProductGroup productGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.amount = amount;
        this.productGroup = productGroup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(productGroup, product.productGroup) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, productGroup, description);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productGroup=" + productGroup +
                ", priсe=" + price +
                ", amount=" + amount +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }

    public void setAmount(int i) {
        amount = i;
    }
}
