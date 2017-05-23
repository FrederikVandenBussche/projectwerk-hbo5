/*
 * indien het om een nieuwe klant gaat is het wenselijk dat: 
 *  - adressen
 *  - exporteren van prestaties
 *  verborgen zijn
 */

function setup(){
	var tekst = $("#klantAanspreekNaam").text().trim();
	
	if(tekst.length <= 20){
		
		$("#adressen").hide();
		$("#exportatie").hide();
	} else {
	
		$("#adressen").show();
		$("#exportatie").show();
	}
}

$(setup);