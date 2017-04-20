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
			<div id="materialenMenu" class="actiefItem">
				<form action="materialenMenu" method="get">
					<input type="submit" name="submit" value="materialen" />
				</form>
			</div>
			<div id="personeelMenu">
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
			Materiaal 
			<br />
			<fieldset>
				<legend>Nieuw materiaal/wijzig materiaal</legend>
					<form action="materiaalOpslaan" method="post" >
						<input type="hidden" name="id" value="${materiaal.id }" />
						Soort: <input type="text" name="soort" value="${materiaal.soort }" />
						Eenheidsmaat: <input type="text" name="eenheidsmaat" value="${materiaal.eenheidsmaat }" />
						<br />
						Naam: <input type="text" name="naam" value="${materiaal.naam }" />
						Eenheidsprijs: <input type="number" step="0.01" pattern="[0-9]+([\.,][0-9]+)?" name="eenheidsprijs" value="${materiaal.eenheidsprijs }" />
						
						<input type="submit" name="submit" value="opslaan" />
						<br />
						
					</form>
			</fieldset>
			<div class="groteTabel">
			<table>
				<tr>
					<th>Soort</th>
					<th>Naam</th>
					<th>Eenheidsmaat</th>
					<th>Eenheidsprijs</th>
				</tr>
				<c:forEach items="${lijst }" var="element">
					<tr>
						<td>${element.soort }</td>
						<td>${element.naam }</td>
						<td>${element.eenheidsmaat }</td>
						<td>${element.eenheidsprijs }</td>
						<td>
							<form action="materiaalWijzigen" method="get">
								<input type="hidden" name="id" value=${element.id } />
								<input type="submit" name="submit" value="Wijzigen" />
							</form>
						</td>
						<td>
							<form action="materiaalVerwijderen" method="post">
								<input type="hidden" name="id" value=${element.id } />
								<input type="submit" name="submit" value="Verwijderen" />
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