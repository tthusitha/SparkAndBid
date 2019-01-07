package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.SparkDAO;
import Model.Product;

/**
 * Servlet implementation class Index
 */
@WebServlet("/Home")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SparkDAO sparkDAO = new SparkDAO();
	private TreeSet<Product> products = new TreeSet<>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sparkDAO.initSparkConnectionAndReadDataSet();
		products = sparkDAO.createProductFromListOfRows(1);
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		int recordsPerPage = 12;
		
		TreeSet<Product> products = new TreeSet<>();
		TreeSet<Product> withBids = new TreeSet<>();
		
        products = sparkDAO.createProductFromListOfRows(1);
        withBids = sparkDAO.getBidsByProduct(products);
        
        request.setAttribute("products", withBids);
        
        int rows = sparkDAO.getNumberOfRows();
        
        int nbPages = rows / recordsPerPage;
        
        if (nbPages % recordsPerPage > 0) {
            nbPages++;
        }
        
        int length = sparkDAO.getNumberOfRows();
        session.setAttribute("productsLength", length);
        request.setAttribute("length", length);
        request.setAttribute("username", session.getAttribute("username"));
        request.setAttribute("nbPages", nbPages);
        request.setAttribute("currentPage", 1);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	}

}