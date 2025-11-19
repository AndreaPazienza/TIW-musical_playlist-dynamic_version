package controllers;

import beans.User;
import dao.PlaylistDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/createPlaylist")
@MultipartConfig
//Management of the create form button
public class CreatePlaylist extends HttpServlet {

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
	
	//Insertion of the playlist and loading of the home page
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			HttpSession session = request.getSession(false);
			//Check on the validity of the session and the user
			if ((session == null) || (session.getAttribute("currentUser") == null)) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Error in the server session");
			} else {
				int userID = ((User) session.getAttribute("currentUser")).getUserID();
				String playlistName = request.getParameter("playlistName");
				String[] selectedSongs = request.getParameterValues("selectedSongs");
				List<Integer> listID = new ArrayList<Integer>();
				boolean error = false;
				//Creation of the PlaylistDAO
				PlaylistDAO playlistDAO = new PlaylistDAO(connection);
				if ((playlistName != null) && !(playlistName.isEmpty()) && (selectedSongs != null)) {
					try {
						for (String song : selectedSongs) {
							listID.add(Integer.parseInt(song));
						}
					} catch (NumberFormatException e) { 
						error = true;
						response.getWriter().println("Wrong parameter format");
					}
				} else {
					error = true;
					response.getWriter().println("Incomplete parameter insertion");
				}
				//Creation of the playlist and insertion of the selected songs
				if (!error) {
					try {
						connection.setAutoCommit(false);
						int playlistID = playlistDAO.createPlaylist(playlistName, userID);
						for(int songID : listID) {
							playlistDAO.addSongToPlaylist(playlistID, songID, 0);
						}
						connection.commit();
						response.setStatus(HttpServletResponse.SC_OK);
					} catch (SQLException e) {
						try {
							connection.rollback();
						} catch (SQLException e1) {}
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println("Database insertion error");
					} finally {
						try {
							connection.setAutoCommit(true);
						} catch (SQLException e1) {}
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
