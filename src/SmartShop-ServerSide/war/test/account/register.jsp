<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register Form</title>
</head>
<body>

<form name="register_form" action="/api/abc/account-register" method="POST">
	<div align="center"><br>
	<br>
	Username1: <input type="text" size="25" id="username"> <br>
	Password: <input type="text" size="25" id="password"> <br>
	First Name: <input type="text" size="25" id="first_name"> <br>
	Last Name: <input type="text" size="25" id="last_name"> <br>
	Phone: <input type="text" size="25" id="phone"> <br>
	Email: <input type="text" size="25" id="email"> <br>
	Address: <input type="text" size="25" id="address"> <br>
	Birthday: <input type="text" size="25" id="birthday"> <br>
	Lat: <input type="text" size="25" id="lat"> <br>
	Lng: <input type="text" size="25" id="lng"> <br>
	
	<input type="submit" value="Submit" />
	<br>
	</div>
</form>

</body>
</html>