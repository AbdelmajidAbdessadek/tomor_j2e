package edu.inpt.tomor.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import edu.inpt.tomor.DBSingletonConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/add_product", "/remove_product" })
public class ProductManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pathInfo = request.getRequestURI();
		String loggedInUser = (String) request.getSession().getAttribute("loggedIn");



		if (pathInfo == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
			System.out.println("Error! Invalid pathInfo");
			System.out.print(pathInfo);
			System.out.println();
			return;
		}

		switch (pathInfo) {
			case "/tomor/add_product":
				addProduct(request, response);
				break;
			case "/tomor/remove_product":
				removeProduct(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
				System.out.print(pathInfo);
		}
	}

	private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		int productId = (int) (currentTimestamp.getTime() / 1000);
		String productName = request.getParameter("product_name");
		String category = request.getParameter("category");
		double price = Double.parseDouble(request.getParameter("price"));
		String description = request.getParameter("description");
		int stockQuantity = Integer.parseInt(request.getParameter("stock_quantity"));
		String imageUrl = request.getParameter("image_url");

		try {
			Connection connection = DBSingletonConnection.getConnection();
			String insertQuery = "INSERT INTO products (product_id, product_name, category, price, description, stock_quantity, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setInt(1, productId);
				preparedStatement.setString(2, productName);
				preparedStatement.setString(3, category);
				preparedStatement.setDouble(4, price);
				preparedStatement.setString(5, description);
				preparedStatement.setInt(6, stockQuantity);
				preparedStatement.setString(7, imageUrl);

				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					response.sendRedirect("/tomor/products.jsp");
				} else {
					response.getWriter().println("Failed to add product to the database.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("Database error. Failed to add product.");
		}
	}

	private void removeProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String productId = request.getParameter("product_id");
		System.out.print("Received Product Remove request ");
		System.out.println(productId);
		try {
			Connection connection = DBSingletonConnection.getConnection();
			String removeQuery = "DELETE FROM products WHERE product_id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
				preparedStatement.setString(1, productId);

				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					response.getWriter().println("Product removed from the database successfully.");
				} else {
					response.getWriter().println("Failed to remove product from the database.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("Database error. Failed to remove product.");
		}
	}
}
