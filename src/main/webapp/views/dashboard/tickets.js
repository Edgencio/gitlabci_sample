Hi.view(function(_){
    
    _.$preLoad=function(){
         
            
    }
    
    _.$postLoad=function(){
       _.initPickers();
             
    }
   
   
   
   _.showDetails=function (){
    
        Hi.redirect("customer/tdetails");
       
   }
   
   _.initPickers= function (){      
document.getElementsByClassName("datepicker").flatpickr();

       
   }
   
       
   
   
	

});