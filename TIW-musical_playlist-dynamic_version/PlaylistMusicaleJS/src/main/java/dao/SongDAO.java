package dao;

import beans.Song;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Class that extracts from the Song table of the database
public class SongDAO {

	//Connection with the database
	private Connection connection;

	//Constructor of the class
	public SongDAO(Connection connection) {
		this.connection = connection;
	}
	
	//Retrieves the song list that corresponds to the input userID
	public List<Song> getAllSongs(int userID) throws SQLException {
		
		List<Song> songList = new ArrayList<Song>();
		String query = "SELECT * FROM Song JOIN UserSong ON Song.songID = UserSong.songID WHERE UserSong.userID = ?";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, userID);
			result = statement.executeQuery();
			//Creation of the Song object to add to the song list to retrieve, if present in the query result
			while (result.next()) {
				Song song = new Song();
				song.setSongID(result.getInt("songID"));
				song.setSongTitle(result.getString("songTitle"));
				song.setAlbumTitle(result.getString("albumTitle"));
				song.setArtist(result.getString("artist"));
				song.setPublicationYear(result.getInt("publicationYear"));
				songList.add(song);
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
		
		return songList;
	}
	
	//Retrieves the song list that corresponds to the input playlistID
	public List<Song> getPlaylistSongs(int playlistID) throws SQLException {
		
		List<Song> songList = new ArrayList<Song>();
		String query = "SELECT * FROM PlaylistSong JOIN Song ON PlaylistSong.songID = Song.songID WHERE PlaylistSong.playlistID = ? ORDER BY PlaylistSong.position, Song.publicationYear DESC";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, playlistID);
			result = statement.executeQuery();
			//Creation of the Song object to add to the song list to retrieve, if present in the query result
			while (result.next()) {
				Song song = new Song();
				song.setSongID(result.getInt("songID"));
				song.setSongTitle(result.getString("songTitle"));
				song.setAlbumTitle(result.getString("albumTitle"));
				song.setImageFile(Base64.getEncoder().encodeToString(result.getBytes("imageFile")));
				song.setArtist(result.getString("artist"));
				song.setPublicationYear(result.getInt("publicationYear"));
				songList.add(song);
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
		
		return songList;
	}
	
	//Retrieves the song list that corresponds to the input userID and not corresponds to the input playlistID
	public List<Song> getNotPlaylistSongs(int userID, int playlistID) throws SQLException {
		
		List<Song> songList = new ArrayList<Song>();
		String query = "SELECT * FROM Song JOIN UserSong ON Song.songID = UserSong.songID WHERE UserSong.userID = ? AND NOT EXISTS (SELECT 1 FROM PlaylistSong WHERE PlaylistSong.songID = Song.songID AND PlaylistSong.playlistID = ?)";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, userID);
			statement.setInt(2, playlistID);
			result = statement.executeQuery();
			//Creation of the Song object to add to the song list to retrieve, if present in the query result
			while (result.next()) {
				Song song = new Song();
				song.setSongID(result.getInt("songID"));
				song.setSongTitle(result.getString("songTitle"));
				song.setAlbumTitle(result.getString("albumTitle"));
				song.setArtist(result.getString("artist"));
				song.setPublicationYear(result.getInt("publicationYear"));
				songList.add(song);
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
		
		return songList;
	}
	
	//Retrieves the song that corresponds to the input songID
	public Song getSong(int songID) throws SQLException {
		
		Song song = null;
		String query = "SELECT * FROM Song WHERE songID = ?";
		ResultSet result = null;
		PreparedStatement statement = null;
		//Preparation of the statement and execution of the query
		try {  
			statement = connection.prepareStatement(query);
			statement.setInt(1, songID);
			result = statement.executeQuery();
			//Creation of the Song object to retrieve, if present in the query result
			while (result.next()) {
				song = new Song();
				song.setSongID(result.getInt("songID"));
				song.setSongTitle(result.getString("songTitle"));
				song.setAlbumTitle(result.getString("albumTitle"));
				song.setImageFile(Base64.getEncoder().encodeToString(result.getBytes("imageFile")));
				song.setArtist(result.getString("artist"));
				song.setPublicationYear(result.getInt("publicationYear"));
				song.setMusicalGenre(result.getString("musicalGenre"));
				song.setAudioFile(Base64.getEncoder().encodeToString(result.getBytes("audioFile")));
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
		
		return song;
	}
	
	//Retrieves the number of rows insert in the UserSong table
	public int addSong(String songTitle, String albumTitle, InputStream imageFile, String artist, int publicationYear, String musicalGenre, InputStream audioFile, int userID) throws SQLException {
		
		int songID = 0;
		int code = 0;
		//Research of the song to extract the songID
		try {
			songID = searchSong(songTitle, albumTitle, artist, publicationYear);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		//Case of song not present yet in the Song table
		if (songID == 0) {
			try {
				//Insert of the song in the Song table
				songID = addNewSong(songTitle, albumTitle, imageFile, artist, publicationYear, musicalGenre, audioFile);
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
		//Insert of the relationship between the user and the song in the UserSong table
		try {
			code = addUserSong(userID, songID);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		
		return code;
	}
	
	//Retrieves the songID of the Song insert in the Song table
	public int addNewSong(String songTitle, String albumTitle, InputStream imageFile, String artist, int publicationYear, String musicalGenre, InputStream audioFile) throws SQLException {
		
		int songID = 0;
		String query = "INSERT into Song (songTitle, albumTitle, imageFile, artist, publicationYear, musicalGenre, audioFile) VALUES(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		int code = 0;		
		//Preparation of the statement and execution of the update
		try {
			String[] returnKey = {"songID"};
			statement = connection.prepareStatement(query, returnKey);
			statement.setString(1, songTitle);
			statement.setString(2, albumTitle);
			statement.setBlob(3, imageFile);
			statement.setString(4, artist);
			statement.setInt(5, publicationYear);
			statement.setString(6, musicalGenre);
			statement.setBlob(7, audioFile);
			code = statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		//Extraction of the songID, if the update has been done correctly
		if(code > 0) {
			ResultSet generatedKey = null; 
			try {
				generatedKey = statement.getGeneratedKeys();
				while (generatedKey.next()) {
					songID = Integer.parseInt(generatedKey.getString(1));
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
		
		return songID;
	}
	
	//Retrieves the number of row insert in the UserSong table
	public int addUserSong(int userID, int songID) throws SQLException{
		
		String query = "INSERT into UserSong (userID, songID) VALUES(?, ?)";
		PreparedStatement statement = null;		
		int code = 0;
		//Preparation of the statement and execution of the update
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, userID);
			statement.setInt(2, songID);
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
	
	//Retrieves the number of songs already present in the database that corresponds to the input and the userID
	public int searchUserSong(String songTitle, String albumTitle, String artist, int publicationYear, int userID) throws SQLException {
		
		int songID = 0;
		String query = "SELECT * FROM Song JOIN UserSong ON Song.songID = UserSong.songID WHERE Song.songTitle = ? AND Song.albumTitle = ? AND Song.artist = ? AND Song.publicationYear = ? AND UserSong.userID = ?";
		ResultSet result = null;
		PreparedStatement statement = null;		
		//Preparation of the statement and execution of the update
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, songTitle);
			statement.setString(2, albumTitle);
			statement.setString(3, artist);
			statement.setInt(4, publicationYear);
			statement.setInt(5, userID);
			result = statement.executeQuery();
			//Extraction of the songID, if present in the query result
			while (result.next()) {
				songID = result.getInt("songID");
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

		return songID;
	}
	
	//Retrieves the songID of the song that corresponds to the input
	public int searchSong(String songTitle, String albumTitle, String artist, int publicationYear) throws SQLException {
		
		int songID = 0;
		String query = "SELECT * FROM Song WHERE songTitle = ? AND albumTitle = ? AND artist = ? AND publicationYear = ?";
		ResultSet result = null;
		PreparedStatement statement = null;	
		//Preparation of the statement and execution of the update
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, songTitle);
			statement.setString(2, albumTitle);
			statement.setString(3, artist);
			statement.setInt(4, publicationYear);
			result = statement.executeQuery();
			//Extraction of the songID, if present in the query result
			while (result.next()) {
				songID = result.getInt("songID");
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
		
		return songID;
	}
}
