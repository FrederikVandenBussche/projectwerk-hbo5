/*
 * indien het om een nieuw personeelslid gaat is het wenselijk dat
 * adressen verborgen zijn 
 */

function setup(){
	var tekst = $("#personeelAanspreekNaam").text().trim();
	
	if(tekst.length <= 14){
		
		$("#bijNieuwVerbergen").hide();
		$("#personeelVerwijderen").hide();	
	} else {
	
		$("#bijNieuwVerbergen").show();
		$("#personeelVerwijderen").show();	
	}
}

$(setup);