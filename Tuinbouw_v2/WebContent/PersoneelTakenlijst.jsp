<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Tuinbouwbedrijf Hitek</title>
		<link rel="stylesheet" type="text/css" href="style/style.css">   
       	<link  rel = "stylesheet" href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">      
    	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
   		<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="script/legeTabelVerbergen.js"></script>
	</head>
<body>
	<!--  taglib om jstl expression language te gebruiken -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ page isELIgnored="false"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

	<div id="container">
		<div id="nav">
			<div id="afmeldMenu">
				<img src="images/logo.png" alt="logo" id="logo">
				<!--  author credits: 
					<a href="http://www.freepik.com/free-photos-vectors/logo">Logo vector designed by Freepik</a> 
				 -->
				<div id="afmeldBtn">
					<form action="logout" method="get">
						<input type="submit" name="submit" value=" " />
					</form>
					<!--  author credits for 'logout.png': 
						Icon made by Freepik from www.flaticon.com 
					 -->
				</div>
			</div>
			<div id="opdrachtMenu">
				<form action="opdrachtenMenu" method="get">
					<input type="submit" name="submit" value="Opdrachten" />
				</form>
			</div>
			<div id="klantenMenu">
				<form action="klantenMenu" method="get">
					<input type="submit" name="submit" value="Klanten" />
				</form>
			</div>
			<div id="facturatieMenu">
				<form action="facturatieMenu" method="get">
					<input type="submit" name="submit" value="Facturatie" />
				</form>
			</div>
			<div id="materialenMenu">
				<form action="materialenMenu" method="get">
					<input type="submit" name="submit" value="Materialen" />
				</form>
			</div>
			<div id="personeelMenu" class="actiefItem">
				<form action="personeelMenu" method="get">
					<input type="submit" name="submit" value="Personeel" />
				</form>
			</div>
			<div id="bedrijfsgegevensMenu">
				<form action="bedrijfsgegevensMenu" method="get">
					<input type="submit" name="submit" value="Bedrijfsgegevens" />
				</form>
			</div>
			<div id="wieIsWaarMenu">
				<form action="wieIsWaarMenu" method="get">
					<input type="submit" name="submit" value="Wie is waar?" />
				</form>
			</div>
		</div>
		<div id="content">
			<br />
			<fieldset>
				<legend> Takenlijst van ${personeelsnaam } </legend>

				<div>
					<table class = "table table-striped table-hover">
						<thead>
						<tr>
							<th>Datum</th>
							<th>Klant</th>
							<th>Opdracht</th>
							<th>Taak</th>
							<th></th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${takenLijst }" var="element">
							<tr>
								<td><fmt:formatDate value="${element.datum }"
										pattern="dd/MM/YYYY" /></td>
								<td>${element.klantnaam }</td>
								<td>${element.opdrachtnaam }</td>
								<td>${element.taaknaam }</td>
								<td>
									<form action="personeelbeheerTaakVerwijderen" method="post">
										<input type="hidden" name="id"
											value="${element.dbWerknemerOpdrachtTaakId }"></input> <input
											type="submit" class = "btn btn-default" name="submit" value="Verwijder" />
									</form>
								</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</fieldset>

		</div>
	</div>

</body>
</html>