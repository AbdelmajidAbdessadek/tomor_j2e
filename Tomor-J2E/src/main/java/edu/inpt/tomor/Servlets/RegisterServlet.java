package edu.inpt.tomor.Servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import edu.inpt.tomor.DBSingletonConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute("session", session);

		if (session.getAttribute("loggedIn") == null) {
			response.sendRedirect("register.jsp");
		} else if (session.getAttribute("loggedIn").equals("admin")) {
			response.sendRedirect("admin.jsp");
		} else if (!session.getAttribute("loggedIn").equals("user")) {
			response.sendRedirect("products.jsp");
		} else {
			response.sendRedirect("products.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String birthdate = request.getParameter("birthdate");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String phone_number = request.getParameter("phone_number");
		String password = request.getParameter("password");

		// TODO: md5 is weak
		String passwordHash = hashPasswordMD5(password);
		String userId = generateUniqueUserId();

		try {
			Connection connection = DBSingletonConnection.getConnection();
			String insertQuery = "INSERT INTO users (firstname, lastname, birthdate, address, city, country, username, email, phone_number, password_hash, id) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
					Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setString(1, firstname);
				preparedStatement.setString(2, lastname);
				preparedStatement.setString(3, birthdate);
				preparedStatement.setString(4, address);
				preparedStatement.setString(5, city);
				preparedStatement.setString(6, country);
				preparedStatement.setString(7, username);
				preparedStatement.setString(8, email);
				preparedStatement.setString(9, phone_number);
				preparedStatement.setString(10, passwordHash);
				preparedStatement.setString(11, userId);

				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					response.sendRedirect("login");
				} else {
					response.getWriter().println("Registration failed. Please try again.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("Registration failed due to a database error. Please try again.");
		}
	}

	private String hashPasswordMD5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(password.getBytes());

			StringBuilder result = new StringBuilder();
			for (byte b : bytes) {
				result.append(String.format("%02x", b));
			}

			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String generateUniqueUserId() {
		return UUID.randomUUID().toString();
	}
}
