package controllers;

import beans.Song;
import dao.SongDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;

@WebServlet("/getPlayer")
//Loading of the player page
public class GetPlayer extends HttpServlet{

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
	
	//Extraction of the song and loading of the playlist page
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		//Check on the validity of the session, the user and the playlist
		if ((session == null) || (session.getAttribute("currentUser") == null)) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Error in the server session");
		}
		else {
			int songID = 0;
			boolean error = false;
			//Creation of the SongDAO
			SongDAO songDAO = new SongDAO(connection);
			if (request.getParameter("songID") != null) {
				try {
					songID = Integer.parseInt(request.getParameter("songID"));
				} catch (NumberFormatException e) { 
					error = true;
			        request.setAttribute("errorMessage", "Wrong parameter format");
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Incomplete parameter insertion");
			}
			//Extraction of the song
			if (!error) {
				try {
					Song song = songDAO.getSong(songID);
					//Creation of the json file to retrieve
					if (song != null) {
						String json = new Gson().toJson(song);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(json);
						response.setStatus(HttpServletResponse.SC_OK);
					} else {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println("Not possible to load the page");
					}
				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Database access failed");
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
