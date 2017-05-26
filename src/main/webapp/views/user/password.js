Hi.view(function (_) {

    _.$preLoad = function () {



    }
    _.$postLoad = function () {


    }


    _.validateCredentials = function (c_pass, new_pass, rep_pass) {

        if (typeof c_pass === 'undefined' || !c_pass || typeof new_pass === 'undefined' || !new_pass || typeof rep_pass === 'undefined' || !new_pass) {
            _.showEmptyFieldsErrorMessage();
        } else {

            if (new_pass != rep_pass) {
                _.showNewPasswordErrorMessage();
            } else {
                _.updateUserPassword(new_pass);
            }
        }
    }


    _.updateUserPassword = function (new_password) {
        $("#change_pass_loader").remove();
        $('#btn_change_password').append('<i class="fa fa-spinner fa-spin" id="change_pass_loader"  style="margin-left: 10px;"></i>');
        UserFrontier.updatePassword(new_password).try(function (result) {

            if (result) {
                $("#change_pass_loader").remove();
                _.showUpdatePassSuccessMessage();
            } else {
                $("#change_pass_loader").remove();
                _.showUpdatePasswordErrorMessage();
            }
        });
    }

    _.refreshPage = function ( ) {

        UserFrontier.refreshPage().try(function (result) {

        });
    }

    _.showEmptyFieldsErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Por favor preencha todos os campos!',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }

    _.showUpdatePassSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Senha Actualizada com sucesso! ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {
                        UserFrontier.refreshPage().try(function (result) {


                        });


                    }

                }

            }
        });

    }


    _.showNewPasswordErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'As senhas introduzidas não são idênticas',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }



    _.showUpdatePasswordErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar a senha',
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



