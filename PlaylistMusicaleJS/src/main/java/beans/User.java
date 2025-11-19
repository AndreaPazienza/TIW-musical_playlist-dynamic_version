package beans;

//Class that represents an user
public class User {
	
	//Number that identifies uniquely an user
	private int userID;
	//Username associated to the user
	private String username;
	//Password associated to the user
	private String password;
	
	//Sets the userID
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	//Sets the username
	public void setUsername(String username) {
		this.username = username;
	}
	
	//Sets the password
	public void setPassword(String password) {
		this.password = password;
	}
	
	//Retrieves the userID
	public int getUserID() {
		return userID;
	}
	
	//Retrieves the username
	public String getUsername() {
		return username;
	}
	
	//Retrieves the password
	public String getPassword() {
		return password;
	}
}