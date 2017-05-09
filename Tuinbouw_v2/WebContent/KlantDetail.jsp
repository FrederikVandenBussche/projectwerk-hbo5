<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tuinbouwbedrijf Hitek</title>
	<link rel="stylesheet" type="text/css" href="style/style.css">
	<link rel="stylesheet" type="text/css" href="style/lijst.css">
	<link href = "style/bootstrap.min.css" rel = "stylesheet">      
    <script type="text/javascript" src="script/jquery-2.1.3.min.js"></script>
    <script  type="text/javascript" src = "script/bootstrap.min.js"></script>
	<script type="text/javascript" src="script/adresScript.js"></script>
	<script type="text/javascript" src="script/legeTabelVerbergen.js"></script>
	<script type="text/javascript" src="script/nieuweKlant.js"></script>
	

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
			<div id = "klantAanspreekNaam">
				Details van klant: ${aanspreeknaam }. 
			</div>
			<div class="invulError">${inputValidatieErrorMsg }</div>
			<ul id = "myTab" class = "nav nav-tabs">
           	<li class = "active">
               	<a href = "#gegevens" data-toggle = "tab">Klantgegevens</a>
           	</li>
           	<li><a href = "#adressen" data-toggle = "tab">Adres</a></li>	
        </ul>
        <div id = "myTabContent" class = "tab-content">
            	<div class = "tab-pane fade in active" id = "gegevens">
            		<br />
           	    	<form action="klantOpslaan" method="post">
						<input type="hidden" name="id" value="${id }" />
						<input type="hidden" name="variabelVeldnaam1" value="${variabelVeldnaam1 }" />
							<!--  Via het hidden veld "variabelVeldnaam1 wordt bekeken of het om een 
							-- ParticulierKlant of een BedrijfKlant gaat -->
						<div class = "inputvelden form-group">
							<label class="control-label col-sm-3" for="variabelVeld1"> ${variabelVeldnaam1 } : </label>
           					<div class="col-sm-9">          
        					    <input type="text" class="form-control" name="variabelVeld1" value="${variabelVeld1 }">
  					      	</div>
  					      	<label class="control-label col-sm-3" for = "variabelVeld2"> ${variabelVeldnaam2 } : </label>
  					      	<div class="col-sm-9">  
  					      		<input class="form-control" type="text" name="variabelVeld2" value="${variabelVeld2 }" />
  					      	</div>
  					      	<div class = "col-sm-offset-3 col-sm-9">
  					      		<input type="submit" class = "btn btn-default active" name="submit" value="Opslaan" />
  					      </div>
						</div>
					</form>
            	</div>
            	<div class = "tab-pane fade" id = "adressen">
            		<br />
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
											<input type="submit" class = "btn btn-default" name="submit" value="Verwijder" />
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
									<input type="hidden" name="aanspreeknaam" value="${aanspreeknaam}" />
									<input type="hidden" name="klant_id" value="${id}" />
								
									<input type="hidden" name="variabelVeldnaam1" value="${variabelVeldnaam1 }" />
									<input type="hidden" name="variabelVeld1" value="${variabelVeld1 }" />
									<input type="hidden" name="variabelVeldnaam2" value="${variabelVeldnaam2 }" />
									<input type="hidden" name="variabelVeld2" value="${variabelVeld2 }" />
									<div class="col-sm-offset-2 col-sm-10">
										<input type="submit" class = "btn btn-default active" name="submit" value="Voeg nieuw adres toe" />
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
            </div>
            <fieldset id="exportatie">
				<legend>Exporteer alle prestaties bij deze klant</legend>
				<form action="exporteerPrestaties" method="get">
					<label for = "opdracht"> Exporteer opdracht: </label>
					<select name = "opdracht">
						<option value="0">Alle opdrachten</option>
							<c:forEach items="${opdrachtMap }" var="element">
								<option value="${element.key }">
									${element.value }
							</c:forEach>
						</option>
					</select>
					<br />
					<label for = "begindatum0"> Begindatum: </label>
					<input type="date" name="begindatum" />
					<br />
					<label for = "einddatum"> Einddatum: </label>
					<input type="date" name="einddatum"/>
					<br />
					<input type="submit" class = "btn btn-primary btn-block active " name="submit" value="Exporteer als Excell-file" />
				</form>
			</fieldset>
		</div>
	</div>
</body>
</html>