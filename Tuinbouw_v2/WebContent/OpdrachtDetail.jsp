<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tuinbouwbedrijf Hitek</title>
<link rel="stylesheet" type="text/css" href="style/style.css">
<link rel="stylesheet" type="text/css" href="style/lijst.css">

<script type="text/javascript" src="script/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="script/legeTabelVerbergen.js"></script>
<script type="text/javascript" src="script/nieuweOpdracht.js"></script>




</head>
<body>
	<!--  taglib om jstl expression language te gebruiken -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
			<div id="opdrachtMenu" class="actiefItem">
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
			<fieldset>
				<legend id="opdrachtAanspreekNaam">Opdracht ${opdrachtDetailData.aanspreeknaam } </legend>
				<form action="opdrachtOpslaan" method="post">
					<div class="inlogError">${inputValidatieErrorMsg }</div>
					In opdracht van : ${opdrachtDetailData.opdracht.klantNaam } 
					<label for = "klanten">	${opdrachtDetailData.variabelveld1 }</label> 
					<select name="klanten">
						<option value="${opdrachtDetailData.opdracht.klantNaam }" selected>${opdrachtDetailData.opdracht.klantNaam }</option>
						<option></option>
						<c:forEach items="${opdrachtDetailData.klantNaamMap.values() }"
							var="klantNaam">
							<option value="${klantNaam }">${klantNaam }</option>
						</c:forEach>
					</select> 
					<br /> 
					<br />
					<div class="horizontaleDivs">
						<div>
							<label for = "opdrachtNaam"> Opdracht naam: </label>
							<input type="text" name="opdrachtNaam" 
								value="${opdrachtDetailData.opdracht.opdrachtNaam}" /> 
							<br />
							<label for = "nieuweStartDatum">
								StartDatum:
								<fmt:formatDate value="${opdrachtDetailData.opdracht.startDatum }"
									pattern="dd/MM/YYYY" />
								${opdrachtDetailData.variabelveld2 } 
							</label>
							<input type="date" name="nieuweStartDatum"> 
							<br /> 
							<label for = "nieuweEindDatum">
							EindDatum:
								<fmt:formatDate value="${opdrachtDetailData.opdracht.eindDatum }"
									pattern="dd/MM/YYYY" />
								${opdrachtDetailData.variabelveld2 }
							</label> 
							<input type="date" name="nieuweEindDatum"> 
							<br /> 
							Adres:
							${opdrachtDetailData.adresString } 
							<br />
						</div>
						<div class="staticmap">
							<a href="${opdrachtDetailData.googlemap }" target="_blank"> 
								<img src="${opdrachtDetailData.staticmap }">
							</a>
						</div>
					</div>
					Kies een ander adres uit de adreslijst van
					${opdrachtDetailData.opdracht.klantNaam } 
					<br /> 
					<input type="submit" name="submit" value="Adreslijst herladen" /> 
					<select	name="adressen">
						<option value="" selected>Wijzig adres</option>
						<c:forEach items="${opdrachtDetailData.adresMap.values() }" var="adresString">
							<option value="${adresString }">${adresString }</option>
						</c:forEach>
					</select> 
					<br /> 
					<input type="hidden" name="id" value="${opdrachtDetailData.opdracht.id }" /> 
					<input type="submit" name="submit" value="${opdrachtDetailData.buttonNaam }" />
				</form>
			</fieldset>
			<div class="horizontaleDivs">
				<div id="voegNieuweTaakToe">
					<form action="taakToonDetail" method="get">
						<!--  voor een nieuwe taak is de id voorlopig -1 -->
						<input type="hidden" name="id" value="-1" /> 
						<input type="submit" name="submit" value="Voeg een nieuwe taak toe" />
					</form>
				</div>
			</div>

			<fieldset id="takenlijst">
				<legend>Takenlijst</legend>
				<table>
					<tr>
						<th>Naam</th>
						<th>vooruitgang</th>
						<th>Status</th>
						<th>Opmerking</th>
						<th></th>
						<th></th>
					</tr>
					<c:forEach items="${opdrachtDetailData.opdracht.taakLijst }"
						var="element">
						<tr>
							<td>${element.taakNaam }</td>
							<td class="progressbar">
								<progress value="${element.vooruitgangPercentage } " max="100"></progress>
							</td>
							<td>${element.status }</td>
							<td>${fn:substring(element.opmerking, 0, 20 ) }</td>
							<td>
								<form action="taakToonDetail" method="get">
									<input type="hidden" name="id" value="${element.id }" /> 
									<input type="submit" name="submit" value="Details" />
								</form>
							</td>
							<td>
								<form action="taakVerwijderen" method="post">
									<input type="hidden" name="id" value="${element.id }" /> 
									<input type="submit" name="submit" value="Verwijderen" />
								</form>
							</td>
						</tr>
					</c:forEach>
				</table>
			</fieldset>
			<div class="horizontaleDivs">
				<fieldset id="voegGebruiktMateriaalToe">
					<legend>Voeg gebruikt materiaal toe</legend>
					<form action="opdrachtMateriaalToevoegen" method="post">
						<label for = "materialen"> Naam: </label> 
						<select name="materialen">
							<c:forEach items="${opdrachtDetailData.materiaalLijst }" var="materiaal">
								<option value="${materiaal.id }">${materiaal.naam }</option>
							</c:forEach>
						</select>
						<label for ="hoeveelheid"> Hoeveelheid: </label> 
						<input type="number" name="hoeveelheid" />
						${materiaal.eenheidsmaat }
						<input type="submit" name="submit" value="Voeg toe" />
					</form>
				</fieldset>
				<fieldset id="gebruiktMateriaal">
					<legend>Gebruikte materialen</legend>
					<table>
						<tr>
							<th>Soort</th>
							<th>Naam</th>
							<th>Hoeveelheid</th>
							<th>Eenheidsmaat</th>
							<th>Eenheidsprijs</th>
							<th></th>
						</tr>
						<c:forEach
							items="${opdrachtDetailData.opdracht.gebruiktMateriaalLijst }"
							var="materiaal">
							<tr>
								<td>${materiaal.soort }</td>
								<td>${materiaal.naam }</td>
								<td>${materiaal.hoeveelheid }</td>
								<td>${materiaal.eenheidsmaat }</td>
								<td>${materiaal.eenheidsprijs }</td>
								<td>
									<form action="opdrachtMateriaalVerwijderen" method="post">
										<input type="hidden" name="id" value="${materiaal.id }" /> 
										<input type="submit" name="submit" value="Verwijder" />
									</form>
								</td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</div>
			<div id="opdrachtVerwijderenDiv">
				<form action="opdrachtVerwijderen" method="post">
					<input type="hidden" name="id" value="${opdrachtDetailData.opdracht.id }" /> 
					<input type="submit" name="submit" value="Deze opdracht verwijderen" />
				</form>
			</div>
			
		</div>
	</div>
</body>
</html>