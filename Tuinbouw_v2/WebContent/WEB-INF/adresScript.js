/*
 * Bij een lijst van een aantal adressen wordt slechts het eerste adres getoond en de andere adressen worden verborgen.
 * Boven dit getoonde adres staat een rij knoppen. Per adres is er één knop.
 * Bij het drukken op één van de knoppen zal het vorig getoont adres verborgen worden
 * en zal het adres dat overeenstemt met de ingedrukte knop getoond worden.
 */

function setup(){
    
    var aantalDivs = $(".adresLijst").children().size();
        
    var adresKnoppen = [];
    
    var volgnummer = 1;
    if (aantalDivs > 1){
    	
    	// een button maken voor het eerste adres
        
        var button = $('<input type="button" class = "btn btn-default active btn-xs adresKnop" id="adresDiv' + volgnummer + '" class="adresKnop actief" value="adres ' + volgnummer  + '" />');
            adresKnoppen.push(button);
        
    }
    
    if(aantalDivs > 2){
        //bestaande adressen die niet het eerste adres zijn verbergen en er een button voor maken
        for(var i = 1; i < aantalDivs - 1 ; i++){
            $(".adresLijst >div:eq(" + i + ")").hide();
            volgnummer += 1 ;
            var button = $('<input type="button" class = "btn btn-default active btn-xs adresKnop" id="adresDiv' + volgnummer + '" class="adresKnop" value="adres ' + volgnummer  + '" />');
            adresKnoppen.push(button);
        
        }
    }
    
    if (aantalDivs > 1 ){
    	 //'nieuw adres' formulier verbergen
        $(".adresLijst >div:eq(" + volgnummer + ")").hide();
        volgnummer += 1;
        var button = $('<input type="button" id="adresDiv' + volgnummer + '" class="btn btn-default btn-xs adresKnop" value="Voeg nieuw adres toe" />');
        adresKnoppen.push(button);
    }
   
   
    //de adresknoppen toevoegen
    $(".adresLijst").parent().prepend(adresKnoppen);    
    
    //koppel eventlisteners aan de knoppen
    $(".adresKnop").on("click", correctAdresTonen);
   
}

/*
 * afhankelijk van welke knop er in gedrukt is, moet je dit adres tonen
* function om het correcte adres te tonen en de andere adressen te verbergen
*/
function correctAdresTonen(){
    var geklikt = this.id;
    
    $(".adresKnop.actief").removeClass("actief");
    $(this).addClass("actief");
    var adresNr = geklikt.substring(8 , 9 ) - 1 ;
    
    $(".adresLijst > div").hide();
    $(".adresLijst > div:eq(" + adresNr + ")").show();
    
}

$(setup);