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
		
		$("#voegNieuweTaakToe").hide();
		$("#takenlijst").hide();
		$("#voegGebruiktMateriaalToe").hide();
		$("#gebruiktMateriaal").hide();
		$("#opdrachtVerwijderenDiv").hide();
		
	} else {
	
		$("#takenlijst").show();
		$("#voegGebruiktMateriaalToe").show();
		$("#gebruiktMateriaal").show();
		$("#opdrachtVerwijderenDiv").show();
		
	}
}

$(setup);