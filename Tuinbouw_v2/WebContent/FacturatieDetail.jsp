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
	<script type="text/javascript" src="script/legeTabelVerbergen.js"></script>
	
	
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
			<div id="wieIsWaarMenu">
				<form action="wieIsWaarMenu" method="get">
					<input type="submit" name="submit" value="Wie is waar?" />
				</form>
			</div>
		</div>
		<div id="content">
			<br />
			<form action="genereerPdf" method = "post" target="_blank" >
				Klantnaam: ${factuur.klantNaam }.
				<br />
				<label for = "adres"> Verzend de factuur naar het adres: </label> 
				<select name="adres">
					<c:forEach items="${adresMap }" var="adres">
						<option value="${adres.key }">
							${adres.value }
						</option>
					</c:forEach>
				</select>	
				<ul id = "myTab" class = "nav nav-tabs">
            <li class = "active">
                <a href = "#werkuren" data-toggle = "tab">
                    Overzicht werkuren
                </a>
            </li>
            <li><a href = "#verplaatsingen" data-toggle = "tab">Overzicht verplaatsingskosten</a></li>
            <li><a href = "#materialen" data-toggle = "tab">Overzicht gebruikte materialen</a></li>	
        </ul>

        <div id = "myTabContent" class = "tab-content">
            <div class = "tab-pane fade in active" id = "werkuren">
                <fieldset>
					<legend>Overzicht werkuren</legend>
					<c:forEach items="${factuur.opdrachtLijst }" var="opdracht">
						Opdracht: ${opdracht.opdrachtNaam }.
						
						<div class="tabel">
							<table class = "table table-striped table-hover">
								<thead>
								<tr>
									<th>Taak</th>
									<th>Datum</th>
									<th>&#x23; uren</th>						
								</tr>
								</thead>
								<tbody>
								<c:forEach items="${opdracht.taakLijst }" var="taak">
									<tr>
										<td>${taak.taakNaam }</td>
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
											
										</tr>
									</c:forEach>
								</c:forEach>
								</tbody>
							</table>
						</div>
					</c:forEach>
				</fieldset>
            </div>
            <div class = "tab-pane fade" id = "verplaatsingen">
                <fieldset>
					<legend>Overzicht verplaatsingskosten</legend>
					<div class="tabel">
						<table class = 'table table-striped table-hover'>
							<thead>
							<tr>
								<th>Dag	</th>
								<th>aantalKm</th>
								<th>aantal verplaatsingen</th>					
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${factuur.verplaatsingLijst }" var="verplaatsing">
								<tr>
									<td>
										<fmt:formatDate value="${verplaatsing.dag }" pattern="dd/MM/yyyy"/>
									</td>
									<td>${verplaatsing.aantalKm }</td>
									<td>${verplaatsing.aantalVerplaatsingen }</td>
									
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</fieldset>
            </div>
             <div class = "tab-pane fade" id = "materialen">
                <fieldset>
					<legend>Overzicht gebruikte materialen</legend>
					<div class="tabel">
						<table class = "table table-striped table-hover">
							<thead>
							<tr>
								<th>soort</th>
								<th>naam</th>
								<th>aantal</th>
								<th>eenheidsmaat</th>
								<th>eenheidsprijs</th>
								<th></th>
							</tr>
							</thead>
							<tbody>
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
							</tbody>
						</table>
					</div>
				</fieldset>
            </div>
        </div>
				
				
				
				
				
				<input type="submit" class = "btn btn-primary btn-lg btn-block" name="submit" value="download als pdf" />
			</form>
		</div>
	</div>

</body>
</html>