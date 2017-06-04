Hi.view(function (_) {

    _.$preLoad = function () {

    }


    _.$postLoad = function () {
        _.affiliateFirstName = "";
        _.affiliateLastName = "";
        _.affiliateEmail = "";
        _.affiliatePhone = "";
        _.affiliateFullName = "";
        _.affiliateToEdit = "";
        _.isNewAffiliate=false;
        _.existingAffiliateEmail="";
      //  _.initTypeAhead();

    }


_.newAffiliate = function (param){
    
    if(param==1){
        _.isNewAffiliate=true;
        $('#affiliate_name').css("display", "block");
        $('#affiliate_phone').css("display", "block");
        $('#affiliate_email').css("display","block");
        $('#existing_affiliate_email').css("display", "none");
     
    }else{
       _.isNewAffiliate=false; 
         $('#affiliate_name').css("display", "none");
        $('#affiliate_phone').css("display", "none");
        $('#affiliate_email').css("display","none");
        $('#existing_affiliate_email').css("display", "block");
      
    }
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
    
    
    _.initTypeAhead = function (){
        $('#affiliate_typeahead').typeahead({
            source: function(query, process){
                CustomerAffiliatesFrontier.fetchEmails().try(function(result){
                    return process(result);
                });
            }
            
        });
    }





    _.showRemoveAffiliateConfirmAlert = function (affiliateEmail) {
        _.affiliateEmail = affiliateEmail;

        $.confirm({
            title: 'Atenção!',
            content: 'Não poderá desfazer esta acção! Tem certeza que pretende continuar? ',
            type: 'warning',
            buttons: {
                confirm: {
                    text: 'REMOVER',
                    btnClass: 'btn-warning',
                    action: function () {
                        _.removeAffiliate();

                    }
                },
                cancel: function () {

                }
            }
        });
    }







    _.showSaveAffiliateConfirmAlert = function (affiliateFullName, affiliatePhone, affiliateEmail,  existingAffiliateEmail) {

        _.affiliateFullName = affiliateFullName;
        _.affiliateEmail = affiliateEmail;
        _.affiliatePhone = affiliatePhone;
        _.existingAffiliateEmail = existingAffiliateEmail;
        
        if(((typeof (_.existingAffiliateEmail)== "undefined") ||( _.existingAffiliateEmail=="") ||(_.existingAffiliateEmail==null)) && !_.isNewAffiliate){
               
             _.emptyEmailMessage();    
           }else{
        
        
        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende criar um affiliado novo? ',
            type: 'warning',
            buttons: {
                confirm: {
                    text: 'SALVAR',
                    btnClass: 'btn-warning',
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
        
        
        
    }




    _.createAffiliate = function () {
     $('#btn_saveAffiliate').prop("disabled","true");
        
     if( _.isNewAffiliate){
         
        
         
        CustomerAffiliatesFrontier.createAffiliate(_.affiliateFullName, _.affiliatePhone, _.affiliateEmail).try(function (result) {
            if (result) {
                $('#btn_saveAffiliate').prop("disabled","false");
                $('#myModal').modal('toggle');
                _.showCreateAffiliateSuccessMessage();

            } else {
                $('#btn_saveAffiliate').prop("disabled","false");
                _.showCreateAffiliateErrorMessage();
            }
            _.$apply();

        });
      
       
        }   else 
           if(! _.isNewAffiliate){
           
         CustomerAffiliatesFrontier.checkIfUserExists(_.existingAffiliateEmail).try(function (result){
             if(result==true){
                CustomerAffiliatesFrontier.linkAffiliate(_.existingAffiliateEmail).try(function (result){
          
            if(result=='A'){
                  $('#btn_saveAffiliate').prop("disabled","false");
                  $("#save_affiliate_loader").remove();
                  $('#myModal').modal('toggle');
                _.showAffiliateAlreadyLinkedMessage();
            }else 
                if(result='B'){
                $('#btn_saveAffiliate').prop("disabled","false");
                 $("#save_affiliate_loader").remove();
                 $('#myModal').modal('toggle');
                  _.showCreateAffiliateSuccessMessage();
            }else
                if(result='C'){
                 $('#btn_saveAffiliate').prop("disabled","false");
                 $("#save_affiliate_loader").remove();
               _.showCreateAffiliateErrorMessage(); 
                
            }
        });  
                 
             }else{
                 
                  $("#save_affiliate_loader").remove();
                  $('#btn_saveAffiliate').prop("disabled","false");
                 _.showUserNotFoundMessage();
             }
             
             
         });
           
       
        
    }

    }


    _.removeAffiliate = function () {

        CustomerAffiliatesFrontier.removeAffiliate(_.affiliateEmail).try(function (result) {
            if (result) {
                _.showRemoveAffiliateSuccessMessage();

            } else {
                _.showRemoveAffiliateErrorMessage();
            }

        });
    }


    _.showUserNotFoundMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Usuário não encontrado! Tente criar um novo',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger',
                    action: function(){
                         $('#btn_saveAffiliate').prop("disabled","false");
                           CustomerAffiliatesFrontier.refreshPage().try(function () {

                        });
                    }
                }

            }
        });

    }


    _.showRemoveAffiliateSuccessMessage = function () {
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


    _.postFetchAffiliates = function (result) {
       // console.log(result);
        if (result.totalRowsMatch === 0) {
             
            $('#customerAffiliatesNotFound').css("display", "block");

        }


    }


    _.showCreateAffiliateSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Affiliado criado com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {
                        CustomerAffiliatesFrontier.refreshPage().try(function () {

                        });
                    }
                }

            }
        });

    }


 _.showAffiliateAlreadyLinkedMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Este usuário já está associado como affiliado ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger',
                    action: function () {
                    
                    }
                }

            }
        });

    }


 _.showRemoveAffiliateErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao remover o affiliado! Tende novamente ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }


    _.emptyEmailMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Por favor introduza um email válido!',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }

    _.showCreateAffiliateErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao criar affiliado',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }



    _.initAffiliateToEdit = function (affiliate) {

        _.affiliateToEdit = affiliate;


    }



    _.editAffiliate = function () {
        $('#btn_update_affiliate').prop("disabled","true");

        CustomerAffiliatesFrontier.editAffiliate(_.affiliateFullName, _.affiliatePhone, _.affiliateEmail, _.affiliateToEdit.user.id).try(function (result) {

            if (result) {
                $('#btn_update_affiliate').prop("disabled","false");
                _.showEditAffiliateSuccessMessage();

            } else {
                $('#btn_update_affiliate').prop("disabled","false");
                _.showEditAffiliateErrorMessage();
            }

        });
    }


    _.showEditAffiliateConfirmAlert = function (affiliateFullName, affiliatePhone, affiliateEmail) {


        _.affiliateFullName = affiliateFullName
        _.affiliateEmail = affiliateEmail;
        _.affiliatePhone = affiliatePhone;
        

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




    _.showEditAffiliateSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Dados actualizados com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {

                        CustomerAffiliatesFrontier.refreshPage().try(function () {

                        });
                    }
                }

            }
        });

    }


    _.showEditAffiliateErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar informações do affiliado ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }


});