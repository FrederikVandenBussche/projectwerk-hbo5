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
				 		<input type="submit" name="submit" value=" " >
				 	</form>
					<!--  author credits for 'logout.png': 
						Icon made by Freepik from www.flaticon.com 
					 -->
				 </div>
			</div>
			<div id="opdrachtMenu">
				<form action="opdrachtenMenu" method="get">
					<input type="submit" name="submit" value="Opdrachten" >
				</form>
			</div>
			<div id="klantenMenu" class="actiefItem">
				<form action="klantenMenu" method="get">
					<input type="submit" name="submit" value="Klanten" >
				</form>
			</div>
			<div id="facturatieMenu">
				<form action="facturatieMenu" method="get">
					<input type="submit" name="submit" value="Facturatie" >
				</form>
			</div>
			<div id="materialenMenu">
				<form action="materialenMenu" method="get">
					<input type="submit" name="submit" value="materialen" >
				</form>
			</div>
			<div id="personeelMenu">
				<form action="personeelMenu" method="get">
					<input type="submit" name="submit" value="personeel" >
				</form>
			</div>
			<div id="bedrijfsgegevensMenu">
				<form action="bedrijfsgegevensMenu" method="get">
					<input type="submit" name="submit" value="bedrijfsgegevens" >
				</form>
			</div>
		</div>
		<div id="content">
			Klantbeheer.
			<br />	
			<div class="horizontaleDivs">
				<div id="particuliereLijst" class="groteTabel">
					<fieldset>
					<legend>Lijst met particuliere klanten.</legend>
					<div>
					<form action="klantToonDetail" method="get">
						<!--  if(id == -1){nieuwe klant} -->
						<input type="hidden" name="id" value="-1" />
						<input type="submit" name="particulier" value="Voeg particuliere klant toe" />
					</form>	
				</div>
					
					<br />
					<table>
						<tr>
							<th>Naam</th>
							<th>Voornaam</th>
							<th>Details</th>
						</tr>

						<c:forEach items="${particulierLijst}" var="element">
							<tr>
								<td>${element.naam }</td>
								<td>${element.voornaam }</td>
								<td>
									<form action="klantToonDetail" method="get">
										<input type="hidden" name="id" value="${element.id }" />
										<input type="submit" name="particulier" value="meer..."/>
									</form>
								</td>
							</tr>
						</c:forEach>
					</table>
					</fieldset>
				</div>
				<div id="bedrijfLijst" class="groteTabel">
				<fieldset>
				<legend>Lijst met bedrijf klanten.</legend>
					<div>
					<form action="klantToonDetail" method="get">
						<!--  if(id == -1){nieuwe klant} -->
						<input type="hidden" name="id" value="-1" />
						
						<input type="submit" name="bedrijf" value="Voeg bedrijf klant toe" />
					</form>	
				</div>
					<br />
					<table>
						<tr>
							<th>Bedrijfnaam</th>
							<th>Btw Nummer</th>
							<th>Details</th>
						</tr>
						<c:forEach items="${bedrijfLijst }" var="element">
							<tr>
								<td>${element.bedrijfnaam }</td>
								<td>${element.btwNummer }</td>
								<td>
									<form action="klantToonDetail" method="get">
										<input type="hidden" name="id" value="${element.id }" />
										<input type="submit" name="bedrijf" value="meer..."/>
									</form>
								</td>
							</tr>
						</c:forEach>
					</table>
					</fieldset>
				</div>
				
				</div>
			</div>
			
		</div>
	</div>

</body>
</html>