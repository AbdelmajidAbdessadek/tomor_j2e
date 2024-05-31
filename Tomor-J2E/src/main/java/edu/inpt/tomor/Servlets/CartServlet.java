package edu.inpt.tomor.Servlets;

import edu.inpt.tomor.CartDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/cart_action")
public class CartServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("user_id");

			if (userId != null) {
				String action = request.getParameter("add");
				if (action != null) {
					addToCart(userId, action);
				}

				action = request.getParameter("remove");
				if (action != null) {
					removeFromCart(userId, action);
				}

				response.sendRedirect("/tomor/products.jsp");
			} else {
				response.sendRedirect("/tomor/login");
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addToCart(String userId, String productId) {
		CartDAOImpl cartDAO = new CartDAOImpl();
		cartDAO.addToCart(userId, productId, 1);
	}

	private void removeFromCart(String userId, String productId) {
		CartDAOImpl cartDAO = new CartDAOImpl();
		cartDAO.removeFromCart(userId, productId, 999999);
	}
}
