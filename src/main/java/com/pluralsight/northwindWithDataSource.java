package com.pluralsight;

import java.sql.*;
import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;

public class northwindWithDataSource {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        if (args.length < 2){
            System.out.println("Usage: java com.pluralsight.exercise3Main <username> <password>");
            return;
        }
        String username = args[0];
        String password = args[1];

        DataSource dataSource = createDataSource(username, password);
        displayScreen();

        String choice = scanner.nextLine();
        switch (choice){
            case "2": displayAllCustomers(dataSource);
                break;
            case "3": displayAllCategories(dataSource);
                displayAllProductsByCategoryID(dataSource);
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
    private static DataSource createDataSource(String username, String password){
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setUrl("jdbc:mysql://localhost:3306/northwind");

        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
    public static String getUserInput(String text){
        System.out.println(text);
        return scanner.nextLine();
    }

    public static void displayAllCustomers(DataSource dataSource){
        String sql = "SELECT * FROM customers";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf("Customer name: %s | Company name: %s | City: %s | Country: %s | Phone number: %s\n",resultSet.getString("ContactName"), resultSet.getString("CompanyName"), resultSet.getString("City"), resultSet.getString("Country"), resultSet.getString("Phone"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void displayAllCategories(DataSource dataSource){
        try {
            Connection connection = dataSource.getConnection();
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
    public static void displayAllProductsByCategoryID(DataSource dataSource){
        int catID = Integer.parseInt(getUserInput("What category do you want to see? Enter the ID: "));
        String sql = "SELECT * FROM products WHERE CategoryID = ? ";
        try {
            Connection connection = dataSource.getConnection();
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
