package controllers;

import beans.User;
import dao.UserDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
//Management of the login form button
public class Login extends HttpServlet {
	
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
	
	//Check on the credentials and redirection to the home page
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Extraction of the parameters
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    //Check on the validity of the input
		if ((username == null) || (password == null)) {
			request.setAttribute("error", true);
	        request.setAttribute("errorMessage", "Incomplete parameters insertion");
	        request.getRequestDispatcher("/start.jsp").forward(request, response);
		} else {
		    //Creation of the UserDAO
			UserDAO userDAO = new UserDAO(connection);
			//Extraction of the user and check
			try {
				User user = userDAO.getUser(username);
				//Comparison between the insert password and the password stored in the database
				if (user != null) {
					if (password.equals((user.getPassword()))) {
						HttpSession session = request.getSession();
						session.setAttribute("currentUser", user);
						String path = "/WEB-INF/home.jsp";
						RequestDispatcher dispatcher = request.getRequestDispatcher(path);
						dispatcher.forward(request, response);
					} else {
						request.setAttribute("error", true);
				        request.setAttribute("errorMessage", "Invalid password");
				        request.getRequestDispatcher("/start.jsp").forward(request, response);
					}
				}
				else {
					request.setAttribute("error", true);
			        request.setAttribute("errorMessage", "Invalid user");
			        request.getRequestDispatcher("/start.jsp").forward(request, response);
				}
			} catch (SQLException e) {
				request.setAttribute("error", true);
		        request.setAttribute("errorMessage", "Database access error");
		        request.getRequestDispatcher("/start.jsp").forward(request, response);
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
