Hi.view(function (_) {

    _.$preLoad = function () {
        _.countSupplierTickets();
        _.fetchPendingTickets();

    }

    _.$postLoad = function () {
        //initialization setion;
        _.clickedTicket = "";
        _.supplierEmail = "";
        _.supplierPhone = "";
        _.pendingTickets = "";
        _.tickets = "";
        _.initPickers();
        //_.fecthSupplierHistory();
        _.fetchSupplierContacts();
        _.fetchSupplierTechnicians();
        _.initJQuery();
        _.isSearching = true;
        _.showTable = false;


    }


    _.initClickedTicket = function (ticket) {
        _.clickedTicket = ticket;

    }



    _.postFetchTickets = function (result) {
     
        if (result.totalRowsMatch == 0) {
            $('#supplierTicketsNotFound').css("display", "block");
            _.showTable = false;
            _.$apply();

        } else {
            moment.locale('pt');
            for (i = 0; i < result.data.length; i++) {
                result.data[i].date = moment(result.data[i].date).format('ll');
                if (result.data[i].status == "CLOSED") {
                    result.data[i].status = "Terminado";
                } else
                if (result.data[i].status == "PENDING") {
                    result.data[i].status = "Pendente";
                } else
                if (result.data[i].status == "OPENED") {
                    result.data[i].status = "Em progresso";
                }

            }
            _.showTable = true;
        }

    }




    _.assignTechnician = function (ticketId, technicianId) {

        if (typeof technicianId === 'undefined' || !technicianId) {
            _.showNoTechnicianToAssignErrorMessage();

        } else {

            $("#assign_technician_loader").remove();
            $('#btn_assign_technician').append('<i class="fa fa-spinner fa-spin" id="assign_technician_loader"  style="margin-left: 10px;"></i>');

            SupplierTicketsFrontier.assignTechnician(ticketId, technicianId).try(function (result) {
                if (result) {

                    $('#assign_technician_modal').modal('toggle');

                    for (i = 0; i < _.pendingTickets.length; i++) {
                        if (_.pendingTickets[i].id === ticketId) {
                            _.pendingTickets.splice(i, 1);
                        }

                    }

                    if ((_.pendingTickets.length === 0) || (_.pendingTickets.length < 1)) {
                        $("#pending_tickets_container").css("display", "none");
                    }
                     $('#pending_ticket-'+ ticketId).remove();


                    _.showAssignTechnicianSuccessMessage();
                } else {
                    _.showAssignTechnicianErrorMessage();
                }

            });
        }

    }

    _.fetchSupplierTechnicians = function () {

        SupplierTechniciansFrontier.getTechnicians().try(function (result) {
            _.supplierTechnicians = result;
            _.$apply();
        });
    }

    _.showDetails = function (ticket_id) {
        Hi.redirect("supplier/tdetails?id=" + ticket_id);

    }

    _.initPickers = function () {
        document.getElementsByClassName("datepicker").flatpickr();


    }


    //Fetching supplier contacts to fill contacts panel
    _.fetchSupplierContacts = function () {
        SupplierTicketsFrontier.getSupplierContacts().try(function (result) {
            _.activeUser = result;
            _.supplierEmail = result.userMail;
            _.supplierPhone = result.userPhone;
            _.$apply();

        });
    }


    //Fetching Supplier history (fills history table)
    _.fecthSupplierHistory = function () {
        SupplierTicketsFrontier.getSupplierHistory().try(function (result) {
            _.tickets = result;
            _.$apply();

            if ((_.tickets.length === 0) || (typeof _.tickets == 'undefined') || (_.tickets == null)) {
                $('#supplierTicketsNotFound').css("display", "block");
            }
            moment.locale('pt');
            for (i = 0; i < _.tickets.length; i++) {
                _.tickets[i].date = moment(_.tickets[i].date).format('ll');

            }

            _.$apply();
        });
    }


    //Fetches and count Supplier pending request 
    _.fetchPendingTickets = function () {
        SupplierTicketsFrontier.getPendingTickets().try(function (result) {
            _.pendingTickets = result;
            _.totalPendingTickets = result.length;
            _.$apply();
            moment.locale('pt');
            for (i = 0; i < _.pendingTickets.length; i++) {
                _.pendingTickets[i].date = moment(_.pendingTickets[i].date).format('ll');

            }


        });
    }


    _.countSupplierTickets = function () {
        SupplierTicketsFrontier.countSupplierTickets().try(function (result) {
            _.totalSupplierTickets = result;
            _.$apply();


        });

    }





    _.showAssignTechnicianSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Técnico alocado com sucesso!',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action: function () {



                    }

                }

            }
        });

    }



    _.showAssignTechnicianErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao tentar alocar um técnico a esta Tarefa!',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'

                }

            }
        });

    }


    _.showNoTechnicianToAssignErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Tens de selecionar o técnico que vais alocar!',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'

                }

            }
        });

    }



    _.editContacts = function (email, phone) {

        SupplierTicketsFrontier.updateSupplierContacts(email, phone).try(function (result) {
            if (result) {

                $('#edit_contacts_modal').modal('toggle');

                SupplierTicketsFrontier.updateActiveUserContacts(email, phone).try(function (result) {

                });
                _.showUpdateInfoSuccessMessage();
            } else {

                _.showUpdateInfoErrorMessage();
            }
        });
    }


    _.showUpdateInfoSuccessMessage = function () {
        $.alert({
            title: 'Parabéns!',
            content: 'Informação actualizada com sucesso!',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'

                }

            }
        });

    }


    _.showUpdateInfoErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar os contactos!',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'

                }

            }
        });

    }







    _.initJQuery = function () {
        $('.category-typeahead').typeahead({
            source: function (query, process) {
                CustomerTicketsFrontier.fetchCategories().try(function (result) {
                    return process(result);
                });
            }
        });

        $('.technician-typeahead').typeahead({
            source: function (query, process) {
                CustomerTicketsFrontier.fetchCustomers().try(function (result) {
                    return process(result);
                });
            }
        });

    }


    _.search = function (category_title, customer_name) {
        var startDate = $("#input_date").val();
//       calendar = flatpickr("#input_date");
//       var startDate=calendar.altInput.value;
        if (typeof startDate == 'undefined') {
            startDate = "2017-01-01";
        }

        if ((typeof customer_name == 'undefined' || customer_name == null) || (typeof category_title == 'undefined' || category_title == null)) {
            _.showMissingFieldsMessage();
        } else
        if ((typeof startDate == 'undefined' || startDate == null)) {
            //searches only by category and technician
            _.showMissingDateMessage();

        } else {

            $("#panel_pending_tickets").css('display', 'none');
            $("#panel_search_results").css('display', 'block');
            $("#search_loader").css('display', 'block');
            _.isSearching = true;

            //search using all fields
        
            SupplierTicketsFrontier.fullSearch(category_title, customer_name, startDate).try(function (result) {
                _.searchResults = result;
                _.$apply();
               

                if ((typeof result != 'undefined') && (result != null)) {
                    _.isSearching = false;
                    $("#search_loader").css('display', 'none');
                } else {
                    _.isSearching = false;
                    $("#search_loader").css('display', 'none');
                }



            });

        }


    }


    _.showMissingFieldsMessage = function () {
        $.alert({
            title: 'Atençao!',
            content: 'Os campos da categoria e do técnico não podem ser nulos!',
            type: 'warning',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-warning'

                }

            }
        });

    }



    _.showMissingDateMessage = function () {
        $.alert({
            title: 'Atençao!',
            content: 'O campo da data está em falta!',
            type: 'warning',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-warning'

                }

            }
        });

    }








});
