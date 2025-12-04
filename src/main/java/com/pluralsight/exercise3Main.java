package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class exercise3Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        displayScreen();

        String choice = scanner.nextLine();
        switch (choice){
            case "2": displayAllCustomers(args);
                break;
            case "3": displayAllCategories(args);
                displayAllProductsByCategoryID(args);
                break;
            default:
                System.out.println("Exiting! Thank you!");
        }
    }
    public static void displayScreen(){
        String options = """
                What do you want to do?\s
                2) Display all Customers\s
                3) Display all categories\s
                0) Exit\s
                Select an option: \s
                """;
        System.out.println(options);
    }
    public static String getUserInput(String text){
        System.out.println(text);
        return scanner.nextLine();
    }

    public static void displayAllCustomers(String[] args){
        String sql = "SELECT * FROM customers";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", args[0], args[1]);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf("Customer name: %s | Company name: %s | City: %s | Country: %s | Phone number: %s\n",resultSet.getString("ContactName"), resultSet.getString("CompanyName"), resultSet.getString("City"), resultSet.getString("Country"), resultSet.getString("Phone"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void displayAllCategories(String[] args){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", args[0], args[1]);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories ORDER BY CategoryID");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                System.out.printf("Category ID: %s | Category Name: %s ", resultSet.getString("CategoryID"), resultSet.getString("CategoryName"));
            }
            connection.close();


        }catch (SQLException e){
            throw new RuntimeException();
        }

    }
    public static void displayAllProductsByCategoryID(String[] args){
        int catID = Integer.parseInt(getUserInput("What category do you want to see? Enter the ID: "));
        String sql = "SELECT * FROM products WHERE CategoryID = ? ";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", args[0], args[1]);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,catID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.printf("Product ID: %s | Product name: %s | Unit Price: %.2f | Units in stock: %d\n", resultSet.getString("ProductId"), resultSet.getString("ProductName"), resultSet.getFloat("UnitPrice"), resultSet.getInt("UnitsInStock"));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }
}
