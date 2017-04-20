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
			Taakbeheer
			<div id="taakomschrijving">
				<form action="taakOpslaan" method="post" >
					Taak: In opdracht van ${opdrachtDetailData.opdracht.klantNaam }
					<br />
					Onderdeel van de opdracht : ${opdrachtDetailData.opdracht.opdrachtNaam }
					<br />
					naam: 
					<input type="text" name="taaknaam" value ="${taak.taakNaam }" />
					<br />
					opmerking: 
					<br />
					<textarea rows="4" cols="50" name="opmerking">${taak.opmerking }</textarea>
					<br />
					<input type="submit" name="submit" value="opslaan" />
				</form>
			</div>
			<div id="planning"></div>
				<fieldset>
					<legend>Planning</legend>
						<form action="taakPlanningToevoegen" method="post">
							Voeg een nieuwe werknemer toe : 
							<select name = "werknemer">
								<c:forEach items="${werknemerMap }" var="werknemer">
									<option value="${werknemer.key }">
										${werknemer.value }
									</option>							
								</c:forEach>
							</select>
							<br />
							Plan een nieuwe datum in: (yyyy/mm/dd)
							<input type="date" name="datum" />
							<input type="submit" name="submit" value="Voeg toe" />	
						</form>
						<br />
						Volgende werknemer staan reeds gepland:
						<table>
							<tr>
								<th> naam </th>
								<th> datum </th>
								<th> </th>
							</tr>
							<c:forEach items="${taak.planningLijst }" var="planning">
								<tr>
									<td>${planning.werknemer } </td>
									<td>
										<fmt:formatDate value="${planning.beginuur }" pattern="dd/MM/YYYY"/> 
									</td>
									<td>
										<form action="taakGeplandeWerknemerVerwijderen" method="get">
											<input type="hidden" name="id" value="${planning.id }" />
											<input type="submit" name="submit" value="verwijder" /> 
										</form>
									</td>
								</tr>
							</c:forEach>
						</table>
				</fieldset>
			<div id="vooruitgang">
				<table>
					<tr>
						<th>Naam Werknemer </th>
						<th>Beginuur</th>
						<th>Einduur</th> <!--  een String -->
						<th>isAanwezig</th>
						<th></th>
					</tr>
					<c:forEach items="${taak.planningLijst }" var="element">
						<tr>
							<td>${element.werknemer }</td>
							<td>
								<fmt:formatDate value="${element.beginuur }" pattern="dd/MM/YYYY" />
							</td>
							<td>
								<fmt:formatDate value="${element.einduur }" pattern="dd/MM/YYYY" />
							</td>
							<td>${element.isAanwezig }</td>
							
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

</body>
</html>