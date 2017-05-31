/*
 * indien het om een nieuwe planning gaat 
 * dient de fieldset "planning" verborgen te zijn
 */

function setup(){
		
	var tekst = $("#taaknaam").val();
	    
	if(!tekst.trim()){
		
		$("#werknemerOpdrachtTaak").hide();
		$("#bijNieuwVerbergen").hide();
	} else {
	
		$("#werknemerOpdrachtTaak").show();
		$("#bijNieuwVerbergen").show();
	}
}

$(setup);