package com.examly.springapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringappApplication.class, args);

        String jdbcUrl = "jdbc:mysql://localhost:3306/HotelManagement?createDatabaseIfNotExist=true";
        String username = "root"; // Replace with your DB username
        String password = "examly"; // Replace with your DB password

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database");

                // Insert records
                insertHotel(connection, 101, "Hotel A", "City A", 4.5, true, true, false);
                insertHotel(connection, 102, "Hotel B", "City B", 3.5, false, true, true);

                // Display records
                String output = displayHotels(connection);
                System.out.println(output);
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertHotel(Connection connection, int id, String hotelName, String location, double rating,
                            boolean hasPool, boolean hasGym, boolean hasSpa) throws SQLException {
        String insertQuery = "INSERT INTO hotels (id, hotelName, location, rating, pool, gym, spa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, hotelName);
            preparedStatement.setString(3, location);
            preparedStatement.setDouble(4, rating);
            preparedStatement.setBoolean(5, hasPool);
            preparedStatement.setBoolean(6, hasGym);
            preparedStatement.setBoolean(7, hasSpa);
            preparedStatement.executeUpdate();
        }
    }

    static String displayHotels(Connection connection) {
        StringBuilder output = new StringBuilder();

        try {
            if (connection != null) {
                System.out.println("Connected to the database");

                String query = "SELECT * FROM hotels";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String hotelName = resultSet.getString("hotelName");
                            String location = resultSet.getString("location");
                            double rating = resultSet.getDouble("rating");
                            boolean hasPool = resultSet.getBoolean("pool");
                            boolean hasGym = resultSet.getBoolean("gym");
                            boolean hasSpa = resultSet.getBoolean("spa");

                            String hotelInfo = "ID: " + id + ", Hotel Name: " + hotelName +
                                    ", Location: " + location + ", Rating: " + rating +
                                    ", Pool: " + hasPool + ", Gym: " + hasGym + ", Spa: " + hasSpa;

                            output.append(hotelInfo).append("\n");
                        }
                    }
                }
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}


        
        
        
        


