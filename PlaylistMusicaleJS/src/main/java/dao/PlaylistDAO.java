package dao;

import beans.Playlist;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class that extracts from the Playlist table of the database
public class PlaylistDAO {

	//Connection with the database
	private Connection connection;

	//Constructor of the class
	public PlaylistDAO(Connection connection) {
		this.connection = connection;
	}
	
	//Retrieves the playlist list that corresponds to the input userID
	public List<Playlist> getPlaylistList(int userID) throws SQLException {
		
		List<Playlist> playlistList = new ArrayList<Playlist>();
		String query = "SELECT * FROM Playlist WHERE userID = ? ORDER BY creationDate DESC";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {
			statement = connection.prepareStatement(query);
			statement.setLong(1, userID);
			result = statement.executeQuery();
			//Creation of the Playlist object to add to the playlist list to retrieve, if present in the query result
			while (result.next()) {
				Playlist playlist = new Playlist();
				playlist.setPlaylistID(result.getInt("playlistID"));
				playlist.setPlaylistName(result.getString("playlistName"));
				playlist.setCreationDate(new Date(result.getTimestamp("creationDate").getTime()));
				playlistList.add(playlist);
			}
		} catch (SQLException e) {;
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
		
		return playlistList;
	}
	
	//Retrieves the playlist that corresponds to the input playlistID
	public Playlist getPlaylist(int playlistID) throws SQLException {
		
		Playlist playlist = null;
		String query = "SELECT * FROM Playlist WHERE playlistID = ?";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {   
			statement = connection.prepareStatement(query);
			statement.setInt(1, playlistID);
			result = statement.executeQuery();
			//Creation of the Playlist object, if present in the query result
			while (result.next()) {
				playlist = new Playlist();
				playlist.setPlaylistID(result.getInt("playlistID"));
				playlist.setPlaylistName(result.getString("playlistName"));
				playlist.setCreationDate(new Date(result.getTimestamp("creationDate").getTime()));
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
		
		return playlist;
	}
	
	//Retrieves the playlistID of the playlist insert in the Playlist table
	public int createPlaylist(String playlistName, int userID) throws SQLException {
		
		int playlistID = 0;
		String query = "INSERT into Playlist (playlistName, userID) VALUES(?, ?)";
		PreparedStatement statement = null;
		int code = 0;		
		//Preparation of the statement and execution of the update
		try {
			String[] returnKey = {"playlistID"};
			statement = connection.prepareStatement(query, returnKey);
			statement.setString(1, playlistName);
			statement.setInt(2, userID);
			code = statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		//Extraction of the playlistID, if the update has been done correctly
		if (code > 0) {
			ResultSet generatedKey = null; 
			try {
				generatedKey = statement.getGeneratedKeys();
				while (generatedKey.next()) {
					playlistID = Integer.parseInt(generatedKey.getString(1));
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			} finally {
				try {
					generatedKey.close();
				} catch (SQLException e) {
					throw new SQLException(e);
				}
				try {
					statement.close();
				} catch (SQLException e) {
					throw new SQLException(e);
				}
			}
		}
		
		return playlistID;
	}
	
	//Retrieves the number of rows insert in the PlaylistSong table
	public int addSongToPlaylist(int playlistID, int songID, int position) throws SQLException {
		
		String query = "INSERT into PlaylistSong (playlistID, songID, position) VALUES(?, ?, ?)";
		PreparedStatement statement = null;
		int code = 0;
		//Preparation of the statement and execution of the update
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, playlistID);
			statement.setInt(2, songID);
			statement.setInt(3, position);
			code = statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return code;
	}
	
	//Retrieves the number of rows midified in the PlaylistSong table
	public int modifiyPlaylistOrder(int playlistID, int songID, int position) throws SQLException {
		
		String query = "UPDATE PlaylistSong SET position = ? WHERE playlistID = ? AND songID = ?";
		PreparedStatement statement = null;
		int code = 0;
		//Preparation of the statement and execution of the update
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, position);
			statement.setInt(2, playlistID);
			statement.setInt(3, songID);
			code = statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		
		return code;
	}
}
