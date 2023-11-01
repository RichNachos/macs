package com.example.part2.database;

import com.example.part2.Item;
import com.mysql.cj.PreparedQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebstoreDAO {
    private static Connection conn;
    public WebstoreDAO() {
        DBConnection db = new DBConnection();
        conn = db.getConnection();
    }

    public List<Item> getAllItems() {
        String query = "SELECT * FROM products;";
        ResultSet result;
        try {

            Statement state = conn.createStatement();
            result = state.executeQuery(query);

            List<Item> items = new ArrayList<Item>();
            while (result.next()) {
                String productId = result.getString(1);
                String name = result.getString(2);
                String imageFile = result.getString(3);
                double price = Double.parseDouble(result.getString(4));
                items.add(new Item(productId, name, imageFile, price));
            }

            return items;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Item getItem(String productId) {
        String query = "SELECT * FROM products WHERE productid = ?;";
        ResultSet result;
        try {
            PreparedStatement preparedQuery = conn.prepareStatement(query);
            preparedQuery.setString(1, productId);
            result = preparedQuery.executeQuery();

            result.next();

            String name = result.getString(2);
            String imageFile = result.getString(3);
            double price = Double.parseDouble(result.getString(4));
            return new Item(productId, name, imageFile, price);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
