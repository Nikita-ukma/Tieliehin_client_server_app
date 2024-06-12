package models;

public class Product {
    private final String name;
    private String group;
    private int quantity;
    private double price;

    public Product(String name, int quantity, double price) {
        this.name = name;
        this.group = null;
        this.quantity = quantity;
        this.price = price;
    }
    public Product(String name) {
        this.name = name;
        this.group = null;
        this.quantity = 0;
        this.price = 0;
    }

    public Product(String name, int quantity) {
        this.name = name;
        this.group = null;
        this.quantity = quantity;
        this.price = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getQuantity() {
        return quantity;
    }

    public void removeQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Insufficient quantity on hand.");
        }
        quantity -= amount;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String print() {
        return "models.Product{name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}