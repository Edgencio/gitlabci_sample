/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.services.AccountTechnicianImpl;
import mz.co.technosupport.data.services.SupplierService;
import mz.co.technosupport.dto.UserDTO;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class SupplierAddressesFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private SupplierService suplierService;

    @Inject
    private AccountTechnicianImpl accountTechnicianServiceImpl;

    public List<Address> getSupplierAddresses() {
        List<Address> addresses = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            addresses = suplierService.getSupplierAddresses(supplierId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return addresses;
    }

    public boolean updateSupplierAddress(long addressId, String addressName, String addressDescription, double lat, double lng) {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            accountTechnicianServiceImpl.editAddress(supplierId, addressId, addressName, addressDescription, lat, lng);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    

    
    public boolean removeAddress(long addressId){
        try{UserDTO user = (UserDTO) activeUser.getProperty("user");
         long supplierId = user.getSupplier().getSupplierID();
         accountTechnicianServiceImpl.removeAddress(supplierId, addressId);
          frontEnd.ajaxRedirect("supplier/addresses");
         }catch(Exception ex){
         ex.printStackTrace();
         return false;
         }
        return true;
    
    }

}
