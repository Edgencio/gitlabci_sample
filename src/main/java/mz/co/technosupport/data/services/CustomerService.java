package mz.co.technosupport.data.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mz.co.technosupport.data.daos.CustomerDAO;
import mz.co.technosupport.data.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;


/**
 * Created by Edgêncio da Calista.
 */
@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerDAO customerDAO;

    @Inject
    ConsumerDAO consumerDAO;

    @Inject
    AddressDAO addressDAO;

    public List<Consumer> getCustomerAffiliates(long customerId) {
        List<Consumer> affiliates = new ArrayList();

        try {

            affiliates = consumerDAO.getAffiliates(customerId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao buscar affiliados do cliente");
        }
        return affiliates;
    }

    public List<Address> getCustomerAddresses(long customerId) {
        List<Address> addresses = new ArrayList();
        try {
            addresses = addressDAO.getAllByCustomerId(customerId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao buscar endereços do cliente");
        }
        return addresses;
    }
    
    
    
        public List<String> getCustomers() {
        List<String> suppliers = new ArrayList();
        List<Customer> sups = null;
        try {
            sups = customerDAO.getAll();
            for (Customer cust : sups) {
                suppliers.add(cust.getName());
            }
            return suppliers;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
        
        
       public Map getCustomerAffiliatesPerPage(long customerId, int pageNumber, int itemsPerPage, Map filter, Map ordering) {
 
        Map affiliates = new HashMap(0);

        try {
            affiliates = consumerDAO.fetchAffiliatesByCustomer(customerId, pageNumber, itemsPerPage, filter, ordering);
           
          
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }

        return affiliates;
    }
        

}
