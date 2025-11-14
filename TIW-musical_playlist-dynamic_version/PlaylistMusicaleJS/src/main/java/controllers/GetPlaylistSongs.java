package controllers;

import beans.Song;
import dao.SongDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;

@WebServlet("/getPlaylistSongs")
//Loading the playlist page
public class GetPlaylistSongs extends HttpServlet {

	//Serial version of the object
	private static final long serialVersionUID = 1L;
	//Connectiongson with the database
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
	
	//Extraction of the elements and loading of the playlist page
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		//Check on the validity of the session and the user
		if ((session == null) || (session.getAttribute("currentUser")) == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error in the server session");
		}
		else {
			int playlistID = 0;
			boolean error = false;
			//Creation of the SongDAO
			SongDAO songDAO = new SongDAO(connection);
			//Extraction of the playlistID
			if (request.getParameter("playlistID") != null) {
				try {
					playlistID = Integer.parseInt(request.getParameter("playlistID"));
				} catch (NumberFormatException e) { 
					error = true;
					response.getWriter().println("Wrong parameter format");
				}
			} else {
				error = true;
		        response.getWriter().println("Incomplete parameter insertion");
			}
			//Extraction of the songs in the playlist
			if (!error) {
				try {
					List<Song> playlistSongs = songDAO.getPlaylistSongs(playlistID);
					//Creation of the json file to retrieve
					if (playlistSongs != null) {
						String json = new Gson().toJson(playlistSongs);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().write(json);
					} else {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println("Not possible to load the page");
					}
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Not possible to retrieve the playlist");
				}	
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
