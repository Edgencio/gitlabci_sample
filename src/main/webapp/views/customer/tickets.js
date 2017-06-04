Hi.view(function (_) {

    _.$preLoad = function () {
        _.allSuppliers = "";
        _.countCustomerTickets();
        _.fetchPendingTickets();

    }

    _.$postLoad = function () {
        //initialization setion;
        _.customerEmail = "";
        _.customerPhone = "";
        _.pendingTickets = "";
        _.activeUser = "";
        _.tickets = "";
        _.initPickers();
        _.fetchCustomerContacts();
        _.fetchSuppliers();
        _.initTypeAhead();
        _.isSearching = true;
        _.showTable = false;

    }



    _.showDetails = function (ticket_id) {
        Hi.redirect("customer/tdetails?id=" + ticket_id);

    }

    _.initPickers = function () {
        document.getElementsByClassName("datepicker").flatpickr();
    }


    //Fetching customer contacts to fill contacts panel
    _.fetchCustomerContacts = function () {
        CustomerTicketsFrontier.getCustomerContacts().try(function (result) {
            _.activeUser = result;

        });
    }


    _.postFetchTickets = function (result) {
     
        if (result.totalRowsMatch == 0 ) {
            $('#customerTicketsNotFound').css("display", "block");
             _.showTable = false;
            _.$apply();

        }else{
             moment.locale('pt');
            for (i = 0; i < result.data.length; i++) {
                result.data[i].date = moment( result.data[i].date).format('ll');
                if(result.data[i].status=="CLOSED"){
                    result.data[i].status="Terminado";
                }else 
                    if(result.data[i].status=="PENDING"){
                    result.data[i].status="Pendente";
                }else 
                    if(result.data[i].status=="OPENED"){
                    result.data[i].status="Em progresso";
                }

            }
             _.showTable = true;
        }

    }




    //Fetching customer history (fills history table)
    _.fecthCustomerHistory = function () {
        CustomerTicketsFrontier.getCustomerHistory().try(function (result) {
            _.tickets = result;
            if (_.tickets.length == 0) {
                $('#customerTicketsNotFound').css("display", "block");

            }
            moment.locale('pt');
            for (i = 0; i < _.tickets.length; i++) {
                _.tickets[i].date = moment(_.tickets[i].date).format('ll');

            }

            _.$apply();
        });
    }

    //Fetches and count customer pending request 
    _.fetchPendingTickets = function () {
        CustomerTicketsFrontier.getPendingTickets().try(function (result) {
            _.pendingTickets = result;
            _.totalPendingTickets = result.length;
            _.$apply();
            moment.locale('pt');
            for (i = 0; i < _.pendingTickets.length; i++) {
                _.pendingTickets[i].date = moment(_.pendingTickets[i].date).format('ll');

            }




        });

    }


    _.countCustomerTickets = function () {
        CustomerTicketsFrontier.countCustomerTickets().try(function (result) {
            _.totalCustomerTickets = result;
            if(_.totalCustomerTickets==0){
              $('#customerTicketsNotFound').css("display", "block");
             _.showTable = false;
           
        }
        _.$apply();
            
            


        });

    }


    _.editContacts = function (email, phone) {

        CustomerTicketsFrontier.updateCustomerContacts(email, phone).try(function (result) {
            if (result) {
                $('#edit_contacts_modal').modal('toggle');

                CustomerTicketsFrontier.updateActiveUserContacts(email, phone).try(function (result) {
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

    _.fetchSuppliers = function () {

        CustomerTicketsFrontier.fetchSuppliers().try(function (result) {
            _.allSuppliers = result;
            _.$apply();
        });
    }



    _.initTypeAhead = function () {
        $('.category-typeahead').typeahead({
            source: function (query, process) {
                CustomerTicketsFrontier.fetchCategories().try(function (result) {
                    return process(result);
                });
            }
        });

        $('.technician-typeahead').typeahead({
            source: function (query, process) {
                CustomerTicketsFrontier.fetchSuppliers().try(function (result) {
                    return process(result);
                });
            }
        });

    }


    _.search = function (category_title, technician_name) {
        var startDate = $("#input_date").val();
        if (typeof startDate == 'undefined') {
            startDate = "2017-01-01";
        }

        if ((typeof technician_name == 'undefined' || technician_name == null) || (typeof category_title == 'undefined' || category_title == null)) {
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
            CustomerTicketsFrontier.fullSearch(category_title, technician_name, startDate).try(function (result) {
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