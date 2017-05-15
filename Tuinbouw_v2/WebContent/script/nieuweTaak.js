/*
 * indien het om een nieuwe planning gaat 
 * dient de fieldset "planning" verborgen te zijn
 */

function setup(){
		
	var tekst = $("#taaknaam").val();
	    
	if(!tekst.trim()){
		
		$("#planning").hide();
		
	} else {
	
		$("#planning").show();
	
	}
}

$(setup);