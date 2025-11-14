package controllers;

import dao.PlaylistDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;

@WebServlet("/modifyPlaylistOrder")
@MultipartConfig
//Loading the home page
public class ModifyPlaylistOrder extends HttpServlet {

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
	
	//Update of the order in the playlist
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);	
		//Check the validity of the session and the user
		if ((session == null) || (session.getAttribute("currentUser") == null)) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Session not available");
		} else {
			//Extraction of the playlist order
			String json = request.getParameter("playlistOrder");
			JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
			//Extraction of each parameter of the json file
			int playlistID = jsonObject.get("playlistID").getAsInt();
			List<Integer> playlistSongs = new ArrayList<Integer>();
			JsonArray JsonArrayOfSongs = jsonObject.get("orderedPlaylist").getAsJsonArray();
			JsonArrayOfSongs.forEach(item -> {
				playlistSongs.add(item.getAsInt());
			});
			//Creation of the PlaylistDAO
			PlaylistDAO playlistDAO = new PlaylistDAO(connection);
			//Creation of the playlist and insertion of the selected songs
			try {
				connection.setAutoCommit(false);
				for (int i = 0; i < playlistSongs.size(); i ++) { 
					int songID = playlistSongs.get(i);
					int position = i;
					playlistDAO.modifiyPlaylistOrder(playlistID, songID, position);
				}
				connection.commit();
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {}
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Not possible to reorder the playlist");
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e1) {}
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