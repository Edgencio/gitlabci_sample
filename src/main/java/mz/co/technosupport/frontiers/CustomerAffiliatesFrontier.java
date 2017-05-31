/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.component.HiList;
import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.data.services.AccountClientServiceImpl;
import mz.co.technosupport.data.services.ConsumerService;
import mz.co.technosupport.data.services.CustomerService;
import mz.co.technosupport.data.services.TicketServiceImpl;
import mz.co.technosupport.dto.UserDTO;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.service.AccountClientService;
import mz.co.technosupport.util.EmailSender;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class CustomerAffiliatesFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private CustomerService customerService;

    @Inject
    private UserDAO userDAO;
    
    @Inject
    private ConsumerService consumerService;

    @Inject
    private AccountClientServiceImpl accountClientServiceImpl;

    public List<Consumer> getAffiliates() {
        List<Consumer> affiliates = null;

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            
            long customerId = user.getCustomer().getCustomerID();
            affiliates = customerService.getCustomerAffiliates(customerId);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return affiliates;
    }

    public boolean createAffiliate(String fullName, String phone, String email) {
        UserDTO user = (UserDTO) activeUser.getProperty("user");
        try {
            ClientAccountInfo accountInfo = accountClientServiceImpl.addAffiliate(user.getCustomer().getCustomerID(), email, fullName, phone);
            if (accountInfo != null) {
                EmailSender.sendActivationMail(email, user.getUserName(),
                        user.getUserName());
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean removeAffiliate(String email) {

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            accountClientServiceImpl.removeAffiliate(customerId, email);
            frontEnd.ajaxRedirect("customer/affiliates");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean editAffiliate(String name, String phone, String email, long user_id) {
        User user = null;
        try {
            user = userDAO.find(user_id);
            user.setEmail(email);
            user.setMobile(phone);
            user.setName(name);
         

            userDAO.update(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void refreshPage() {
        frontEnd.ajaxRedirect("customer/affiliates");
    }

    public Map fetchAffiliatesPerPage(int pageNumber, int itemsPerPage, Map filter, Map ordering) {

        Map customerAffiliates = new HashMap(0);
        int totalMatchedPages = 0;
        int totalMatchedRows = 0;
        List<Consumer> affiliates = new ArrayList();

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            customerAffiliates = customerService.getCustomerAffiliatesPerPage(customerId, pageNumber, itemsPerPage, filter, ordering);
            affiliates = (List<Consumer>) customerAffiliates.get("list");
            totalMatchedRows = (int) customerAffiliates.get("total");
            totalMatchedPages = (int) customerAffiliates.get("pages");
            return HiList.listEncode(affiliates, totalMatchedRows, pageNumber, totalMatchedPages);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
    
    
    public Collection<String> fetchEmails() {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            return customerService.fetchUsersEmails(customerId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }
    
    
    public char linkAffiliate (String affiliateEmail){
        char response='C';
    try{
        UserDTO user = (UserDTO) activeUser.getProperty("user");
        long customerId = user.getCustomer().getCustomerID();
        response=consumerService.saveConsumer(customerId, affiliateEmail);
         EmailSender.sendActivationMail(affiliateEmail, user.getUserName(),
                        user.getUserName());
        return response;
     
    
    }catch(Exception ex){
    ex.printStackTrace();
    return 'C';
    }
    
    }
    

}
