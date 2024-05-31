package edu.inpt.tomor.Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import edu.inpt.tomor.DBSingletonConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("user_id");
        createOrdersTableIfNotExists();
        if (userId != null) {
            if (isUserInOrdersTable(userId)) {
                updateOrderDate(userId);
            } else {
                insertOrder(userId);
            }

            response.getWriter().println("Order processed successfully");
        } else {
            response.getWriter().println("User not logged in");
        }
    }

    private void createOrdersTableIfNotExists() {
        try {
            Connection connection = DBSingletonConnection.getConnection();
            String checkTableQuery = "SHOW TABLES LIKE 'orders'";
            boolean tableExists;

            try (PreparedStatement preparedStatement = connection.prepareStatement(checkTableQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    tableExists = resultSet.next();
                }
            }

            if (!tableExists) {
                String createTableQuery = "CREATE TABLE orders ("
                        + "order_date TIMESTAMP,"
                        + "user_id VARCHAR(36) PRIMARY KEY"
                        + ")";

                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(createTableQuery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserInOrdersTable(String userId) {
        try {
            Connection connection = DBSingletonConnection.getConnection();
            String checkUserQuery = "SELECT * FROM orders WHERE user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(checkUserQuery)) {
                preparedStatement.setString(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertOrder(String userId) {
        try {
            Connection connection = DBSingletonConnection.getConnection();
            String insertOrderQuery = "INSERT INTO orders (order_date, user_id) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderQuery)) {
                preparedStatement.setTimestamp(1, getCurrentTimestamp());
                preparedStatement.setString(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderDate(String userId) {
        try {
            Connection connection = DBSingletonConnection.getConnection();
            String updateOrderQuery = "UPDATE orders SET order_date = ? WHERE user_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateOrderQuery)) {
                preparedStatement.setTimestamp(1, getCurrentTimestamp());
                preparedStatement.setString(2, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
