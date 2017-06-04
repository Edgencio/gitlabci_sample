Hi.view(function (_) {

    _.$preLoad = function () {
        _.supplierAddresses = "";
        _.fetchSupplierAddresses();

    }

    _.$postLoad = function () {
        _.clickedAddress = "";


    }


    _.initClickedAddress = function (address) {
        _.clickedAddress = address;
      

    }

    _.initMap = function () {
        var url1 = {lat: _.supplierAddresses[0].latitude, lng: _.supplierAddresses[0].longitude};
        var map = new google.maps.Map(document.getElementById('map'), {
            zoom: 12,
            center: url1
        });

        if(_.supplierAddresses.length>1){
        for (i = 0; i < _.supplierAddresses.length; i++) {
            var url = {lat: _.supplierAddresses[i].latitude, lng: _.supplierAddresses[i].longitude};
            var marker = new google.maps.Marker({
                position: url,
                map: map
            });
        }
        }else{
            var url = {lat: _.supplierAddresses[0].latitude, lng: _.supplierAddresses[0].longitude};
            var marker = new google.maps.Marker({
                position: url,
                map: map
            });  
            
        }


    }


    _.fetchSupplierAddresses = function () {
        SupplierAddressesFrontier.getSupplierAddresses().try(function (result) {
            _.supplierAddresses = result;
            _.$apply();
         
         if(_.supplierAddresses.length==0){
             $('#supplierAddressesNotFound').css("display","block");
         }
            _.initMap();
        });

    }

    _.updateAddress = function () {
        $("#address_loader").remove();
        $('#btn_edit_address').append('<i class="fa fa-spinner fa-spin" id="address_loader"  style="margin-left: 10px;"></i>');
        SupplierAddressesFrontier.updateSupplierAddress(_.clickedAddress.id, _.clickedAddress.name, _.clickedAddress.description, _.clickedAddress.latitude, _.clickedAddress.longitude).try(function (result) {

            if (result) {
                $('#editAddressModal').modal('toggle');
                _.showUpdateAddressSuccessMessage();
            } else {
                _.showUpdateAddressErrorMessage();
            }



        });
    }


    _.removeAddress = function (id) {
     
        SupplierAddressesFrontier.removeAddress(id).try(function (result) {
         
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
            title: 'Erro!',
            content: 'Erro ao remover o java ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
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