<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Tuinbouwbedrijf Hitek</title>
	<link rel="stylesheet" type="text/css" href="style/style.css">
	<link rel="stylesheet" type="text/css" href="style/lijst.css">
	<link  rel = "stylesheet" href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">      
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="script/legeTabelVerbergen.js"></script>
	<script type="text/javascript" src="script/tabKiezer.js"></script>
</head>
<body>
	<!--  taglib om jstl expression language te gebruiken -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ page isELIgnored="false"%>
	
	<div id="container">
		<input type="hidden" id="tabKiezer" value="${tabKiezer }" />
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
					<input type="submit" name="submit" value="Materialen" />
				</form>
			</div>
			<div id="personeelMenu">
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
			<div class="invulError">${inputValidatieErrorMsg }</div>
			<br />
			<fieldset>
				<legend>Nieuw materiaal/wijzig materiaal</legend>
				<form action="materiaalOpslaan" method="post" >
					<input type="hidden" name="materiaalId" value="${materiaal.id }" />
					<label for = "naam"> Naam: </label>
					<input type="text" name="naam" value="${materiaal.naam }" />
					<label for="typeMateriaal">type materiaal: </label>
					<select name = "typeMateriaalId">
						<option value="${materiaal.soortId }" selected>${materiaal.soort }</option>
						<c:forEach items="${typeMateriaalLijst }" var="element">
							<option value="${element.id }">${element.naam }</option>
						</c:forEach>
					</select>
					<br />
					<label for = "eenheidsprijs"> Eenheidsprijs: </label>
					<input type="number" step="0.01" pattern="[0-9]+([\.,][0-9]+)?" 
						name="eenheidsprijs" value="${materiaal.eenheidsprijs }" />
					<label for = "eenheidsmaat"> Eenheidsmaat: </label>
					<input type="text" name="eenheidsmaat" value="${materiaal.eenheidsmaat }" />
					<br />
					<input type="submit" class = "btn btn-default active" name="submit" value="toevoegen / wijzigingen opslaan" />
				</form>
			</fieldset>
			<fieldset>
				<legend>Overzicht materialen per categorie</legend>
				<ul id="myTab" class = "nav nav-tabs">
					<c:forEach items="${typeMateriaalLijst }" var="element" varStatus = "status">
						<li id="li${element.naam }"class = ${status.first ? 'active' : '' }>
							<a href = "#${element.naam }" data-toggle = "tab">${element.naam } </a>
						</li>
					</c:forEach>
					<li>
						<a href ="#nieuweSoort" data-toggle = "tab">Voeg nieuwe soort toe</a>
					</li>
				</ul>
				<div id="myTabContent" class="tab-content">
					<c:forEach items="${typeMateriaalLijst }" var="element" varStatus = "status">
						<div class = "tab-pane fade ${status.first ? ' in active' : '' }" id="${element.naam }">
							<table class="table table-striped table-hover">
								<thead>
									<tr>
										<th>Naam</th>
										<th>Eenheidsprijs</th>
										<th>Eenheidsmaat</th>
									</tr>
								</thead>
								<c:forEach items="${element.materiaalLijst }" var="materiaal">
									<tr>
										<td>${materiaal.naam }</td>
										<td>${materiaal.eenheidsprijs }</td>
										<td>${materiaal.eenheidsmaat }</td>
										<td>
											<form action="materiaalWijzigen" method="get">
												<input type="hidden" name="typeMateriaalId" value=${element.id } />
												<input type="hidden" name="materiaalId" value=${materiaal.id } />
												<input type="submit" class = "btn btn-default" name="submit" value="Wijzigen" />
											</form>
										</td>
										<td>
											<form action="materiaalVerwijderen" method="post">
												<input type="hidden" name="typeMateriaalId" value=${element.id } />
												<input type="hidden" name="materiaalId" value=${materiaal.id } />
												<input type="submit" class = "btn btn-default" name="submit" value="Verwijderen" />
											</form>
										</td>
									</tr>
								</c:forEach>
							</table>
							<form action="typeMateriaalVerwijderen" method="post">
								<input type="hidden" name="typeMateriaalId" value=${element.id } />
								<input type="submit" class = "btn btn-default btn-block btn-lg" name="submit" value="De categorie '${element.naam }' en alle bijhorende materialen verwijderen" />
							</form>
						</div>
					</c:forEach>
					<div class = "tab-pane fade" id="nieuweSoort">
						<form action="typeMateriaalToevoegen" method="post">
							<div class="inputvelden form-group">
								<label class = "control-label col-sm-2" for = "naam"> Naam: </label>
								<div class="col-sm-10">
									<input type="text" name="naam" />
								</div>
								<div class="col-sm-offset-2 col-sm-10">
									<input type="submit" class = "btn btn-default active" name="submit" value="opslaan" />
								</div>
							</div>
						</form>
					</div>
				</div>
			</fieldset>
		</div>
	</div>
</body>
</html>