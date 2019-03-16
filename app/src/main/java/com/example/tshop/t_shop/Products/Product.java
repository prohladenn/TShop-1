package com.example.tshop.t_shop.Products;


import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable, Comparable<Product> {

    private String name;
    private String description;
    private String pictureSource;
    private String pictureSubnail;
    private int priceAmount;
    private String priceCurrency;
    private String valueAmount;
    private String valueUnits;
    private String id;
    private Long count;
    private int selected;

    public Product(String name, String description,
                   String pictureSource, String pictureSubnail,
                   int priceAmount, String priceCurrency,
                   String valueAmount, String valueUnits, Long count, int selected, String id) {
        this.name = name;
        this.description = description;
        this.pictureSource = pictureSource;
        this.pictureSubnail = pictureSubnail;
        this.priceAmount = priceAmount;
        this.priceCurrency = priceCurrency;
        this.valueAmount = valueAmount;
        this.valueUnits = valueUnits;
        this.count = count;
        this.selected = selected;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureSource() {
        return pictureSource;
    }

    public void setPictureSource(String pictureSource) {
        this.pictureSource = pictureSource;
    }

    public String getPictureSubnail() {
        return pictureSubnail;
    }

    public void setPictureSubnail(String pictureSubnail) {
        this.pictureSubnail = pictureSubnail;
    }

    public int getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(int priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getValueAmount() {
        return valueAmount;
    }

    public void setValueAmount(String valueAmount) {
        this.valueAmount = valueAmount;
    }

    public String getValueUnits() {
        return valueUnits;
    }

    public void setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(Product o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}