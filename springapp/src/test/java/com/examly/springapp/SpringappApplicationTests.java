package com.examly.springapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringappApplicationTests {

    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/HotelManagement?createDatabaseIfNotExist=true";
    private static final String username = "root"; // Replace with your DB username
    private static final String password = "root"; // Replace with your DB password

    @Test
    public void testDatabaseConnection() {
        // Arrange
        Connection connection = null;

        // Act
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Assert
        assertNotNull(connection);
    }

    @Test
    public void testInsertHotel() throws SQLException {
        // Arrange
        int id = 1001;
        String hotelName = "Hotel 5 Spa";
        String location = "City London";
        double rating = 4.0;
        boolean hasPool = true;
        boolean hasGym = false;
        boolean hasSpa = false;

        // Act
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            SpringappApplication.insertHotel(connection, id, hotelName, location, rating, hasPool, hasGym, hasSpa);

            // Verify that the record was inserted successfully
            String query = "SELECT * FROM hotels WHERE id = ?";
            try (java.sql.PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    assertTrue(resultSet.next());
                    assertEquals(location, resultSet.getString("location"));
                    assertEquals(rating, resultSet.getDouble("rating"));
                    assertEquals(hasPool, resultSet.getBoolean("pool"));
                    assertEquals(hasGym, resultSet.getBoolean("gym"));
                    assertEquals(hasSpa, resultSet.getBoolean("spa"));
                }
            }
        }
    }

	@Test
	public void testDisplayHotels() {
		// Arrange
		String expectedOutput = "ID: 1001, Hotel Name: Hotel 5 Spa, Location: City London, Rating: 4.0, Pool: true, Gym: false, Spa: false\n";
	
		// Act
		try {
			String output = SpringappApplication.displayHotels(DriverManager.getConnection(jdbcUrl, username, password));
	
			// Assert
			assertNotNull(output);
			//assertEquals(expectedOutput, output);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("An exception occurred: " + e.getMessage());
		}
	}
	
}