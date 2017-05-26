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
import mz.co.technosupport.data.daos.CustomerDAO;
import mz.co.technosupport.data.model.Customer;
import mz.co.technosupport.data.model.Ticket;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.data.services.CategoryService;
import mz.co.technosupport.data.services.CustomerService;
import mz.co.technosupport.data.services.SupplierService;
import mz.co.technosupport.data.services.TicketServiceImpl;
import mz.co.technosupport.dto.UserDTO;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.info.ticket.TimelineItemInfo;
import mz.co.technosupport.service.TicketService;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class CustomerTicketsFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private SupplierService supplierService;

    @Inject
    private TicketServiceImpl ticketService;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private CategoryService categoryService;
    
    @Inject
    private CustomerService customerService;

    /**
     * Get customer details
     *
     * @return Data Transfer Object with user contacts (phone and email)
     */
    public UserDTO getCustomerContacts() {
        UserDTO user = null;
        try {
            //Retrieving active user data
            user = (UserDTO) activeUser.getProperty("user");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return user;
    }

    public Collection<TicketInfo> getCustomerHistory() {

        Collection<TicketInfo> tickets = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            tickets = ticketService.getCustomerHistory(customerId, 0, 15);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return tickets;
    }

    public Collection<TicketInfo> getPendingTickets() {
        Collection<TicketInfo> pendingTickets = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            pendingTickets = ticketService.getCustomerPendingTickets(customerId, 0, 15);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return pendingTickets;
    }

    public long countCustomerTickets() {
        long totalTickets = 0;
        try {
            long clientId = 1;
            totalTickets = ticketService.countClienttickets(clientId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return totalTickets;
        }
        return totalTickets;
    }

    public Ticket getTicketById(long ticketId) {

        try {
            return ticketService.getTicketById(ticketId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public Collection<TimelineItemInfo> getTicketInfo(long ticketId) {

        try {
            TicketInfo ticket = ticketService.getById(ticketId);
            return ticket.getTimelineItems();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean updateCustomerContacts(String email, String phone) {
        Customer customer = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            user.getCustomer().setCustomerMail(email);
            user.getCustomer().setCustomerPhone(phone);
            activeUser.setProperty("user", user);
            long customerId = user.getCustomer().getCustomerID();
            customer = customerDAO.find(customerId);
            customer.setMobile(phone);
            customer.setEmail(email);

            customerDAO.update(customer);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateActiveUserContacts(String email, String phone) {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            user.getCustomer().setCustomerMail(email);
            user.getCustomer().setCustomerPhone(phone);
            activeUser.setProperty("user", user);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public Collection<String> fetchSuppliers() {
        try {
            return supplierService.getSuppliers();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    
       public Collection<String> fetchCustomers() {
        try {
            return customerService.getCustomers();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Collection<String> fetchCategories() {
        try {
            return categoryService.getCategories();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }
    
    

    public Collection<TicketInfo> fullSearch(String category, String technician, String startDate) {
        Collection<TicketInfo> tickets = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long customerId = user.getCustomer().getCustomerID();
            tickets = ticketService.getByCustomerfullSearch(customerId, 0, 10, category, technician, startDate);
            return tickets;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
    
    
   public Map historyTable (int pageNumber, int itemsPerPage, Map filter, Map ordering){
    
   Map ticketsHistory= new HashMap(0);
   int totalMatchedPages=0;
   int totalMatchedRows=0;
   List<TicketInfo> tickets=new ArrayList();
  
   
   try{
      UserDTO user = (UserDTO) activeUser.getProperty("user");
      long customerId = user.getCustomer().getCustomerID();
      ticketsHistory = ticketService.getCustomerHistoryPerPage(customerId, pageNumber, itemsPerPage, filter, ordering);
      tickets=(List<TicketInfo>)ticketsHistory.get("list");
      totalMatchedRows=(int)ticketsHistory.get("total");
      totalMatchedPages=(int)ticketsHistory.get("pages");
        return HiList.listEncode(tickets, totalMatchedRows, pageNumber, totalMatchedPages);
   }catch(Exception ex){
   ex.printStackTrace();
   return null;
   }
  
    }
    

}
