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
		<input type="hidden" id ="tabKiezer" value="${tabKiezer }" />
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
					<input type="submit" name="submit" value="Materialen" >
				</form>
			</div>
			<div id="personeelMenu">
				<form action="personeelMenu" method="get">
					<input type="submit" name="submit" value="Personeel" >
				</form>
			</div>
			<div id="bedrijfsgegevensMenu">
				<form action="bedrijfsgegevensMenu" method="get">
					<input type="submit" name="submit" value="Bedrijfsgegevens" >
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
			<ul id = "myTab" class = "nav nav-tabs">
        	    <li id="liparticulier" class = "active">
        	        <a href = "#particulier" data-toggle = "tab">
        	            Lijst met particuliere klanten
        	        </a>
        	    </li>
        	    <li id="libedrijf"><a href = "#bedrijf" data-toggle = "tab">Lijst met bedrijf klanten</a></li>	
        	</ul>
	        <div id = "myTabContent" class = "tab-content">
	            <div class = "tab-pane fade in active" id = "particulier">
	            	<br />
	                <div id="particuliereLijst">
						<div>
							<form action="klantToonDetail" method="get">
								<!--  if(id == -1){nieuwe klant} -->
								<input type="hidden" name="id" value="-1" />
								<input type="hidden" name="type" value="particulier" />
								<input type="submit" class = "btn btn-default btn-block active" name="particulier" value="Voeg particuliere klant toe" />
							</form>	
						</div>
						<br />
						<table class="table table-striped table-hover">
							<thead>
							<tr>
								<th>Naam</th>
								<th>Voornaam</th>
								<th>Details</th>
							</tr>
							</thead>
							<tbody>
								<c:forEach items="${particulierLijst}" var="element">
									<tr>
										<td>${element.naam }</td>
										<td>${element.voornaam }</td>
										<td>
											<form action="klantToonDetail" method="get">
												<input type="hidden" name="id" value="${element.id }" />
												<input type="hidden" name="type" value="particulier" />
												<input type="submit"  class = "btn btn-default" name="particulier" value="meer..."/>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
    	        </div>
           		<div class = "tab-pane fade" id = "bedrijf">
          			<div id="bedrijfLijst">
						<br />
						<div>
							<form action="klantToonDetail" method="get">
								<!--  if(id == -1){nieuwe klant} -->
								<input type="hidden" name="id" value="-1" />
								<input type="hidden" name="type" value="bedrijf" />
								<input type="submit" class = "btn btn-default btn-block active" name="bedrijf" value="Voeg bedrijf klant toe" />
							</form>	
						</div>
						<br />
						<table class ="table table-striped table-hover">
							<thead>
								<tr>
									<th>Bedrijfnaam</th>
									<th>Btw Nummer</th>
									<th>Details</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${bedrijfLijst }" var="element">
									<tr>
										<td>${element.bedrijfnaam }</td>
										<td>${element.btwNummer }</td>
										<td>
											<form action="klantToonDetail" method="get">
												<input type="hidden" name="id" value="${element.id }" />
												<input type="hidden" name="type" value="bedrijf" />
												<input type="submit" class = "btn btn-default" name="bedrijf" value="meer..."/>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
            	</div>
        	</div>
		</div>
	</div>
</body>
</html>