package com.employee.registration.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.employee.registration.model.Employee;

public class EmployeeDao {

	public int registerEmployee(Employee employee) throws ClassNotFoundException {

		String INSERT_USERS_SQL = "INSERT INTO employee"
				+ "  (id,firstname, lastname, username, password, address, contact) VALUES " + " (?, ?, ?, ?, ?,?,?);";

		int result = 0;

		Class.forName("com.mysql.cj.jdbc.Driver");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees?useSSL=false",
				"root", "root");

				// Step 2:Create a statement using connection object

				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL,
						Statement.RETURN_GENERATED_KEYS)) {
			/*
			 * This statement is for 1 data in DB as a primary key.
			 * 
			 * preparedStatement.setInt(1,1);
			 * 
			 */
			preparedStatement.setString(1, employee.getFirstname());
			preparedStatement.setString(2, employee.getLastname());
			preparedStatement.setString(3, employee.getUsername());
			preparedStatement.setString(4, employee.getPassword());
			preparedStatement.setString(5, employee.getAddress());
			preparedStatement.setString(6, employee.getContact());

			// Step 3: Execute the query or update query

			result = preparedStatement.executeUpdate();
			
			// If the insertion was successful, retrieve the auto-generated keys
			if (result > 0) {
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				
			// If auto-generated keys are available, print the newly inserted ID
				if (generatedKeys.next()) {
					int newId = generatedKeys.getInt(1);
					System.out.println("Inserted with ID: " + newId);
				}
			}

			System.out.println("Data inserted Successfully");

		} catch (SQLException e) {
			// process sql exception
			printSQLException(e);
		}
		return result;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}