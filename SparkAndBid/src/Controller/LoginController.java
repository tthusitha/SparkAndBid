package Controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.SparkDAO;
import Model.User;

/**
 * Servlet implementation class Index
 */
@WebServlet({ "/Login"})
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SparkDAO sparkDAO = new SparkDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		User user = new User();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));

		Collection<User> users = sparkDAO.logInUser(user);
		User exist = new User();
		for(User u : users) {
			exist = u;
		}
        
        if(exist.getUsername().equals(user.getUsername()) && exist.getPassword().equals(user.getPassword())) {
        	session.setAttribute("username", request.getParameter("username"));
			response.sendRedirect("Home");
        }else {
			response.sendRedirect(request.getHeader("Referer"));
        }
	}
	}
		

