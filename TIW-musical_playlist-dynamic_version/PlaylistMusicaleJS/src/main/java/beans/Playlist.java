package beans;

import java.util.Date;

//Class that represents a playlist
public class Playlist {

	//Number that identifies uniquely an user
	private int playlistID;
	//Name associated to the playlist
	private String playlistName;
	//Date of the creation of the playlist
	private Date creationDate;
	//User that created the playlist 
	private User creator;

	//Sets the playlistID
	public void setPlaylistID(int playlistID) {
		this.playlistID = playlistID;
	}
	
	//Sets the playlist name
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	
	//Sets the creation date
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	//Sets the creator of the playlist
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	//Retrieves the playlistID
	public int getPlaylistID() {
		return playlistID;
	}
	
	//Retrieves the playlist name
	public String getPlaylistName() {
		return playlistName;
	}
	
	//Retrieves the creation date
	public Date getCreationDate() {
		return creationDate;
	}
	
	//Retrieves the creator of the playlist
	public User getCreator() {
		return creator;
	}
}