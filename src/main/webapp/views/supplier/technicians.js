Hi.view(function (_) {

    _.$preLoad = function () {
   
    }


    _.$postLoad = function () {
     
        _.technicianFirstName = "";
        _.technicianLastName = "";
        _.technicianEmail = "";
        _.technicianPhone = "";
        _.technicianFullName="";
        _.noTechnicians=false;
        _.technicianToEdit="";
        _.onlineTechnicians= [];

    }

    _.showRemoveTechnicianConfirmAlert = function (technicianEmail) {
        _.technicianEmail = technicianEmail;

        $.confirm({
            title: 'Atenção!',
            content: 'Não poderá desfazer esta acção! Tem certeza que pretende continuar? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'REMOVER',
                    btnClass: 'btn-danger',
                    action: function () {
                        _.removeTechnician();

                    }
                },
                cancel: function () {

                }
            }
        });
    }
    
    
    
    _.showSaveTechnicianConfirmAlert = function (technicianFirstName, technicianLastName, technicianPhone, technicianEmail) {
     
        _.technicianFirstName=technicianFirstName;
        _.technicianLastName=technicianLastName;
        _.technicianEmail=technicianEmail;
        _.technicianPhone=technicianPhone;
        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende criar um técnico novo? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'SALVAR',
                    btnClass: 'btn-danger',
                    action: function () {
                      $("#save_technicnian_loader").remove();
                      $('#btn_saveTechnician').append('<i class="fa fa-spinner fa-spin" id="save_technicnian_loader"  style="margin-left: 10px;"></i>');
                        _.createTechnician();
                         
                    }
                },
                cancel: function () {

                }

            }
        });
    }
    
    
    

    
     _.createTechnician = function () {
        _.fullName = _.technicianFirstName.concat(" ", _.technicianLastName);

        SupplierTechniciansFrontier.createTechnician(_.fullName,  _.technicianPhone,  _.technicianEmail).try(function (result) {
            if (result) {
                 $('#myModal').modal('toggle');
                 _.showTechnicianSuccessMessage();
                
            } else {
                _.showCreateTechnicianErrorMessage(); 
            }
            _.$apply();

        });

    }
    
    
    
      _.removeTechnician= function(){
        
           SupplierTechniciansFrontier.removeTechnician(_.technicianEmail).try(function (result) {
            if (result) {
                 _.showRemoveTechnicianSuccessMessage();
                
            } else {
                _.showRemoveTechnicianErrorMessage(); 
            }
      
        });
    }
    
    
        _.showRemoveTechnicianSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Técnico removido com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'
                }
               
            }
        });
        
    }
    
    
    
            _.showCreateTechnicianSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Técnico criado com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'
                }
               
            }
        });
        
    }
    
    
      _.showCreateTechnicianErrorMessage= function(){
           $.alert({
            title: 'Erro!',
            content: 'Erro ao criar técnico',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }
               
            }
        });
        
    }
    
    
  
       _.initTechnicianToEdit= function(technician){
        
        _.technicianToEdit=technician;
      
        
    }
    
    
    
    _.editTechnician= function(){
        
            SupplierTechniciansFrontier.editTechnician(   _.technicianFullName,  _.technicianPhone, _.technicianEmail, _.technicianToEdit.user.id).try(function (result) {
         
                if (result) {
                     $('#editTechnicianModal').modal('toggle');
                 _.showEditTechnicianSuccessMessage();
                
            } else {
                _.showEditTechnicianErrorMessage(); 
            }
      
        });
    }
    
    
     _.showEditTechnicinaConfirmAlert = function (technicianFullName, technicianPhone, technicianEmail) {
     
      
        _.technicianFullName=technicianFullName
        _.technicianEmail=technicianEmail;
        _.technicianPhone=technicianPhone;
        
        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende actualizar informações do Técnico? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'Actualizar',
                    btnClass: 'btn-danger',
                    action: function () {
                      $("#update_technician_loader").remove();
                      $('#btn_update_technician').append('<i class="fa fa-spinner fa-spin" id="update_technician_loader"  style="margin-left: 10px;"></i>');
                        _.editTechnician();
                         
                    }
                },
                cancel: function () {

                }

            }
        });
    }
    
    


        _.showEditTechnicianSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Dados actualizados com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function(){
                        
                        SupplierTechniciansFrontier.refreshPage().try(function () {
                          
                        });
                    }
                }
               
            }
        });
        
    }
    
    
      _.showEditTechnicianErrorMessage= function(){
           $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar informações do técnico ',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }
               
            }
        });
        
    }
    
    
    
    _.postFetchTechnicians = function(result){
         if (result.totalRowsMatch === 0) {
            $('#noTechniciansFound').css("display", "block");
            $('#noTechniciansAvailable').css("display", "none");
           
        }
        
        
    }
  


});