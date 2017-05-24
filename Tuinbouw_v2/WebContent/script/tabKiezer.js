/*
 * Sommige pagina's maken gebruik van bootstrap tabs.
 * 
 * Bij het herladen van de pagina is het niet altijd gewenst om naar
 * de eerste tab te gaan. Deze function zorgt ervoor dat er naar de
 * tab genavigeerd wordt met de naam die in '#tabKiezer' gedefinieerd wordt.
 */

function setup(){
	
	var tab = $("#tabKiezer").val();
	
	if (tab != ""){
		var li = "li";
		var liElement = li.concat(tab);
		
		$("#myTab li").removeClass("active");
		$("#myTabContent div").removeClass("in active");
				
		$('#' + liElement).addClass("active");
		$('#' + tab).addClass("in active");
	}
}


$(setup);