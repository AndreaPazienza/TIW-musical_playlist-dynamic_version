package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
//Management of the logout button
public class Logout extends HttpServlet{
	
	//Serial version of the object
	private static final long serialVersionUID = 1L;

	//Invalidation of the session and redirection to the login page
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		String path = getServletContext().getContextPath();
		response.sendRedirect(path);
	}
}
