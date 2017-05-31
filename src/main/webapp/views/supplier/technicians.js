Hi.view(function (_) {

    _.$preLoad = function () {

    }


    _.$postLoad = function () {

        _.technicianFirstName = "";
        _.technicianLastName = "";
        _.technicianEmail = "";
        _.technicianPhone = "";
        _.technicianFullName = "";
        _.noTechnicians = false;
        _.technicianToEdit = "";
        _.onlineTechnicians = [];
        _.isNewTechnician = false;
        _.existingTechnicianEmail = "";
        _.initTypeAhead();

    }


    _.newTechnician = function (param) {

        if (param == 1) {
            _.isNewTechnician = true;
            $('#technician_name').css("display", "block");
            $('#technician_phone').css("display", "block");
            $('#technician_email').css("display", "block");
            $('#existing_technician_email').css("display", "none");

        } else {
            _.isNewTechnician = false;
            $('#technician_name').css("display", "none");
            $('#technician_phone').css("display", "none");
            $('#technician_email').css("display", "none");
            $('#existing_technician_email').css("display", "block");

        }
    }



    _.initTypeAhead = function () {
        $('#technician_typeahead').typeahead({
            source: function (query, process) {
                SupplierTechniciansFrontier.fetchEmails().try(function (result) {
                    return process(result);
                });
            }

        });
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



    _.showSaveTechnicianConfirmAlert = function (technicianFullName, technicianPhone, technicianEmail, existingTechnicianEmail) {

        _.technicianFullName = technicianFullName;
        _.technicianEmail = technicianEmail;
        _.technicianPhone = technicianPhone;
        _.existingTechnicianEmail = existingTechnicianEmail;

        if (((typeof (_.existingTechnicianEmail) == "undefined") || (_.existingTechnicianEmail == "") || (_.existingTechnicianEmail == null)) && !_.isNewTechnician) {

            _.emptyEmailMessage();
        } else {

            $.confirm({
                title: 'Atenção!',
                content: 'Tem certeza que pretende criar um técnico novo? ',
                type: 'warning',
                buttons: {
                    confirm: {
                        text: 'SALVAR',
                        btnClass: 'btn-warning',
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
    }

    _.emptyEmailMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'O campo de email não pode ser vazio',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }



    _.createTechnician = function () {

        if (_.isNewTechnician) {
            SupplierTechniciansFrontier.createTechnician(_.technicianFullName, _.technicianPhone, _.technicianEmail).try(function (result) {
                if (result) {
                    $("#save_technicnian_loader").remove();
                    $('#add_technician_modal').modal('toggle');
                    _.showCreateTechnicianSuccessMessage();

                } else {
                    $("#save_technicnian_loader").remove();
                    _.showCreateTechnicianErrorMessage();
                }
                _.$apply();

            });
        } else {

            SupplierTechniciansFrontier.linkAffiliate(_.existingTechnicianEmail).try(function (result) {


                if (result == 'A') {
                    $("#save_technicnian_loader").remove();
                    $('#add_technician_modal').modal('toggle');
                    _.showTechnicianAlreadyLinkedMessage();
                } else
                if (result = 'B') {
                    $("#save_technicnian_loader").remove();
                    $('#add_technician_modal').modal('toggle');
                    _.showCreateTechnicianSuccessMessage();
                } else
                {
                    $("#save_technicnian_loader").remove();
                    _.showCreateTechnicianErrorMessage();
                }
            });

        }

    }





    _.removeTechnician = function () {

        SupplierTechniciansFrontier.removeTechnician(_.technicianEmail).try(function (result) {
            if (result) {
                _.showRemoveTechnicianSuccessMessage();

            } else {
                _.showRemoveTechnicianErrorMessage();
            }

        });
    }


    _.showRemoveTechnicianSuccessMessage = function () {
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



    _.showCreateTechnicianSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Técnico criado com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {
                        SupplierTechniciansFrontier.refreshPage().try(function () {

                        });
                    }
                }

            }
        });

    }



    _.showTechnicianAlreadyLinkedMessage = function () {
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


    _.showCreateTechnicianErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao criar técnico',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }



    _.initTechnicianToEdit = function (technician) {

        _.technicianToEdit = technician;


    }



    _.editTechnician = function () {

        SupplierTechniciansFrontier.editTechnician(_.technicianFullName, _.technicianPhone, _.technicianEmail, _.technicianToEdit.user.id).try(function (result) {

            if (result) {
                $('#editTechnicianModal').modal('toggle');
                _.showEditTechnicianSuccessMessage();

            } else {
                _.showEditTechnicianErrorMessage();
            }

        });
    }


    _.showEditTechnicinaConfirmAlert = function (technicianFullName, technicianPhone, technicianEmail) {


        _.technicianFullName = technicianFullName
        _.technicianEmail = technicianEmail;
        _.technicianPhone = technicianPhone;

        $.confirm({
            title: 'Atenção!',
            content: 'Tem certeza que pretende actualizar informações do Técnico? ',
            type: 'warning',
            buttons: {
                confirm: {
                    text: 'Actualizar',
                    btnClass: 'btn-warning',
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




    _.showEditTechnicianSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Dados actualizados com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {

                        SupplierTechniciansFrontier.refreshPage().try(function () {

                        });
                    }
                }

            }
        });

    }


    _.showEditTechnicianErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar informações do técnico ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }



    _.postFetchTechnicians = function (result) {
        console.log(result.pages);
        if (result.totalRowsMatch === 0) {
            console.log(result.pages);
            $('#noTechniciansFound').css("display", "block");
            $('#noTechniciansAvailable').css("display", "none");

        }


    }



});