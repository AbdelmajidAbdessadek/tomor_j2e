<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.inpt.tomor.Product"%>
<%@ page import="edu.inpt.tomor.ProductDAOImpl"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html data-bs-theme="light" data-bs-core="elegant" dir="rtl" lang="ar">
<head>
<meta charset="UTF-8">
<%@include file="includes/header.jsp"%>

<title>تمور</title>
</head>
<body class="">
	<%@include file="includes/navbar.jsp"%>
	<%
	isLoggedIn = session.getAttribute("loggedIn") != null;
	if (isLoggedIn && session.getAttribute("loggedIn").equals("admin")) {
	%>
	<div class="modal fade" id="confirm-modal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1" aria-labelledby="modal-title-2"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h1 class="modal-title fs-5">Confirmation Dialog</h1>
					<button type="button" onclick="closeModal();" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">This action cannot be undone, are you sure you want to remove this product?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal" onclick="closeModal();">Cancel</button>
					<button type="button" class="btn btn-danger" onclick="doRemoveRequest();">Remove</button>
				</div>
			</div>
		</div>
	</div>

	<div class="toast-container position-fixed bottom-0 end-0 p-3">
		<div id="remove-success-toast" class="toast" role="alert"
			aria-live="assertive" aria-atomic="true">
			<div class="toast-header">
				<strong class="me-auto">Success!</strong> <small>Just now</small>
				<button type="button" class="btn-close" data-bs-dismiss="toast"
					aria-label="Close"></button>
			</div>
			<div class="toast-body">Product has been removed successfully.</div>
		</div>
	</div>
	<% } %>
	<div class="container mt-2 mb-4">
		<div class="card">
			<%
			if (isLoggedIn && session.getAttribute("loggedIn").equals("admin")) {
			%>
			<div
				class="card-header mb-2 d-flex flex-row justify-content-between align-items-center">
				<div class="d-flex flex-col align-items-center">
					<h5>Products Manager</h5>
				</div>
				<a class="btn btn-primary" href="/tomor/add_product.jsp">Add
					New Product</a>
			</div>
			<div class="card-body">
				<%
				double total = 0;
				if (userId != null) {
					try {
						Connection connection = DBSingletonConnection.getConnection();
						String selectQuery = "SELECT * FROM products";

						try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						List<Product> products = new ArrayList<>();

						while (resultSet.next()) {
							String productId = resultSet.getString("product_id");
							Product product = new Product();
							product.setProductId(Integer.parseInt(productId));
							product.setProductName(resultSet.getString("product_name"));
							product.setImageUrl(resultSet.getString("image_url"));
							product.setPrice(resultSet.getDouble("price"));
							product.setStockQuantity(resultSet.getInt("stock_quantity"));

							products.add(product);

							total += product.getPrice();
						}
				%>
				<table class="table align-middle table-stripped">
					<thead>
						<tr>
							<th>نوع التمر</th>
							<th>الكمية المتوفرة</th>
							<th>ثمن الكيلوجرام</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<%
						for (Product product : products) {
						%>
						<tr id="productRow_<%=product.getProductId()%>">
							<td>
								<div class="container ">
									<img class="rounded" src="<%=product.getImageUrl()%>"
										alt="Product Image" height="50"> <span><%=product.getProductName()%></span>
								</div>
							</td>
							<td><%=product.getStockQuantity()%></td>
							<td><span class="badge text-bg-primary rounded-pill">$<%=product.getPrice()%></span></td>
							<td>
								<div>
									<a type="button" class="btn btn-secondary ml-1">تعديل</a>
									<button type="button" class="btn btn-danger"
										onclick="removeProduct(<%=product.getProductId()%>);">حذف</button>
								</div>
							</td>
						</tr>

						<%
						}
						%>
					</tbody>
				</table>
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
			<%
			} else {
			%>
			<div class="card-header mb-2">
				<h5>تمورنا</h5>
			</div>
			<div class="row row-cols-1 row-cols-sm-3 g-2 p-2">

				<%
				ProductDAOImpl productDAO = new ProductDAOImpl();
				List<Product> productList = productDAO.getAll();

				for (Product product : productList) {
				%>
				<div class="col d-flex justify-content-center mt-1 mb-1">
					<div class="card specific-w-300">
						<img src="<%=product.getImageUrl()%>" class="card-img-top"
							alt="Product Image" height="240">
						<div class="card-body">
							<h5 class="card-title"><%=product.getProductName()%></h5>
							<p class="card-text fs-5 d-flex align-items-center">
								<strong class="text-success-emphasis me-2">$<%=product.getPrice()%></strong>
							</p>
							<a href="/tomor/cart_action?add=<%=product.getProductId()%>"
								class="stretched-link btn btn-success btn-lg w-100"> <i
								class="fa-solid fa-bag-shopping me-1"></i> شراء
							</a>
						</div>
					</div>
				</div>
				<%
				}
				}
				%>
			</div>
		</div>
	</div>
	<script>
	var productIdTarget = 0;
	const modal = new bootstrap.Modal($("#confirm-modal")[0]);
	function removeProduct(productId) {
		productIdTarget = productId;
		modal.show();
	}
	
	function closeModal() {
		modal.hide();
	}
	
    function doRemoveRequest() {
		modal.hide();
        $.post(`/tomor/remove_product`, {product_id: productIdTarget})
        .done(function() {
           	$("#productRow_" + productIdTarget).remove();
           	$("#remove-success-toast").show();
        })
        .fail(function() {
            
        });
    }
	</script>
</body>
<%@include file="includes/footer.jsp"%>
</html>