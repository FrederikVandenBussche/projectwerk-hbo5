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
			<div id="klantenMenu" class="actiefItem">
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
			<div>
				Details van klant: ${aanspreeknaam }.
			</div>
			<form action="klantOpslaan" method="post">
				<input type="hidden" name="id" value="${id }" />
				<!--  Via het hidden veld "variabelVeldnaam1 wordt bekeken of het om een 
				-- ParticulierKlant of een BedrijfKlant gaat -->
				<input type="hidden" name="variabelVeldnaam1" value="${variabelVeldnaam1 }" />
				<fieldset>
					<legend>Gegevens</legend>
					${variabelVeldnaam1 } :
					<input type="text" name="variabelVeld1" value="${variabelVeld1 }" />
					${variabelVeldnaam2 } :
					<input type="text" name="variabelVeld2" value="${variabelVeld2 }" />
					<br />
					
					<input type="submit" name="submit" value="opslaan" />
				</fieldset>
			</form>
			
			<fieldset>
				<legend>Adressen</legend>
				<div class="adresLijst">
						<c:forEach items="${klant.adreslijst}" var="element">
							<div>
								<form action="klantAdresVerwijderen" method="post">
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
											<input type="hidden" name="adres_id" value=${element.id } />
											<input type="hidden" name="klant_id" value="${id }" />
											<input type="hidden" name="aanspreeknaam" value="${aanspreeknaam}" />
											<input type="hidden" name="variabelVeldnaam1" value="${variabelVeldnaam1 }" />
											<input type="hidden" name="variabelVeld1" value="${variabelVeld1 }" />
											<input type="hidden" name="variabelVeldnaam2" value="${variabelVeldnaam2 }" />
											<input type="hidden" name="variabelVeld2" value="${variabelVeld2 }" />
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
							<form action="klantAdresOpslaan" method="post">
								<div>
									straat: <input type="text" name="straat" />
									nr: <input type="number" name="nr" size="5" />
									bus: <input type="text" name="bus" size="5" />
									<br />
									postcode: <input type="number" name="postcode" size="5" />
									plaats: <input type="text" name="plaats" />	
								</div>
								<input type="hidden" name="aanspreeknaam" value="${aanspreeknaam}" />
								<input type="hidden" name="klant_id" value="${id}" />
								
								<input type="hidden" name="variabelVeldnaam1" value="${variabelVeldnaam1 }" />
								<input type="hidden" name="variabelVeld1" value="${variabelVeld1 }" />
								<input type="hidden" name="variabelVeldnaam2" value="${variabelVeldnaam2 }" />
								<input type="hidden" name="variabelVeld2" value="${variabelVeld2 }" />
								<br />
								<br />
								<input type="submit" name="submit" value="Voeg nieuw adres toe" />
								<br />
								<br />
							</form>
						</div>
					</div>
				</fieldset>
				
				<fieldset>
					<legend>Exporteer alle prestaties bij deze klant</legend>
					<form action="exporteerPrestaties" method="get">
						Exporteer opdracht: 
						<select name = "opdracht">
							<option value="0">Alle opdrachten</option>
							<c:forEach items="${opdrachtMap }" var="element">
								<option value="${element.key }">
									${element.value }
								</option>
							</c:forEach>
						</select>
						<br />
						Begindatum:
						<input type="date" name="begindatum" />
						<br />
						Einddatum: 
						<input type="date" name="einddatum"/>
						<input type="submit" name="submit" value="Exporteer" />
					</form>
				</fieldset>
			
			
		</div>
	</div>

</body>
</html>