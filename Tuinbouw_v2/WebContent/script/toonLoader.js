/*
 * Tijdens het aanmelden wordt er verbinding gemaakt met de databank en worden 
 * verschillende zaken geladen.
 * 
 * Dit laden duurt een eindje en tijdens het laden (dus als er op de knop gedrukt wordt)
 * verschijnt er een loader.
 * 
 */

function setup(){
	
	$("#submit").on("click", function(){
		$(".loader").show();
	});
}

$(setup);