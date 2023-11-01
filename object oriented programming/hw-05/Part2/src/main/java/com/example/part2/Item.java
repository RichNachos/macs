package com.example.part2;

import java.io.PrintWriter;

public class Item {
    private String ProductId;
    private String Name;
    private String ImageFile;
    private double Price;
    public Item(String productId, String name, String imageFile, double price) {
        ProductId = productId;
        Name = name;
        ImageFile = imageFile;
        Price = price;
    }

    public String getProductId() { return ProductId; }
    public String getItemName() { return Name; }
    public String getImageFile() { return ImageFile; }
    public double getPrice() { return Price; }

    @Override
    public int hashCode() {
        return ProductId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass()))
            return false;

        Item newItem = (Item) obj;
        return newItem.getItemName().equals(getItemName());
    }
}
