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
import mz.co.technosupport.data.daos.SupplierDAO;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.model.Ticket;
import mz.co.technosupport.data.model.User;
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
public class SupplierTicketsFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private TicketServiceImpl ticketService;

    @Inject
    private SupplierDAO supplierDAO;

    /**
     * Get customer details
     *
     * @return Data Transfer Object with user contacts (phone and email)
     */
    public UserDTO getSupplierContacts() {
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

    public Collection<TicketInfo> getSupplierHistory() {

        Collection<TicketInfo> tickets = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            tickets = ticketService.geSupplierHistory(supplierId, 0, 15);
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
            long supplierId = user.getSupplier().getSupplierID();
            pendingTickets = ticketService.getSupplierPendingTickets(supplierId, 0, 4);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return pendingTickets;
    }

    public long countSupplierTickets() {
        long totalTickets = 0;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            totalTickets = ticketService.countSupplierTickets(supplierId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return totalTickets;
        }
        return totalTickets;
    }

    public boolean assignTechnician(long ticketId, long technicianId) {
        try {
            ticketService.assignTechnician(ticketId, technicianId);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void refreshPage() {
        frontEnd.ajaxRedirect("supplier/tickets");
       
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

    public boolean updateSupplierContacts(String email, String phone) {
        Supplier supplier = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            user.getSupplier().setSupplierMail(email);
            user.getSupplier().setSupplierPhone(phone);
            activeUser.setProperty("user", user);
            long supplierId = user.getSupplier().getSupplierID();
            supplier = supplierDAO.find(supplierId);
            supplier.setMobile(phone);
            supplier.setEmail(email);

            supplierDAO.update(supplier);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateActiveUserContacts(String email, String phone) {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            user.getSupplier().setSupplierMail(email);
            user.getSupplier().setSupplierPhone(phone);
            activeUser.setProperty("user", user);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }
    
    
    
    
    
        public Collection<TicketInfo> fullSearch(String category, String customer, String startDate) {
        Collection<TicketInfo> tickets = null;
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            tickets = ticketService.getBySupplierFullSearch(supplierId, 0, 10, category, customer, startDate);
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
      long supplierId = user.getSupplier().getSupplierID();
      ticketsHistory = ticketService.getSupplierHistoryPerPage(supplierId, pageNumber, itemsPerPage, filter, ordering);
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
