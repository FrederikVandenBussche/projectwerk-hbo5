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
	<script type="text/javascript" src="script/adresScript.js"></script> 
	<script type="text/javascript" src="script/tabKiezer.js"></script>
</head>
<body>
	<!--  taglib om jstl expression language te gebruiken -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ page isELIgnored="false"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<div id="container">
		<input type="hidden" id ="tabKiezer" value="${tabKiezer }" />
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
			<div id="personeelMenu">
				<form action="personeelMenu" method="get">
					<input type="submit" name="submit" value="Personeel" />
				</form>
			</div>
			<div id="bedrijfsgegevensMenu" class="actiefItem">
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
			<div>
				Bedrijfsgegevens ${werkgever.naam } ${werkgever.voornaam }.
				<div class = "invulError">
					${inputValidatieErrorMsg }
				</div>
			</div>
			<ul id = "myTab" class = "nav nav-tabs">
            	<li id="ligegevens" class = "active">
            	    <a href = "#gegevens" data-toggle = "tab">Gegevens</a>
            	</li>
            	<li id="liwachtwoord"><a href = "#wachtwoord" data-toggle = "tab">Wijzig Wachtwoord</a></li>	
            	<li id="liadres"><a href = "#adres" data-toggle = "tab">Adresgegevens</a></li>	
        	</ul>
	        <div id = "myTabContent" class = "tab-content">
          	  <div class = "tab-pane fade in active" id = "gegevens">
                <form action="bedrijfsgegevensWijzigen" method="post">
					<br />
					<div class = 'inputvelden form-group'>
						<label class = "control-label col-sm-2" for = "naam"> naam: </label>
						<div class="col-sm-10">
							<input type="text" name="naam" value="${werkgever.naam }"/>
						</div>
						<label class = "control-label col-sm-2" for = "voornaam"> voornaam: </label>
						<div class="col-sm-10">
							<input type="text" name="voornaam" value="${werkgever.voornaam }"/>
						</div>
						<label for = "nieuweGeboortedatum">
						Geboortedatum
						<fmt:formatDate value="${werkgever.geboortedatum }" pattern="dd/MM/yyyy" />
						wijzigen:
						</label> 
						<div class="col-sm-offset-2 col-sm-10">
							<input type="date" name="nieuweGeboortedatum" >
						</div>
						<label class = "control-label col-sm-2" for = "email" > email: </label>
						<div class="col-sm-10">
							<input type="email" name="email" value="${werkgever.email }" />
						</div>
						<label class = "control-label col-sm-2" for = "gebruikersnaam"> gebruikersnaam: </label>
						<div class="col-sm-10">
							<input type="text" name="gebruikersnaam" value="${werkgever.gebruikersnaam }"/>
						</div>
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" class = "btn btn-default active" name="submit" value="Wijzigingen opslaan" />
						</div>
					</div>
				</form>
            	</div>
            <div class = "tab-pane fade" id = "wachtwoord">
                <form action="bedrijfWachtwoordWijzigen" method="post">
                	<div class = 'inputvelden form-group'>
                		<label class = "control-label col-sm-2" for = "oud"> Oud wachtwoord: </label> 
						<div class="col-sm-10">
							<input type="password" name="oud" />
						</div>
						<label class = "control-label col-sm-2" for = "nieuw1"> Nieuw wachtwoord: </label>
						<div class="col-sm-10">
							<input type="password" name="nieuw1" />
						</div>
						<label class = "control-label col-sm-2" for = "nieuw2"> Nieuw wachtwoord: </label>
						<div class="col-sm-10">
							<input type="password" name="nieuw2" />
						</div>
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" class = "btn btn-default active" name="submit" value="Wijzig wachtwoord" />
                		</div>
                	</div>
				</form>
            </div>
            <div class = "tab-pane fade" id = "adres">
				<br />
				<ul id = "adresTab" class = "nav nav-tabs">
					<c:forEach items="${werkgever.adreslijst }" var = "element" varStatus = "status">
						<li class = ${status.first ? 'active' : '' }>
							<a href = "#${element.id }" data-toggle = "tab">
								${element.straat } ${element.nummer } ${element.bus }
								<br />
								${element.postcode } ${element.plaats }
							</a>
						</li>
					</c:forEach>
					<li>
						<a href = "#nieuwAdres" data-toggle = "tab">
							Voeg nieuw adres toe
						</a>
					</li>
				</ul>
				
				<div id = "adresContent" class = "tab-content">
					<c:forEach items = "${werkgever.adreslijst }" var = "element" varStatus = "status">
						<div class = "tab-pane fade ${status.first ? ' in active' : '' }" id = "${element.id }">
							<div>	
								<form action="bedrijfsGegevensAdresVerwijderen" method="post">
									<div class="staticmap">
										<a href="${element.googlemap }" target="_blank">
											<img src="${element.staticmap }" alt="kaart van dit adres">
										</a>
									</div>
									<div>
										<input type="hidden" name="bedrijfsnaam" value="${bedrijfsnaam}" />
										<input type="hidden" name="adres_id" value=${element.id } />
										<input type="submit" class = "btn btn-default btn-lg btn-block" name="submit" value="Verwijder dit adres" />
									</div>
								</form>
							</div>
						</div>
					</c:forEach>
					<div class = "tab-pane fade" id = "nieuwAdres">
						<div>
							<form action="bedrijfsGegevensAdresOpslaan" method="post">
									<div class = "inputvelden form-group">
										<label class = "control-label col-sm-2" for="straat">straat: </label>
										<div class = "col-sm-10">
											<input type="text" name="straat" />
										</div>
										<label class = "control-label col-sm-2" for="nr">nr: </label>
										<div class = "col-sm-10">
											<input type="number" name="nr" size="5" />
										</div>
										<label class = "control-label col-sm-2" for="bus">bus: </label>
										<div class = "col-sm-10">
											 <input type="text" name="bus" size="5" />
										</div>
										<label class = "control-label col-sm-2" for="postcode">postcode: </label>
										<div class = "col-sm-10">
											 <input type="number" name="postcode" size="5" />
										</div>
										<label class = "control-label col-sm-2" for="plaats">plaats: </label>
										<div class = "col-sm-10">
											 <input type="text" name="plaats" />	
										</div> 
										<input type="hidden" name="bedrijfsnaam" value="${bedrijfsnaam }" />
										<div class="col-sm-offset-2 col-sm-10">
											<input type="submit" class = "btn btn-default active" name="submit" value="Voeg nieuw adres toe" />
										</div>
									</div>
								</form>
							</div>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>