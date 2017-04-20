<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Tuinbouwbedrijf Hitek</title>
	<link rel="stylesheet" type="text/css" href="style/style.css">
	<link rel="stylesheet" type="text/css" href="style/lijst.css">
</head>
<body>
	<!--  taglib om jstl expression language te gebruiken -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ page isELIgnored="false"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
					<input type="submit" name="submit" value="materialen" />
				</form>
			</div>
			<div id="personeelMenu" class="actiefItem">
				<form action="personeelMenu" method="get">
					<input type="submit" name="submit" value="personeel" />
				</form>
			</div>
			<div id="bedrijfsgegevensMenu">
				<form action="bedrijfsgegevensMenu" method="get">
					<input type="submit" name="submit" value="bedrijfsgegevens" />
				</form>
			</div>
		</div>
		<div id="content">
			<div>
				Mijn personeel
			</div>
			<div>
				<form action = "personeelToonDetail" method="get">
					<!--  if(id == -1){nieuw personeelslid} -->
					<input type="hidden" name="id" value="-1" />
					<input type="submit" name="submit" value="Voeg een nieuw personeelslid toe" />
				</form>
			</div>
			
			<div class="groteTabel">
				<table>
					<tr>
						<th>Voornaam</th>
						<th>Familienaam</th>
						<th>loon</th>
						<th>aanwervingsdatum</th>
						<th>takenlijst</th>
						<th>Details</th>
					</tr>
					<c:forEach items="${lijst}" var="element">
						<tr>
							<td>${element.voornaam}</td>
							<td>${element.naam}</td>
							<td>${element.loon }</td>
							<td>
								<fmt:formatDate value="${element.aanwervingsdatum}" pattern="dd/MM/YYYY" />
							</td>
							<td>
								<form action="personeelToonTakenlijst" method="get">
									<input type="hidden" name="id" value=${element.persoonId}></input>
									<input type="hidden" name="werknemerId" value=${element.werknemerId } ></input>
									<input type="submit" name="submit" value="takenlijst" />
								</form>
							</td>
							<td>
								<form action="personeelToonDetail" method="get">
									<input type="hidden" name="id" value=${element.persoonId}></input>
									<input type="submit" name="submit" value="meer..." />
								</form>
							</td>
						</tr>
					</c:forEach>
					
					
				</table>
			</div>
		</div>
	</div>

</body>
</html>