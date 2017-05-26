Hi.view(function (_) {

    _.$preLoad = function () {
        // _.initAlerts(); 
        _.fetchCustomerAffiliates();       
    }


    _.$postLoad = function () {
        _.affiliateFirstName="";
        _.affiliateLastName="";
        _.affiliateEmail="";
        _.affiliatePhone="";
        _.affiliateFullName="";
        _.affiliateToEdit="";

    }
    
    
    
 
    
    _.initAlerts = function () {

        $('.ks-alert-danger-dialog-example').on('click', function () {
            $.confirm({
                title: 'Atenção!',
                content: 'Não poderá desfazer esta acção! Tem certeza que pretende continuar? ',
                type: 'danger',
                buttons: {
                    confirm: {
                        text: 'REMOVER',
                        btnClass: 'btn-danger'
                    },
                    cancel: function () {

                    }
                }
            });
        });

    }
    
    
    


    _.showRemoveAffiliateConfirmAlert = function (affiliateEmail) {
        _.affiliateEmail=affiliateEmail;
       
        $.confirm({
            title: 'Atenção!',
            content: 'Não poderá desfazer esta acção! Tem certeza que pretende continuar? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'REMOVER',
                    btnClass: 'btn-danger',
                    action: function(){
                       _.removeAffiliate();
                       
                    }
                },
                cancel: function () {
               
                }
            }
        });
    }




    _.showSaveAffiliateConfirmAlert = function (affiliateFirstName, affiliateLastName, affiliatePhone, affiliateEmail) {
     
        _.affiliateFirstName=affiliateFirstName;
        _.affiliateLastName=affiliateLastName;
        _.affiliateEmail=affiliateEmail;
        _.affiliatePhone=affiliatePhone;
        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende criar um affiliado novo? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'SALVAR',
                    btnClass: 'btn-danger',
                    action: function () {
                      $("#save_affiliate_loader").remove();
                      $('#btn_saveAffiliate').append('<i class="fa fa-spinner fa-spin" id="save_affiliate_loader"  style="margin-left: 10px;"></i>');
                        _.createAffiliate();
                         
                    }
                },
                cancel: function () {

                }

            }
        });
    }




    _.fetchCustomerAffiliates = function () {

        CustomerAffiliatesFrontier.getAffiliates().try(function (result) {
            _.customerAffiliates = result;
            _.$apply();
            if(_.customerAffiliates.length==0){
                $('#customerAffiliatesNotFound').css("display", "block");
            }
            
        });
    }


    _.createAffiliate = function () {
        _.fullName = _.affiliateFirstName.concat(" ", _.affiliateLastName);

        CustomerAffiliatesFrontier.createAffiliate(_.fullName, _.affiliatePhone, _.affiliateEmail).try(function (result) {
            if (result) {
                 $('#myModal').modal('toggle');
                 _.showCreateAffiliateSuccessMessage();
                
            } else {
                _.showCreateAffiliateErrorMessage(); 
            }
            _.$apply();

        });

    }
    
    
    _.removeAffiliate= function(){
        
            CustomerAffiliatesFrontier.removeAffiliate( _.affiliateEmail).try(function (result) {
            if (result) {
                 _.showRemoveAffiliateSuccessMessage();
                
            } else {
                _.showRemoveAffiliateErrorMessage(); 
            }
      
        });
    }
    
    
        _.showRemoveAffiliateSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Affiliado removido com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'
                }
               
            }
        });
        
    }
    
    
        _.showCreateAffiliateSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Affiliado criado com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'
                }
               
            }
        });
        
    }
    
    
    
    _.showRemoveAffiliateErrorMessage= function(){
           $.alert({
            title: 'Erro!',
            content: 'Erro ao remover o affiliado! Tende novamente ',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }
               
            }
        });
        
    }
    
    _.showCreateAffiliateErrorMessage= function(){
           $.alert({
            title: 'Erro!',
            content: 'Erro ao criar affiliado',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }
               
            }
        });
        
    }
    
    
    
       _.initAffiliateToEdit= function(affiliate){
        
        _.affiliateToEdit=affiliate;
      
        
    }
    
    
    
    _.editAffiliate= function(){
        
            CustomerAffiliatesFrontier.editAffiliate(   _.affiliateFullName,  _.affiliatePhone, _.affiliateEmail, _.affiliateToEdit.user.id).try(function (result) {
            console.log(_.affiliateFullName);
            console.log(_.affiliateEmail);
            console.log(  _.affiliatePhone);
             console.log(  _.affiliateToEdit.user.id);
                if (result) {
                 _.showEditAffiliateSuccessMessage();
                
            } else {
                _.showEditAffiliateErrorMessage(); 
            }
      
        });
    }
    
    
     _.showEditAffiliateConfirmAlert = function (affiliateFullName, affiliatePhone, affiliateEmail) {
     
      
        _.affiliateFullName=affiliateFullName
        _.affiliateEmail=affiliateEmail;
        _.affiliatePhone=affiliatePhone;
        
        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende editar informações do affiliado? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'Actualizar',
                    btnClass: 'btn-danger',
                    action: function () {
                      $("#update_affiliate_loader").remove();
                      $('#btn_update_affiliate').append('<i class="fa fa-spinner fa-spin" id="update_affiliate_loader"  style="margin-left: 10px;"></i>');
                        _.editAffiliate();
                         
                    }
                },
                cancel: function () {

                }

            }
        });
    }
    
    


        _.showEditAffiliateSuccessMessage= function(){
           $.alert({
            title: 'Sucesso!',
            content: 'Dados actualizados com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function(){
                        
                        CustomerAffiliatesFrontier.refreshPage().try(function () {
                           
                        });
                    }
                }
               
            }
        });
        
    }
    
    
      _.showEditAffiliateErrorMessage= function(){
           $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar informações do affiliado ',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }
               
            }
        });
        
    }


});