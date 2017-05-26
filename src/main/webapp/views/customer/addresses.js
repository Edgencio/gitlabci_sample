Hi.view(function (_) {

    _.$preLoad = function () {
        _.customerAddresses = "";
        _.fetchCustomerAddresses();

    }

    _.$postLoad = function () {
        _.clickedAddress = "";




    }


    _.initClickedAddress = function (address) {
        _.clickedAddress = address;
        console.log(_.clickedAddress);

    }

    _.initMap = function () {
        var url1 = {lat: _.customerAddresses[0].latitude, lng: _.customerAddresses[0].longitude};
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 4,
            center: url1
        });

        if(_.customerAddresses.length>1){
        for (i = 0; i < _.customerAddresses.length; i++) {
            var url = {lat: _.customerAddresses[i].latitude, lng: _.customerAddresses[i].longitude};
            var marker = new google.maps.Marker({
                position: url,
                map: map
            });
        }
        }else{
             var url = {lat: _.customerAddresses[0].latitude, lng: _.customerAddresses[0].longitude};
            var marker = new google.maps.Marker({
                position: url,
                map: map
            });
        }


    }


    _.fetchCustomerAddresses = function () {
        CustomerAddressesFrontier.getCustomerAddresses().try(function (result) {
            _.customerAddresses = result;
            
            if(_.customerAddresses.length==0){
                $('#customerAddressesNotFound').css("display", "block");
            }
            _.$apply();
            _.initMap();
        });

    }

    _.updateAddress = function () {
        $("#address_loader").remove();
        $('#btn_edit_address').append('<i class="fa fa-spinner fa-spin" id="address_loader"  style="margin-left: 10px;"></i>');
        CustomerAddressesFrontier.updateCustomerAddress(_.clickedAddress.id, _.clickedAddress.name, _.clickedAddress.description, _.clickedAddress.latitude, _.clickedAddress.longitude).try(function (result) {

            if (result) {
                $('#editAddressModal').modal('toggle');
                _.showUpdateAddressSuccessMessage();
            } else {
                _.showUpdateAddressErrorMessage();
            }



        });
    }


    _.removeAddress = function (id) {
        console.log(id);
        CustomerAddressesFrontier.removeAddress(id).try(function (result) {
         
            if (result) {
                _.showRemoveAddressSuccessMessage();
            } else {
                _.showRemoveAddressErrorMessage();
            }
        });

    }


    _.showRemoveAddressConfirmAlert = function (address) {
        _.clickedAddress = address;
        $.confirm({
            title: 'Atenção!',
            content: 'Não poderá desfazer esta acção! Tem certeza que pretende continuar? ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'REMOVER',
                    btnClass: 'btn-danger',
                    action: function () {
                        _.removeAddress(_.clickedAddress.id);

                    }
                },
                cancel: function () {

                }
            }
        });
    }


    _.showRemoveAddressSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Endereço removido com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'

                }

            }
        });

    }


    _.showRemoveAddressErrorMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Endereço removido com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success'
                }

            }
        });

    }

    _.showUpdateAddressSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Endereço actualizado com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',

                }

            }
        });

    }


    _.showUpdateAddressErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao actualizar o endereço ',
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