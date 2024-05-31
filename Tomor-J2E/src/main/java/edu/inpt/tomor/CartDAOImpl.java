package edu.inpt.tomor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAOImpl implements CartDAO {
	private Connection connection;

	public CartDAOImpl() {
		this.connection = DBSingletonConnection.getConnection();
	}

	@Override
	public Cart getUserCart(String userId) {
		Cart cart = new Cart();
		try {
			Connection connection = DBSingletonConnection.getConnection();
			String selectQuery = "SELECT product_id, quantity FROM carts WHERE user_id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
				preparedStatement.setString(1, userId);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						String productId = resultSet.getString("product_id");
						int quantity = resultSet.getInt("quantity");

						String productDetailsQuery = "SELECT * FROM products WHERE product_id = ?";
						try (PreparedStatement productDetailsStatement = connection
								.prepareStatement(productDetailsQuery)) {
							productDetailsStatement.setString(1, productId);

							try (ResultSet productDetailsResultSet = productDetailsStatement.executeQuery()) {
								if (productDetailsResultSet.next()) {
									Product product = new Product();
									product.setProductId(Integer.parseInt(productId));
									product.setProductName(productDetailsResultSet.getString("product_name"));
									product.setImageUrl(productDetailsResultSet.getString("image_url"));
									product.setPrice(productDetailsResultSet.getDouble("price"));
									product.setStockQuantity(quantity);

									cart.addProduct(product);
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cart;
	}

	@Override
	public void addToCart(String userId, String productId, int quantity) {
		if (!isTableExists("carts")) {
			System.out.println("Error: The 'carts' table does not exist.");
			createCartsTable();
			return;
		}

		String selectQuery = "SELECT quantity FROM carts WHERE user_id = ? AND product_id = ?";
		String updateQuery = "UPDATE carts SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
		String insertQuery = "INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?)";

		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setString(1, userId);
			selectStatement.setString(2, productId);

			try (ResultSet resultSet = selectStatement.executeQuery()) {
				if (resultSet.next()) {

					int currentQuantity = resultSet.getInt("quantity");

					try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
						updateStatement.setInt(1, quantity);
						updateStatement.setString(2, userId);
						updateStatement.setString(3, productId);
						int rowsAffected = updateStatement.executeUpdate();

						if (rowsAffected > 0) {
							System.out.println("Quantity updated successfully for product in cart for user: " + userId);
						} else {
							System.out.println("Failed to update quantity for product in cart for user: " + userId);
						}
					}
				} else {

					try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
						insertStatement.setString(1, userId);
						insertStatement.setString(2, productId);
						insertStatement.setInt(3, quantity);
						int rowsAffected = insertStatement.executeUpdate();

						if (rowsAffected > 0) {
							System.out.println(quantity + " product(s) added to cart successfully for user: " + userId);
						} else {
							System.out.println("Failed to add product to cart for user: " + userId);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeFromCart(String userId, String productId, int quantityToRemove) {
		if (!isTableExists("carts")) {
			System.out.println("Error: The 'carts' table does not exist.");
			return;
		}

		String selectQuery = "SELECT quantity FROM carts WHERE user_id = ? AND product_id = ?";
		String updateQuery = "UPDATE carts SET quantity = ? WHERE user_id = ? AND product_id = ?";
		String deleteQuery = "DELETE FROM carts WHERE user_id = ? AND product_id = ?";

		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setString(1, userId);
			selectStatement.setString(2, productId);

			try (ResultSet resultSet = selectStatement.executeQuery()) {
				if (resultSet.next()) {
					int currentQuantity = resultSet.getInt("quantity");

					int newQuantity = Math.max(currentQuantity - quantityToRemove, 0);

					if (newQuantity > 0) {

						try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
							updateStatement.setInt(1, newQuantity);
							updateStatement.setString(2, userId);
							updateStatement.setString(3, productId);
							int rowsAffected = updateStatement.executeUpdate();

							if (rowsAffected > 0) {
								System.out.println(quantityToRemove
										+ " product(s) removed from cart successfully for user: " + userId);
							} else {
								System.out.println("Failed to remove product from cart for user: " + userId);
							}
						}
					} else {

						try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
							deleteStatement.setString(1, userId);
							deleteStatement.setString(2, productId);
							int rowsAffected = deleteStatement.executeUpdate();

							if (rowsAffected > 0) {
								System.out.println("Product removed from cart successfully for user: " + userId);
							} else {
								System.out.println("Failed to remove product from cart for user: " + userId);
							}
						}
					}
				} else {
					System.out.println("Product not found in the cart for user: " + userId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isTableExists(String tableName) {
		try {
			ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void createCartsTable() {
		String createTableQuery = "CREATE TABLE carts (user_id VARCHAR(255), product_id VARCHAR(255), quantity INT)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
			preparedStatement.executeUpdate();
			System.out.println("Created 'carts' table successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to create 'carts' table.");
		}
	}

}
