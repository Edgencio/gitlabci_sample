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
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.services.AccountClientServiceImpl;
import mz.co.technosupport.data.services.CustomerService;
import mz.co.technosupport.dto.UserDTO;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class CustomerAddressesFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private CustomerService customerService;

    @Inject
    private AccountClientServiceImpl accountClientServiceImpl;

    public List<Address> getCustomerAddresses() {
        List<Address> addresses = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            addresses = customerService.getCustomerAddresses(customerId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return addresses;
    }

    public boolean updateCustomerAddress(long addressId, String addressName, String addressDescription, double lat, double lng) {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            accountClientServiceImpl.editAddress(customerId, addressId, addressName, addressDescription, lat, lng);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addCustomerAddress(String addressName, String addressDescription, double lat, double lng) {
        try {
            if (addressName == " " || addressName == null) {
                return false;
            } else if (addressDescription == " " || addressDescription == null) {
                return false;
            } else if (lat == 0 || lat == 0) {
                return false;
            } else {
                UserDTO user = (UserDTO) activeUser.getProperty("user");
                long customerId = user.getCustomer().getCustomerID();
                accountClientServiceImpl.addAddress(customerId, addressName, addressDescription, lat, lng);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeAddress(long addressId) {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            accountClientServiceImpl.removeAddress(customerId, addressId);
            frontEnd.ajaxRedirect("customer/addresses");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;

    }
    
    
     public void refreshPage() {
        frontEnd.ajaxRedirect("customer/addresses");
    }

}
