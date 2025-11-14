package beans;

//Class that represents a song
public class Song {

	//Number that identifies uniquely a song
	private int songID;
	//Title associated to the song
	private String songTitle;
	//Title associated to the album of the song
	private String albumTitle;
	//File associated to the cover of the album
	private String imageFile;
	//Name of the artist(s) associated to the song
	private String artist;
	//Year of publication of the song (or the album that contains it)
	private int publicationYear;
	//Musical genre associated to the song
	private String musicalGenre;
	//File associated to the song
	private String audioFile;

	//Sets the songID
	public void setSongID(int songID) {
		this.songID = songID;
	}
	
	//Sets the song title
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	
	//Sets the alum title
	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	
	//Sets the image file
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
	//Sets the artist(s)
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	//Sets the publication year
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
	
	//Sets the musical genre
	public void setMusicalGenre(String musicalGenre) {
		this.musicalGenre = musicalGenre;
	}
	
	//Sets the audio file
	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}
	
	//Retrieves the songID
	public int getSongID() {
		return songID;
	}
	
	//Retrieves the song title
	public String getSongTitle() {
		return songTitle;
	}
	
	//Retrieves the album title
	public String getAlbumTitle() {
			return albumTitle;
		}
	
	//Retrieves the image file
	public String getImageFile() {
		return imageFile;
	}
	
	//Retrieves the artist(s)
	public String getArtist() {
		return artist;
	}
	
	//Retrieves the publication year
	public int getPublicationYear() {
		return publicationYear;
	}
	
	//Retrieves the musical genre
	public String getMusicalGenre() {
		return musicalGenre;
	}
	
	//Retrieves the audio file
	public String getAudioFile() {
		return audioFile;
	}
}