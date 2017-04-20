<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Tuinbouwbedrijf Hitek</title>
	<link rel="stylesheet" type="text/css" href="style/style.css">
</head>
<body>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
			<div id="facturatieMenu" class="actiefItem">
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
			Facturatie
			
			<form action="genereerPdf" method = "post" target="_blank" >
				<fieldset>
					<legend>Klantgegevens</legend>
					Klantnaam: ${factuur.klantNaam }.
					<br />
					Verzend de factuur naar het adres: 
					<select name="adres">
						<c:forEach items="${adresMap }" var="adres">
							<option value="${adres.key }">
								${adres.value }
							</option>
						</c:forEach>
					</select>	
				</fieldset>
				
				<fieldset>
					<legend>Overzicht werkuren</legend>
					<c:forEach items="${factuur.opdrachtLijst }" var="opdracht">
						Opdracht: ${opdracht.opdrachtNaam }.
						
						<div class="tabel">
							<table>
								<tr>
									<th>Taak</th>
									<th>Datum</th>
									<th>&#x23; uren</th>
									<th></th>						
								</tr>
								<c:forEach items="${opdracht.taakLijst }" var="taak">
									<tr>
										<td>${taak.taakNaam }</td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<c:forEach items="${taak.planningLijst }" var="planning">
										<tr>
											<td></td>
											<td>
												<fmt:formatDate value="${planning.beginuur }" pattern="dd/MM/yyyy"/>
											</td>
											<td>${planning.aantalUren }</td>
											<td>Verwijder</td>
										</tr>
									</c:forEach>
								</c:forEach>
							</table>
						</div>
					</c:forEach>
				</fieldset>
				
				<fieldset>
					<legend>Overzicht verplaatsingskosten</legend>
					<div class="tabel">
						<table>
							<tr>
								<th>Dag	</th>
								<th>aantalKm</th>
								<th>aantal verplaatsingen</th>
								<th></th>						
							</tr>
							<c:forEach items="${factuur.verplaatsingLijst }" var="verplaatsing">
								<tr>
									<td>
										<fmt:formatDate value="${verplaatsing.dag }" pattern="dd/MM/yyyy"/>
									</td>
									<td>${verplaatsing.aantalKm }</td>
									<td>${verplaatsing.aantalVerplaatsingen }</td>
									<td></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</fieldset>
				
				<fieldset>
					<legend>Overzicht gebruikte materialen</legend>
					<div class="tabel">
						<table>
							<tr>
								<th>soort</th>
								<th>naam</th>
								<th>aantal</th>
								<th>eenheidsmaat</th>
								<th>eenheidsprijs</th>
								<th></th>
							</tr>
							<c:forEach items="${factuur.opdrachtLijst }" var="opdracht">
								<c:forEach items="${opdracht.gebruiktMateriaalLijst }" var="materiaal">
									<tr>
										<td>${materiaal.soort }</td>
										<td>${materiaal.naam }</td>
										<td>${materiaal.hoeveelheid }</td>
										<td>${materiaal.eenheidsmaat }</td>
										<td>${materiaal.eenheidsprijs }</td>
									</tr>								
								</c:forEach>
							</c:forEach>
						</table>
					</div>
				</fieldset>
				<input type="submit" name="submit" value="download als pdf" />
			</form>
		</div>
	</div>

</body>
</html>