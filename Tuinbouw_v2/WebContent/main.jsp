<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tuinbouwbedrijf Hitek</title>
	<link rel="stylesheet" type="text/css" href="style/inlogpagina.css">
	<link rel="stylesheet" type="text/css" href="style/style.css">
	<link rel="stylesheet" type="text/css" href="style/loader.css">
    <link href = "style/bootstrap.min.css" rel = "stylesheet">      
    <script type="text/javascript" src="script/jquery-2.1.3.min.js"></script>
    <script  type="text/javascript" src = "script/bootstrap.min.js"></script>
    <script type= "text/javascript" src = "script/toonLoader.js"></script>
</head>
<body>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ page isELIgnored="false"%>
	<div class='container'>
		<div class='input-group paneel'>
			<form action='inloggen' method='post'>
				<input type='text' name='gebruikersnaam' size='30' class = 'input' placeholder='Gebruikersnaam' />
				<input type='password' name='wachtwoord' size='30' class = 'input' placeholder='Wachtwoord' />
				<div class = 'invulError'>${errorMsg} </div>				
				<input type='submit' name='submit' id='submit' class = 'btn btn-block btn-primary input'value='aanmelden' />
				<div class = 'loader'></div>
		</form>
		</div>
	</div>
</body>
</html>
