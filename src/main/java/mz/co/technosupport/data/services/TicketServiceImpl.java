/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.services;

import mz.co.technosupport.data.daos.*;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Feedback;
import mz.co.technosupport.data.model.Ticket;
import mz.co.technosupport.data.model.TimelineItem;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.info.ticket.TimelineItemInfo;
import mz.co.technosupport.info.util.TicketStatusEnum;
import mz.co.technosupport.service.TicketService;
import mz.co.technosupport.util.Middleware;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mz.co.technosupport.data.model.Technitian;

/**
 *
 * @author zJohn
 */
@ApplicationScoped
public class TicketServiceImpl implements TicketService {

    @Inject
    AddressDAO addressDAO;
    @Inject
    TechnicianDAO technicianDAO;
    @Inject
    TicketDAO ticketDAO;
    @Inject
    ConsumerDAO consumerDAO;
    @Inject
    TimelineItemDAO timelineItemDAO;
    @Inject
    CategoryDAO categoryDAO;
    @Inject
    FeedbackDAO feedbackDAO;

    @Override
    public Collection<TicketInfo> getClientHistory(long clientId, int skip, int limit) {
        List<TicketInfo> clientHistory = new ArrayList();
        List<Ticket> tickets = new ArrayList();

        try {
            tickets = ticketDAO.getByConsumerIdAndSkipLimit(clientId, skip, limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }
        clientHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientHistory;
    }

    /**
     * @param customerId
     * @param skip
     * @param limit
     * @return
     */
    public Collection<TicketInfo> getCustomerHistory(long customerId, int skip, int limit) {
        List<TicketInfo> clientHistory = new ArrayList();
        List<Ticket> tickets = new ArrayList();

        try {
            tickets = ticketDAO.getByCustomerIdAndSkipLimit(customerId, skip, limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }
        clientHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientHistory;
    }
    

 /**
  * @param customerId
  * @param pageNumber
  * @param itemsPerPage
  * @param filter
  * @param ordering
  * @return 
  */
    public Map getCustomerHistoryPerPage(long customerId, int pageNumber, int itemsPerPage, Map filter, Map ordering) {
        List<Ticket> clientHistory = new ArrayList();
        Map tickets = new HashMap(0);
        List finalTicketsList = new ArrayList();
        List<TimelineItemInfo> timeLineItens= new ArrayList();

        try {
            tickets = ticketDAO.fetchTicketsByCustomer(customerId, pageNumber, itemsPerPage, filter, ordering);
            List <Ticket> ticketsList = (List<Ticket>) tickets.get("list");
            
            for(Ticket tick:ticketsList){
              tick.setTimelineItems(null);
              finalTicketsList.add(tick);
            }
            
            clientHistory = Middleware.ticketInfoInfoList(finalTicketsList, addressDAO, consumerDAO, technicianDAO);
            tickets.remove("list", clientHistory);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }

        return tickets;
    }

    
    
    
    /**
     * @param supplierId
     * @param pageNumber
     * @param itemsPerPage
     * @param filter
     * @param ordering
     * @return 
     */
    public Map getSupplierHistoryPerPage(long supplierId, int pageNumber, int itemsPerPage, Map filter, Map ordering) {
        List<Ticket> clientHistory = new ArrayList();
        Map tickets = new HashMap(0);
        List finalTicketsList = new ArrayList();
        

        try {
            tickets = ticketDAO.fetchTicketsBySupplier(supplierId, pageNumber, itemsPerPage, filter, ordering);
            List <Ticket> ticketsList = (List<Ticket>) tickets.get("list");
            
            for(Ticket tick:ticketsList){
              tick.setTimelineItems(null);
              finalTicketsList.add(tick);
            }
            
            clientHistory = Middleware.ticketInfoInfoList(finalTicketsList, addressDAO, consumerDAO, technicianDAO);
            tickets.remove("list", clientHistory);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }

        return tickets;
    }
    
    
    /**
     * @param clientId
     * @param limitStart
     * @param limitEnd
     * @return clientTickets
     */
    public Collection<TicketInfo> getClientPendingTickets(long clientId, int skip, int limit) {
        List<TicketInfo> clientPendingTickets = new ArrayList();
        List<Ticket> tickets = new ArrayList();
        try {
            tickets = ticketDAO.getPendingByCostumerIdAndSkipLimit(clientId, skip, limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao buscar tickets pendentes do cliente".toUpperCase());
        }
        clientPendingTickets = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientPendingTickets;
    }

    /**
     * @author Edgencio da Calista
     * @param clientId
     * @param limitStart
     * @param limitEnd
     * @return clientTickets
     */
    public Collection<TicketInfo> getCustomerPendingTickets(long customerId, int skip, int limit) {
        List<TicketInfo> clientPendingTickets = new ArrayList();
        List<Ticket> tickets = new ArrayList();
        try {
            tickets = ticketDAO.getPendingByCostumerIdAndSkipLimit(customerId, skip, limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao buscar tickets pendentes do cliente".toUpperCase());
        }
        clientPendingTickets = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientPendingTickets;
    }

    /**
     * @author Edgencio da Calista
     * @param technicianId
     * @param limitStart
     * @param limitEnd
     * @return clientTickets
     */
    public Collection<TicketInfo> getSupplierPendingTickets(long technicianId, int skip, int limit) {
        List<TicketInfo> clientPendingTickets = new ArrayList();
        List<Ticket> tickets = new ArrayList();
        try {
            tickets = ticketDAO.getPendingBySupplierIdAndSkipLimit(technicianId, skip, limit);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao buscar tickets pendentes do supplier".toUpperCase());
        }
        clientPendingTickets = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientPendingTickets;
    }

    /**
     * @author Edgencio da Calista
     * @param technicianId
     * @return totalTickets (long)
     */
    public long countSupplierTickets(long supplierId) {
        long totalTickets = 0;
        try {
            totalTickets = ticketDAO.countSupplierTickets(supplierId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao contar os tickets do supplier");
        }
        return totalTickets;
    }

    /**
     * @author Edgencio da Calista
     * @param clientId
     * @return totalTickets (long)
     */
    public long countClienttickets(long clientId) {
        long totalTickets = 0;
        try {
            totalTickets = ticketDAO.countClientTickets(clientId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao contar os tickets do client");
        }
        return totalTickets;
    }

    /**
     * @author Edgêncio da Calista
     * @param supplierId
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public Collection<TicketInfo> geSupplierHistory(long supplierId, int limitStart, int limitEnd) {
        List<TicketInfo> technicianHistory;
        List<Ticket> tickets;

        try {
            Technitian technician = technicianDAO.getByTechnicianId(supplierId);
            long supplier_id = technician.getSupplier().getId();

            System.err.println(supplier_id);
            //tickets = ticketDAO.getByTechnicianIdAndSkipLimit(technicianId, limitStart, limitEnd);
            tickets = ticketDAO.getBySupplierIdSkipLimit(supplier_id, limitStart, limitEnd);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do Technician".toUpperCase());
        }
        technicianHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return technicianHistory;
    }

    @Override
    public Collection<TicketInfo> geTechniciantHistory(long technicianId, int limitStart, int limitEnd) {
        List<TicketInfo> technicianHistory;
        List<Ticket> tickets;

        try {

            tickets = ticketDAO.getByTechnicianIdAndSkipLimit(technicianId, limitStart, limitEnd);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do Technician".toUpperCase());
        }
        technicianHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return technicianHistory;
    }

    @Override
    public TicketInfo getById(long ticketId) {
        Ticket ticket = new Ticket();
        try {
            ticket = ticketDAO.find(ticketId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Recuperar Ticket".toUpperCase());
        }
        return Middleware.ticketInfoMethod(ticket, addressDAO, consumerDAO, technicianDAO);
    }

    /**
     *
     * @param ticketId
     * @return
     */
    public Ticket getTicketById(long ticketId) {
        Ticket ticket = new Ticket();
        Collection timelineItems = null;
        try {
            ticket = ticketDAO.find(ticketId);
            timelineItems = ticket.getTimelineItems();
            ticket.setTimelineItems(Middleware.timelineItemInfoList(timelineItems));
            return ticket;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void saveClientFeedback(long l, long l1, double v, String s) {

        Feedback feedback = new Feedback();
        try {
            feedback.setConsumer(consumerDAO.find(l));
            feedback.setTicket(ticketDAO.find(l1));
            feedback.setRate(v);
            feedback.setMessage(s);
            feedbackDAO.create(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTechnicianFeedback(long l, long l1, double v, String s) {

        Feedback feedback = new Feedback();
        try {
            feedback.setTechnitian(technicianDAO.find(l));
            feedback.setTicket(ticketDAO.find(l1));
            feedback.setRate(v);
            feedback.setDate(new Date());
            feedback.setMessage(s);
            feedbackDAO.create(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openTicket(TicketInfo ticketInfo) {

        try {
            Ticket ticket = new Ticket();

            ticket.setDate(ticketInfo.getDate());
            ticket.setName(ticketInfo.getName());
            ticket.setStatus(TicketStatusEnum.PENDING);

            ticket.setCategory(categoryDAO.find(ticketInfo.getCategory().getId()));
            ticket.setClientAddress(addressDAO.find(ticketInfo.getClientAddress().getId()));
            ticket.setTechnician(technicianDAO.find(ticketInfo.getTechnician().getId()));
            ticket.setConsumer(consumerDAO.find(ticketInfo.getClient().getId()));

            ticketDAO.create(ticket);
            ticketInfo.setId(ticket.getId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao tentar criar Ticket: " + e.getMessage());
        }
    }

    @Override
    public void closeTicket(long clientId, long ticketId) {
        Ticket ticket = new Ticket();
        try {
            ticket = ticketDAO.find(ticketId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Recuperar Ticket".toUpperCase());
        }
        ticket.setStatus(TicketStatusEnum.CLOSED);
        Consumer consumer = new Consumer();
        try {
            consumer = consumerDAO.find(clientId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Recuperar Cliente".toUpperCase());
        }
        ticket.setConsumer(consumer);
        try {
            ticketDAO.update(ticket);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Actualizar Ticket".toUpperCase());
        }
    }

    @Override
    public void addTimelineInfo(long ticketId, TimelineItemInfo timelineItemInfo) {
        Ticket ticket = findTicket(ticketId);
        TimelineItem item = new TimelineItem();
        item.setDate(timelineItemInfo.getDate());
        item.setItemType(timelineItemInfo.getItemType());
        item.setTicket(ticket);
        ticket.getTimelineItems().add(item);

        try {
            ticketDAO.update(ticket);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Actualizar Ticket".toUpperCase());
        }
    }

    private Ticket findTicket(long ticketId) {
        Ticket ticket = new Ticket();
        try {
            ticket = ticketDAO.find(ticketId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Recuperar Ticket".toUpperCase());
        }
        return ticket;
    }

    private TimelineItem findTimeline(long itemId) {
        TimelineItem item = new TimelineItem();
        try {
            item = timelineItemDAO.find(itemId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Recuperar TimeLineInfo".toUpperCase());
        }
        return item;
    }

    /**
     * @author Edgêncio da Calista
     * @param ticketId
     * @param technicianId
     * @return
     */
    public boolean assignTechnician(long ticketId, long technicianId) {
        Technitian tec;
        Ticket tick;
        try {
            TicketStatusEnum status = TicketStatusEnum.OPENED;
            tec = technicianDAO.find(technicianId);
            tick = ticketDAO.find(ticketId);
            tick.setTechnician(tec);
            tick.setStatus(status);
            ticketDAO.update(tick);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

  /**
   * 
   * @param customerId
   * @param skip
   * @param limit
   * @param category
   * @param technician
   * @param startDate
   * @return 
   */
    public Collection<TicketInfo> getByCustomerfullSearch(long customerId, int skip, int limit, String category, String technician, String startDate) {
        List<TicketInfo> clientHistory = new ArrayList();
        List<Ticket> tickets = new ArrayList();

        try {
            tickets = ticketDAO.getByCustomerfullSearch(customerId, skip, limit, category, technician, startDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }
        clientHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientHistory;
    }

    
    /**
     * @param supplierId
     * @param skip
     * @param limit
     * @param category
     * @param technician
     * @param startDate
     * @return 
     */
      public Collection<TicketInfo> getBySupplierFullSearch(long customerId, int skip, int limit, String category, String technician, String startDate) {
        List<TicketInfo> clientHistory = new ArrayList();
        List<Ticket> tickets = new ArrayList();

        try {
            tickets = ticketDAO.getBySupplierFullSearch(customerId, skip, limit, category, technician, startDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a Levar informacao do cliente".toUpperCase());
        }
        clientHistory = Middleware.ticketInfoInfoList(tickets, addressDAO, consumerDAO, technicianDAO);
        return clientHistory;
    }
    
}
