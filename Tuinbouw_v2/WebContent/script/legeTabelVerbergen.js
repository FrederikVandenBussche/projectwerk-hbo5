/*
* Elke tabel bestaat uit een tableRow met table headers,
*  met daaronder eventueel een of meerdere tableRows met tableData. 
* 
* Dit script verbergt tabellen die onder hun table header geen tableData bevatten.
*/


function legeTabelVerbergen(){
   
    $("table").each(function(){
        
    	var aantalRijen = this.rows.length;
       
    	if(aantalRijen <= 1){
          
    		$(this).hide();
       }  
    });

}

$(legeTabelVerbergen);