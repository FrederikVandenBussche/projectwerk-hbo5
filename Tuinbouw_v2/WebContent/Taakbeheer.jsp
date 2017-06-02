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
	<script type="text/javascript" src="script/nieuweTaak.js"></script>
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
			<br />
			<div id="taakomschrijving">
				<form action="taakOpslaan" method="post" >
					Taak: In opdracht van ${klantNaam } 
					<div class="invulError">${inputValidatieErrorMsg }</div>
					<br />
					<fieldset>
						<legend>
							Onderdeel van de opdracht : ${opdrachtNaam }
						</legend>
						<br />
						<div class="inputvelden form-group">
							<label class="control-label col-sm-2" for = "taaknaam">naam: </label>
							 <div class="col-sm-10">
							 	<input type="text" class="form-control" id="taaknaam" name="taaknaam" value ="${taak.taakNaam }" />
							 </div>
							 <label class="control-label col-sm-2" for = "opmerking">opmerking: </label>
							 <div class="col-sm-10">
							 	<textarea class="form-control" rows="4" cols="50" name="opmerking">${taak.opmerking }</textarea>
							 </div>
							 <div class=" col-sm-offset-2 col-sm-10">
							 	<input type="hidden" name = "taakId" value = "${taak.id }" />
							 	<input type="submit" class = "btn btn-default active" name="submit" value="opslaan" />
							 </div> 
						</div>
					</fieldset>
				</form>
			</div>
			<div id = "werknemerOpdrachtTaak">
				<ul id = "myTab" class = "nav nav-tabs">
            		<li class = "active">
            		    <a href = "#planning" data-toggle = "tab">
            		        Planning
            		    </a>
            		</li>
            		<li><a href = "#gewerkteUren" data-toggle = "tab">Gewerkte uren</a></li>	
        		</ul>
	        	<div id = "myTabContent" class = "tab-content">
	            	<div class = "tab-pane fade in active" id = "planning">
	            	    <form action="taakPlanningToevoegen" method="post">
	            	    	<div class = "inputvelden form control">
	            	    		<label for = "werknemer">Voeg een nieuwe werknemer toe : </label>
	                			<div >
	                				<select name = "werknemer">
										<c:forEach items="${werknemerMap }" var="werknemer">
											<option value="${werknemer.key }">
												${werknemer.value }
											</option>							
										</c:forEach>
									</select>
    	            			</div>
        	        			<label for = "datum">Plan een nieuwe datum in: (dd/mm/yyyy) </label>
           		     			<div >
            	    				<input type="date" name="datum" />
            	    			</div>
                				<div >
                					<input type="hidden" name = "taakId" value = "${taak.id }" />
                					<input type="submit" class = "btn btn-default active" name="submit" value="Voeg toe" />	
                				</div>
                			</div>
						</form>
						<br />
						<br />
						Volgende werknemer staan reeds gepland:
						<table class = "table table-striped table-hover">
							<thead>
								<tr>
									<th> naam </th>
									<th> datum </th>
									<th> </th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${taak.planningLijst }" var="planning">
									<tr>
										<td>${planning.werknemer } </td>
										<td>
											<fmt:formatDate value="${planning.beginuur }" pattern="dd/MM/YYYY"/> 
										</td>
										<td>
											<form action="taakGeplandeWerknemerVerwijderen" method="get">
												<input type="hidden" name="planningId" value="${planning.id }" />
												<input type="hidden" name="taakId" value = "${taak.id }" />
												<input type="submit" class = "btn btn-default" name="submit" value="verwijder" /> 
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
           			 </div>
            		<div class = "tab-pane fade" id = "gewerkteUren">
                		<table class = "table table-striped table-hover">
							<thead>
								<tr>
									<th>Naam Werknemer </th>
									<th>Beginuur</th>
									<th>Einduur</th> <!--  een String -->
									<th>isAanwezig</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${taak.gewerkteUrenLijst }" var="element">
									<tr>
										<td>${element.werknemer }</td>
										<td>
											<fmt:formatDate value="${element.beginuur }" pattern="dd/MM/YYYY HH:mm" />
										</td>
										<td>
											<fmt:formatDate value="${element.einduur }" pattern="dd/MM/YYYY HH:mm" />
										</td>
										<td>${element.isAanwezig }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
        	    	</div>
        		</div>
        	</div>
        	<div id= "bijNieuwVerbergen">
				<form action="taakMarkerenAlsAfgewerkt" method="post" >
					<input type="hidden" name="taakId" value = "${taak.id }" />
					<input type="submit" class = "btn btn-default btn-lg btn-block" name="submit" value="Deze taak als 'afgewerkt' markeren." />
				</form>
			</div>
		</div>
	</div>
</body>
</html>