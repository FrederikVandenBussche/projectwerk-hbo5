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
				Gegevens van ${aanspreeknaam}.
			</div>
			<form action="personeelslidOpslaan" method="post">
				<input type="hidden" name="id" value="${id}"/>
				<fieldset>
					<legend>Gegevens</legend>
					Voornaam: <input type="text" name="voornaam" value="${personeelslid.voornaam }" />
					Naam: <input type="text" name="naam" value="${personeelslid.naam }"/>
					<br />
					Loon: <input type="number" name="loon" value="${personeelslid.loon }" /> 
					email: <input type="email" name="email" value="${personeelslid.email }"/>
					<br />
					Geboortedatum 
					<fmt:formatDate value="${personeelslid.geboortedatum }" pattern="dd/MM/yyyy" />
					wijzigen: 
					<input type="date" name="nieuweGeboortedatum" >
					<br />
					Aanwervingsdatum
					<fmt:formatDate value="${personeelslid.aanwervingsdatum }" pattern="dd/MM/yyyy" />
					wijzigen: 
					<input type="date" name="nieuweAanwervingsdatum" >
					<br />
				</fieldset>
				
				<input type="hidden" name="geboortedatum" value="${personeelslid.geboortedatum }"/>
				<input type="hidden" name="aanwervingsdatum" value="${personeelslid.aanwervingsdatum }" />
				<input type="submit" name="opslaan_btn" value="${buttonNaam }" /> 				
			</form>
	
			<fieldset>
				<legend>Adressen</legend>
				<div class="adresLijst">
						<c:forEach items="${personeelslid.adreslijst}" var="element">
							<div>
								<form action="personeelAdresVerwijderen" method="post">
									<div class="horizontaleDivs">
										<div>
											straat: ${element.straat }  
											nr: ${element.nummer } 
											bus: ${element.bus } 
											<br />
											postcode: ${element.postcode } 
											plaats: ${element.plaats }
											<br />
											<br />
											<input type="hidden" name="buttonNaam" value="${buttonNaam }" />
											<input type="hidden" name="aanspreeknaam" value="${aanspreeknaam}" />
											<input type="hidden" name="adres_id" value=${element.id } />
											<input type="hidden" name="personeel_id" value="${id }" />
											<input type="submit" name="submit" value="Verwijder" />
										</div>
										<div class="staticmap">
											<a href="${element.googlemap }" target="_blank">
												<img src="${element.staticmap }" alt="kaart van dit adres">
											</a>
										</div>
									</div>
								</form>
							</div>
						</c:forEach>
						<div>
							<form action="personeelAdresOpslaan" method="post">
								<div>
									straat: <input type="text" name="straat" />
									nr: <input type="number" name="nr" size="5" />
									bus: <input type="text" name="bus" size="5" />
									<br />
									postcode: <input type="number" name="postcode" size="5" />
									plaats: <input type="text" name="plaats" />	
								</div>
								<input type="hidden" name="buttonNaam" value="${buttonNaam }" />
								<input type="hidden" name="aanspreeknaam" value="${aanspreeknaam}" />
								<input type="hidden" name="personeel_id" value="${id}" />
								<br />
								<br />
								<input type="submit" name="submit" value="Voeg nieuw adres toe" />
								<br />
								<br />
								<br />
								<br />
							</form>
							
						</div>
					</div>
				</fieldset>
				
				<form action="personeelslidVerwijderen" method="post" >
				
					<input type="hidden" name="id" value="${id}" />
					<input type="submit" name="verwijder_btn" value="Verwijder dit personeelslid" />
				</form>
		</div>
	</div>

</body>
</html>