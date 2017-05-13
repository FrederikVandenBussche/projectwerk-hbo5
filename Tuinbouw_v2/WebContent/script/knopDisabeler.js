function setup(){
    
    $(".eenmalig").on("click", function(){
        $(this).prop("disabled", true);
    });
}

$(setup);