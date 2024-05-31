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
</head>
<body class="">
	<%@include file="includes/navbar.jsp"%>

	<div class="toast-container position-fixed bottom-0 end-0 p-3">
		<div id="live-toast" class="toast" role="alert" aria-live="assertive"
			aria-atomic="true">
			<div class="toast-header">
				<strong class="me-auto">تم بنجاح!</strong> <small>الأن</small>
				<button type="button" class="btn-close" data-bs-dismiss="toast"
					aria-label="Close"></button>
			</div>
			<div class="toast-body">تم الحذف</div>
		</div>
	</div>

	<div class="container justify-content-center mt-2 mb-4">
		<div class="card">
			<div class="card-header">
				<%
				if (isAdmin) {
				%>
				<strong>معلومات الطلب<strong> <%
 } else {
 %> <strong>سلة المشتريات</strong> <%
 }
 %>
			</div>
			<div class="card-body">
				<%
				if (isLoggedIn) {
					Cart cart = null;
					if (isAdmin) {
						String orderUserId = request.getParameter("user_id");
						cart = CartDAO.getUserCart(orderUserId);
					} else {
						cart = CartDAO.getUserCart(userId);
					}
					double total = cart.getTotal();
				%>
				<table class="table align-middle table-stripped">
					<thead>
						<tr>
							<th>نوع التمور</th>
							<th>الكمية المتوفرة</th>
							<th>الثمن</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<%
						for (Product product : cart.getProducts()) {
						%>
						<tr id="productRow_<%=product.getProductId()%>">
							<td>
								<div class="container ">
									<img class="rounded" src="<%=product.getImageUrl()%>"
										alt="Product Image" height="50"> <span><%=product.getProductName()%></span>
								</div>
							</td>
							<td>x <%=product.getStockQuantity()%></td>
							<td><span class="badge text-bg-primary rounded-pill">$<%=product.getPrice()%></span></td>
							<td><button type="button"
									onClick="removeProductFromCart(<%=product.getProductId()%>);"
									class="btn btn-danger">حذف</button></td>
						</tr>

						<%
						}
						%>
						<tr>
							<td><strong>المجموع</strong></td>
							<td></td>
							<td>
								<%
								if (total > 0) {
								%>
								<h2 class="text-success">
									$<%=total%></h2> <%
 }
 %>
							</td>
							<td>
								<%
								if (total > 0) {
									if (isLoggedIn && isAdmin) {
								%> <a type="button" class="btn btn-success"
								href="/tomor/checkout.jsp">تم التوصيل؟</a> <%
 } else {
 %> <a type="button" class="btn btn-success"
								href="/tomor/checkout.jsp">الدفع</a> <%
 }
 }
 %>
							</td>
						</tr>
					</tbody>
				</table>
				<%
				}
				%>
			</div>
			<div class="card-footer d-flex justify-content-end"></div>
		</div>
	</div>
</body>
<%@include file="includes/footer.jsp"%>
<script>
function removeProductFromCart(productId) {
	const toastLiveExample = document.getElementById("live-toast");
	const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
	toastBootstrap.show();
	
	  sendAjaxRequest('/tomor/cart_action?remove=' + productId, 'GET', function(response) {
	        console.log(response);
	        var row = document.getElementById('productRow_' + productId);
	        if (row) {
	            row.remove();
	        }
	    });
}

function sendAjaxRequest(url, method, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                callback(xhr.responseText);
            } else {
                console.error('Error: ' + xhr.status);
            }
        }
    };
    xhr.open(method, url, true);
    xhr.send();
}
</script>
</html>