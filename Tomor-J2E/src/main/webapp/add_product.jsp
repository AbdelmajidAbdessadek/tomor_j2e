<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	if (session.getAttribute("loggedIn") != null && isAdmin) {
	%>
	<div class="container mt-2" dir="rtl">
		<form method="POST" action="/tomor/add_product">
			<h5 class="mb-3">إضافة تمور</h5>
			<div class="form-floating mb-3">
				<input type="text" class="form-control" id="product_name"
					name="product_name" placeholder="name@example.com" required>
				<label for="product_name">إسم المنتوج</label>
			</div>
			<div class="form-floating mb-3">
				<input type="number" class="form-control" id="price" name="price"
					placeholder="$500.99" required> <label for="price">الثمن</label>
			</div>
			<div class="form-floating mb-3">
				<input type="number" class="form-control" id="stock_quantity"
					name="stock_quantity" placeholder="23" required> <label
					for="stock_quantity">الكمية المتوفرة</label>
			</div>
			<div class="form-floating mb-3">
				<textarea class="form-control" placeholder="Short description"
					id="description" name="description" style="height: 80px;">تمور الراشيدية</textarea>
				<label for="description">وصف المنتوج</label>
			</div>


			<div class="form-floating mb-3">
				<input type="url" class="form-control" id="image_url"
					name="image_url" placeholder="https://placeholder.com/300/300/"
					required> <label for="price">رابط صورة التمور</label>
			</div>

			<div class="text-end">
				<button type="submit" class="btn btn-primary btn-lg w-100">إضافة</button>
			</div>

		</form>
	</div>
	<%
	} else {
	response.sendRedirect("/tomor/products.jsp");
	}
	%>
</body>
<%@include file="includes/footer.jsp"%>
</html>