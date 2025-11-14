package controllers;

import dao.PlaylistDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

@WebServlet("/addSongToPlaylist")
@MultipartConfig
//Management of the addSongToPlaylist form button
public class AddSongToPlaylist extends HttpServlet{

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
	
	//Insertion of the song in the playlist and refresh of the playlist page
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		//Check the validity of the session, the user and the playlist
		if ((session == null) || (session.getAttribute("currentUser") == null)) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error in the server session");
		} else {
			int playlistID = 0;
			String[] selectedSongs = request.getParameterValues("selectedSongs");
			List<Integer> listID = new ArrayList<Integer>();
			boolean error = false;
			//Creation of the PlaylistDAO and the SongDAO
			PlaylistDAO playlistDAO = new PlaylistDAO(connection);
			if ((request.getParameter("playlistID") != null) && (selectedSongs != null)) {
				try {
					playlistID = Integer.parseInt(request.getParameter("playlistID"));
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
			//Insertion of the selected songs in the playlist
			if (!error) {
				try {
					connection.setAutoCommit(false);
					for(int songID : listID) {
						playlistDAO.addSongToPlaylist(playlistID, songID, 0);
					}
					connection.commit();
					String json = new Gson().toJson(playlistID);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(json);
				} catch (SQLException e) {
					try {
						connection.rollback();
					} catch (SQLException e1) {}
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Not possible to add the songs to the playlist");
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
