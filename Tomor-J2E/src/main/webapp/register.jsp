<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html data-bs-theme="light" data-bs-core="elegant" dir="rtl" lang="ar">
<head>
<meta charset="UTF-8">
<%@include file="includes/header.jsp"%>
<title>تمور</title>
<script>
	document.addEventListener('DOMContentLoaded', function() {
		if (window.location.href.includes('failed')) {
			var alertElement = document.getElementById('alert');
			alertElement.style.opacity = '100%';
		} else {
		}
	});
</script>
</head>
<body class="">
	<%@include file="includes/navbar.jsp"%>

	<div class="container align-middle">
		<div
			class="alert alert-danger border-0 rounded-0 d-flex align-items-center"
			role="alert" id="alert" style="opacity: 0;">

			<i class="fa-light fa-exclamation-circle text-danger-emphasis me-2"></i>

			<div>
				Login <strong>failed</strong> please try again.
			</div>

		</div>
		<div class="row justify-content-center mb-5 mt-2">
			<div class="card shadow" style="width: 50%;">

				<div class="card-body">
					<form action="/tomor/register" method="POST">
						<div class="mb-1">
							<label class="form-label" for="email">البريد الإلكتروني</label> <input
								type="email" class="form-control" name="email" id="email"
								required placeholder="البريد الإلكتروني">
						</div>
						<div class="mb-1">
							<label class="form-label" for="phone_number">رقم الهاتف</label>
							<input type="tel" class="form-control" name="phone_number"
								id="phone_number" required placeholder="رقم الهاتف">
						</div>

						<div class="mb-1">
							<label class="form-label" for="password">كلمة السر</label> <input
								type="password" class="form-control" name="password"
								id="password" required placeholder="كلمة السر">
						</div>
						<hr>
						<div class="row">
							<div class="mb-1 col">
								<label class="form-label" for="firstname">الإسم الشخصي</label> <input
									type="text" class="form-control" name="firstname"
									id="firstname" required placeholder="لإسم الشخصي">
							</div>
							<div class="mb-1 col">
								<label class="form-label" for="lastname">الإسم العائلي</label> <input
									type="text" class="form-control" name="lastname" id="lastname"
									required placeholder="الإسم العائلي ">
							</div>
						</div>
						<div class="mb-1">
							<label class="form-label" for="birthdate">تاريخ الميلاد</label> <input
								type="date" class="form-control" name="birthdate" id="birthdate"
								required placeholder="تاريخ الميلاد">
						</div>
						<hr>
						<div class="mb-1">
							<label class="form-label" for="address">العنوان</label> <input
								type="text" class="form-control" name="address" id="address"
								required placeholder="العنوان">
						</div>
						<div class="mb-1">
							<label for="city" class="form-label">المدينة</label> <select
								class="form-select" id="city" name="city">
								<option selected>إختر المدينة</option>
								<option value="rach">الرشيدية</option>
								<option value="fas">فاس</option>
								<option value="rbat">الرباط</option>
								<option value="casa">الدار البيضاء</option>
							</select>
						</div>
						<div class="d-flex align-items-center">
							<button type="submit" class="btn btn-primary w-100 mt-2 btn-lg">إنشاء حساب</button>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>
</body>
<%@include file="includes/footer.jsp"%>
</html>
