package entity;

import java.util.Objects;

public class ProductGroup {
    private int id;
    private String name;
    private String description;


    public ProductGroup(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setNumber(int number) { this.id = number; }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGroup productGroup = (ProductGroup) o;
        return id == productGroup.id && Objects.equals(name, productGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Product group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
