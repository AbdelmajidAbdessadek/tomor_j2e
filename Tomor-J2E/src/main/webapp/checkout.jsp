<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.inpt.tomor.Product"%>
<%@ page import="edu.inpt.tomor.DBSingletonConnection"%>
<!DOCTYPE html>
<html data-bs-theme="light" data-bs-core="elegant" dir="rtl" lang="ar">
<head>
<meta charset="UTF-8">
<%@include file="includes/header.jsp"%>
<title>تمور</title>
<style>
.container {
	max-width: 960px;
}

.lh-condensed {
	line-height: 1.25;
}
</style>
</head>
<body>
	<%@include file="includes/navbar.jsp"%>

	<%
	if (!(isLoggedIn && session.getAttribute("loggedIn").equals("user"))) {
		response.sendRedirect("/tomor/products.jsp");
	}
	%>

	<div class="container">
		<div class="mb-5"></div>

		<div class="row">
			<div class="col-md-4 order-md-2 mb-4">
				<%
				double total = 0;
				if (userId != null) {
					try {
						Connection connection = DBSingletonConnection.getConnection();
						String selectQuery = "SELECT product_id, quantity FROM carts WHERE user_id = ?";

						try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
					preparedStatement.setString(1, userId);

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						List<Product> products = new ArrayList<>();

						while (resultSet.next()) {
							String productId = resultSet.getString("product_id");
							int quantity = resultSet.getInt("quantity");
							String productDetailsQuery = "SELECT * FROM products WHERE product_id = ?";
							try (PreparedStatement productDetailsStatement = connection.prepareStatement(productDetailsQuery)) {
								productDetailsStatement.setString(1, productId);

								try (ResultSet productDetailsResultSet = productDetailsStatement.executeQuery()) {
									if (productDetailsResultSet.next()) {
										Product product = new Product();
										product.setProductId(Integer.parseInt(productId));
										product.setProductName(productDetailsResultSet.getString("product_name"));
										product.setImageUrl(productDetailsResultSet.getString("image_url"));
										product.setPrice(productDetailsResultSet.getDouble("price"));
										product.setStockQuantity(quantity);

										products.add(product);

										total += product.getPrice();
									}
								}
							}
						}
				%>
				<h4 class="d-flex justify-content-between align-items-center mb-3">
					<span class="text-muted">Your cart</span> <span
						class="badge badge-secondary badge-pill"><%=products.size()%></span>
				</h4>
				<ul class="list-group mb-3">

					<%
					for (Product product : products) {
					%>
					<li
						class="list-group-item d-flex justify-content-between lh-condensed">
						<div>
							<h6 class="my-0"><%=product.getProductName()%></h6>
							<small class="text-muted"><%=product.getDescription()%></small>
						</div> <span class="text-muted">$<%=product.getPrice()%></span>
					</li>
					<%
					}
					%>
					<li class="list-group-item d-flex justify-content-between"><span>Total
							(USD)</span> <strong class="text-success">$<%=total%></strong></li>
				</ul>
				<%
				}
				} catch (SQLException e) {
				e.printStackTrace();
				}
				} catch (Exception e) {
				e.printStackTrace();
				}
				}
				%>


			</div>
			<div class="col-md-8 order-md-1">
				<form action="/tomor/order">
					<div class="custom-control custom-checkbox">
						<input type="checkbox" class="form-check-input" id="same-address">
						<label class="form-check-label" for="same-address">Shipping
							address is the same as my address</label>
					</div>
					<hr class="mb-4">

					<h4 class="mb-3">Payment</h4>

					<div class="d-block my-3">
						<div class="form-check">
							<input id="credit" name="paymentMethod" type="radio"
								class="form-check-input" checked required> <label
								class="form-check-label" for="credit">Credit card</label>
						</div>
						<div class="form-check">
							<input id="debit" name="paymentMethod" type="radio"
								class="form-check-input" required> <label
								class="form-check-label" for="debit">Debit card</label>
						</div>
						<div class="form-check">
							<input id="paypal" name="paymentMethod" type="radio"
								class="form-check-input" required> <label
								class="form-check-label" for="paypal">PayPal</label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 mb-3">
							<label for="cc-name">Name on card</label> <input type="text"
								class="form-control" id="cc-name" placeholder="" required>
							<small class="text-muted">Full name as displayed on card</small>
							<div class="invalid-feedback">Name on card is required</div>
						</div>
						<div class="col-md-6 mb-3">
							<label for="cc-number">Credit card number</label> <input
								type="text" class="form-control" id="cc-number" placeholder=""
								required>
							<div class="invalid-feedback">Credit card number is
								required</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 mb-3">
							<label for="cc-expiration">Expiration</label> <input type="text"
								class="form-control" id="cc-expiration" placeholder="" required>
							<div class="invalid-feedback">Expiration date required</div>
						</div>
						<div class="col-md-3 mb-3">
							<label for="cc-cvv">CVV</label> <input type="text"
								class="form-control" id="cc-cvv" placeholder="" required>
							<div class="invalid-feedback">Security code required</div>
						</div>
					</div>
					<hr class="mb-4">
					<button class="btn btn-primary btn-lg btn-block w-100"
						type="submit">Place Order</button>
				</form>
			</div>
		</div>
		<footer class="my-5 pt-5 text-muted text-center text-small">
			<p class="mb-1">&copy; Tomor 2024</p>
			<ul class="list-inline">
				<li class="list-inline-item"><a href="#">Privacy</a></li>
				<li class="list-inline-item"><a href="#">Terms</a></li>
				<li class="list-inline-item"><a href="#">Support</a></li>
			</ul>
		</footer>
	</div>
</body>
<%@include file="includes/footer.jsp"%>
</html>