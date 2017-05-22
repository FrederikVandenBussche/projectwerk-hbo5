/*
 * indien het om een nieuwe opdracht gaat is het wenselijk dat: 
 *  - adressen
 *  - taken
 *  - gebruikte materialen
 *  - de knop om deze opdracht te verwijderen
 *  verborgen zijn
 */

function setup(){
	var tekst = $("#opdrachtAanspreekNaam").text().trim();
	
	if(tekst.length <= 8){
		
		$("#taakMateriaalVerwijderen").hide();
		$("#adresGegevens").hide();
		$("#adresWijzigen").hide();
		
		
	} else {
	
		$("#taakMateriaalVerwijderen").show();
		$("#adresGegevens").show();
		$("#adresWijzigen").show();
	}
	
	$("#klantenSelector").on("click", function(){
		$("#adresWijzigen").hide();
	});
}

$(setup);