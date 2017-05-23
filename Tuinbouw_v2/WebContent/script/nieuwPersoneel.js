/*
 * indien het om een nieuw personeelslid gaat is het wenselijk dat
 * adressen verborgen zijn 
 */

function setup(){
	var tekst = $("#personeelAanspreekNaam").text().trim();
	
	if(tekst.length <= 14){
		
		$("#adressen").hide();
		$("#personeelVerwijderen").hide();	
	} else {
	
		$("#adressen").show();
		$("#personeelVerwijderen").show();	
	}
}

$(setup);