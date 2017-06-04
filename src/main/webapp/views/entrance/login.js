Hi.view(function (_) {


    _.$postLoad = function () {
    _.isCustomer=true;   

    }

    _.$preLoad = function () {
        _.initAlerts();

    }
    
        _.initAlerts = function () {

    }
    
    
    _.accountController= function (controller){
       _.isCustomer=controller;  
    }
    

    _.doLogin = function (user, pass) {
        
           $('#login_loader').remove();
        //Verify if username and password fields are not empty.
        if (user == null || pass == null) {
            toastr.error('Por favor, preencha todos os campos!');
            
        } else {      
            // Add spinner to the button
            $('#btn_login').append('<i class="fa fa-spinner fa-spin" id="login_loader"  style="margin-left: 10px;"></i>');
            // Call User user Frontier and authenticate user
            UserFrontier.login(user, pass, _.isCustomer).try(function (result) {
                _.loginResult = result;
                _.$apply();

                if (_.loginResult == null) {
                    $('#login_loader').remove();
                    toastr.error('Credenciais incorrectas');
                } else {
                    console.log(_.loginResult);
                }

            });
        }
    }
    


    _.doLogout = function (user, pass) {

        UserFrontier.logout(user, pass).try(function (result) {

        });
    }
    
  

});