Hi.view(function (_) {

    _.$preLoad = function () {
        _.supplierAddresses = "";
        _.fetchSupplierAddresses();
        _.pickedLat = 0;
        _.pickedLng = 0;


    }

    _.$postLoad = function () {
        _.clickedAddress = "";
        _.initModalMap();


    }


    _.initClickedAddress = function (address) {
        _.clickedAddress = address;
      

    }
    
    
    
    
    _.initModalMap = function () {
        var userMarker = false;
   
        var url1 = {lat: -25.9405999, lng: 32.5772084};

        //Creating the map object
        var map = new google.maps.Map(document.getElementById('mapModal'), {
            zoom: 14,
            center: url1,
            // disableDefaultUI: true
        });


        //Listen for any clicks on the map
        google.maps.event.addListener(map, 'click', function (event) {

            //Get the location clicked by user;
            var clickedLocation = event.latLng;

            if (userMarker === false) {
                //Create the marker.
                userMarker = new google.maps.Marker({
                    position: clickedLocation,
                    map: map,
                    draggable: true  //make it draggable
                });

                // Listen for drag events!
                google.maps.event.addListener(userMarker, 'dragend', function (event) {
                    // markerLocation();  
                    var currentLocation = userMarker.getPosition();
                    _.pickedLat = currentLocation.lat();
                    _.pickedLng = currentLocation.lng();
                    $('#input_latitude').val(_.pickedLat);
                    $('#input_longitude').val(_.pickedLng);


                });
            } else {
                //Marker has already been added, so just change ists location.
                userMarker.setPosition(clickedLocation);
            }
            //Get the marker's location.
            var currentLocation = userMarker.getPosition();
            _.pickedLat = currentLocation.lat();
            _.pickedLng = currentLocation.lng();
            $('#input_latitude').val(_.pickedLat);
            $('#input_longitude').val(_.pickedLng);


        });

        $('#btn_add_address').on('click', function () {

            $('#add_address_modal').modal({
                backdrop: 'static',
                keyboard: false
            }).on('shown.bs.modal', function () {
                google.maps.event.trigger(map, 'resize');
                map.setCenter(url1);
            });
        });


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



    
    _.addAddress = function (address_name, address_description, address_lat, address_lng){
       $("#address_loader").remove();  
       $("#btn_add_address_modal").append('<i class="fa fa-spinner fa-spin" id="address_loader"  style="margin-left: 10px;"></i>');
       SupplierAddressesFrontier.addSupplierAddress(address_name, address_description, address_lat, address_lng).try(function (result) {
           if(result==false){
                $("#btn_add_address_modal").prop('disabled', 'false');
                 $("#address_loader").remove(); 
                 _.showaddAddressErrorMessage();  
           }else if(result==true){
                 $("#btn_add_address_modal").prop('disabled', 'false');
               _.showaddAddressSuccessMessage();
             
             
           }
           
       });
    }
    
    _.validateFields = function (address_name, address_description, address_lat, address_lng){
        console.log(address_lat);
        console.log(address_lng);
        if(address_name=="" || typeof address_name== 'undefined'){
         _.showEmptyAddressNameMessage();    
        }else
            if(address_description=="" || typeof address_description== 'undefined'){
                _.showEmptyAddressDescriptionMessage()
            }else
            if(address_lat==0 || address_lng== 0){
                
                _.showEmptyAddressCoordsMessage();
            }else{
             $("#btn_add_address_modal").prop('disabled', 'true');
            _.addAddress(address_name, address_description, address_lat, address_lng);      
            }
        
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
    
    
    
         _.showEmptyAddressNameMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'O campo do nome do endereço é obrigatório ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });

    }
    
    
       _.showEmptyAddressDescriptionMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'O campo da descrição do endereço é obrigatório ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });
    }
    
    
      
       _.showEmptyAddressCoordsMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Por favor escolha uma localização no mapa ',
            type: 'danger',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger'
                }

            }
        });
    }
    


   _.showaddAddressErrorMessage = function () {
        $.alert({
            title: 'Erro!',
            content: 'Erro ao adicionar o endereço ',
            type: 'error',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-danger',
                    action:function (){
                          $("#address_loader").remove(); 
                          SupplierAddressesFrontier.refreshPage().try(function(){
                              
                          });
                    }
                }

            }
        });

    }
    
    
      _.showaddAddressSuccessMessage = function () {
        $.alert({
            title: 'Sucesso!',
            content: 'Endereço salvo com sucesso ',
            type: 'success',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-success',
                    action:function (){
                          $("#address_loader").remove(); 
                          $('#add_address_modal').modal('toggle');
                          CustomerAddressesFrontier.refreshPage().try(function(){
                              
                          });
                    }
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