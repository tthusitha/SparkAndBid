package Controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.SparkDAO;
import Model.Bid;
import Model.Product;
import Model.User;

/**
 * Servlet implementation class ProductController
 */
@WebServlet("/ProductController")
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SparkDAO sparkDAO = new SparkDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int currentPage = Integer.valueOf(request.getParameter("currentPage"));
        int recordsPerPage = 12;
        
        TreeSet<Product> products = new TreeSet<>();
        TreeSet<Product> withBids = new TreeSet<>();
        
        products = sparkDAO.createProductFromListOfRows(currentPage);
        withBids = sparkDAO.getBidsByProduct(products);
        
        request.setAttribute("products", withBids);
        
        int rows = sparkDAO.getNumberOfRows();
        
        int nbPages = rows / recordsPerPage;
        
        if (nbPages % recordsPerPage > 0) {
            nbPages++;
        }
        
        request.setAttribute("length", session.getAttribute("productsLength"));
        request.setAttribute("nbPages", nbPages);
        request.setAttribute("currentPage", currentPage);

        RequestDispatcher dispatcher = request.getRequestDispatcher("listProducts.jsp");
        dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		float bidPrice = Float.valueOf(request.getParameter("bid"));
		Integer id = Integer.valueOf(request.getParameter("id"));
		String username = session.getAttribute("username").toString();
		
		User user = new User();
		user.setUsername(username);
		
		Collection<User> users = new HashSet<>();
		users.add(user);
		
		Bid bid = new Bid();
		bid.setPrice(bidPrice);
		bid.setDate(new Timestamp(System.currentTimeMillis()));
		bid.setUsers(users);
		
		sparkDAO.addBidToProduct(id, bid);
		response.sendRedirect(request.getHeader("Referer"));
	}

}