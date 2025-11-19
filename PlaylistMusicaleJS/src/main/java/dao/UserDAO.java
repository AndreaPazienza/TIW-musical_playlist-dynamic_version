package dao;

import beans.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class that extracts from the User table of the database
public class UserDAO {
	
	//Connection with the database
	private Connection connection;

	//Constructor of the class
	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	//Retrieves the user that corresponds to the input username
	public User getUser(String username) throws SQLException {
		
		User user = null;
		String query = "SELECT * FROM User WHERE username = ?";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			result = statement.executeQuery();
			//Creation of the User object to retrieve, if present in the query result
			while (result.next()) {
				user = new User();
				user.setUserID(result.getInt("userID"));
				user.setUsername(result.getString("username"));
				user.setPassword(result.getString("password"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				result.close();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
			try {
				statement.close();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return user;
	}
}