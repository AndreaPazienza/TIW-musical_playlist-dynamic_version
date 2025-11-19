package controllers;

import beans.User;
import dao.SongDAO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Year;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/addSong")
@MultipartConfig
//Management of the addSong form button
public class AddSong extends HttpServlet {

	//Serial version of the object
	private static final long serialVersionUID = 1L;
	//Connection with the database
	private Connection connection = null;

	//Initialization and connection with the database
	public void init() throws ServletException {
		
		try {
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
	}
	
	//Insertion of the song and refresh of the home page
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		//Check on the validity of the session and the user
		if ((session == null) || (session.getAttribute("currentUser") == null)) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Session not available");
		} else {
			//Extraction of the parameters
			String songTitle = request.getParameter("songTitle");
			String albumTitle = request.getParameter("albumTitle");
			Part imagePart = request.getPart("imageFile");
			String artist = request.getParameter("artist");
			int publicationYear = 0;
			String musicalGenre = request.getParameter("musicalGenre");
			Part audioPart = request.getPart("audioFile");
			//Conversion of the string in image and audio files 
			InputStream imageFile = null;
			InputStream audioFile = null;
			String mimeType1 = null;
			String mimeType2 = null;
			boolean error = false;
			if ((request.getParameter("publicationYear") != null) && (imagePart != null) && (audioPart != null)) {
				try {
					publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
				} catch (NumberFormatException e) {
					error = true;
					response.getWriter().println("Wrong parameter format");
				}
				String filename = null;
				imageFile = imagePart.getInputStream();
				filename = imagePart.getSubmittedFileName();
				mimeType1 = getServletContext().getMimeType(filename);
				audioFile = audioPart.getInputStream();
				filename = audioPart.getSubmittedFileName();
				mimeType2 = getServletContext().getMimeType(filename);
			}
			//Check on the validity of the input
			if ((songTitle == null) || (albumTitle == null) || (imageFile == null) || (artist == null) || (musicalGenre == null) || (audioFile == null) || (audioFile == null) || (publicationYear < 1950) || (publicationYear > Year.now().getValue()) || (songTitle.isEmpty()) || (albumTitle.isEmpty()) || (imageFile.available() == 0) || (artist.isEmpty()) || (musicalGenre.isEmpty()) || (audioFile.available() == 0) || !mimeType1.startsWith("image/") || !mimeType2.startsWith("audio/"))
			{
				error = true;
				response.getWriter().println("Incomplete parameter insertion");
			} else {
				int userID = ((User) session.getAttribute("currentUser")).getUserID();
				//Creation of the SongDAO
				SongDAO songDAO = new SongDAO(connection);
				//Insertion of the song, if not already present for the logged user
				if (!error) {
					try {
						if(songDAO.searchUserSong(songTitle, albumTitle, artist, publicationYear, userID) == 0) {
							//Insertion of the song and association with the logged user
							try {
								connection.setAutoCommit(false);
								songDAO.addSong(songTitle, albumTitle, imageFile, artist, publicationYear, musicalGenre, audioFile, userID);
								connection.commit();
								response.setStatus(HttpServletResponse.SC_OK);
							} catch (SQLException e) {
								try {
									connection.rollback();
								} catch (SQLException e1) {}
								response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								response.getWriter().println("Bad database insertion");
							} finally {
								try {
									connection.setAutoCommit(true);
								} catch (SQLException e1) {}
							}
						} else {
							response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
							response.getWriter().println("Song already present");
						}
					} catch (SQLException e) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println("Bad databse insertion input");
					}
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
		}
	}
	
	//Closure of the connection 
	public void destroy() {
		
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {}
	}
}
