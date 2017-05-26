Hi.template({

 logout: function (){
    console.log("Chegou no place que desejavamos");   
    UserFrontier.logout().try(function(){
        
    
    }).catch(function(){
    }).finally(function(){
    });
     
 }

});
