package com.pluralsight;

import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "yearup");
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM products;";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                System.out.println(resultSet.getString("ProductName"));
            }
            connection.close();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}