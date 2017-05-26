/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.util;

import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.model.*;
import mz.co.technosupport.info.AddressInfo;
import mz.co.technosupport.info.AffiliateInfo;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.info.help.CategoryInfo;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.info.ticket.TimelineItemInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author zJohn
 */
@SuppressWarnings("ALL")
public class Middleware {
    
    public static ClientAccountInfo clientAccountInfoMethod(Consumer consumer, AddressDAO addressDAO, ConsumerDAO consumerDAO){
        ClientAccountInfo clientAccountInfo = new ClientAccountInfo();
        
        clientAccountInfo.setId(consumer.getId());
        clientAccountInfo.setType(consumer.getCustomer().getType());

        if(consumer.getCurrentAddress()!=null){
            clientAccountInfo.setCurrentAddress(addressInfoMethod(consumer.getCurrentAddress()));
        }


        clientAccountInfo.setAddresses(addressInfoList(addressDAO.getAllByCustomerId(consumer.getCustomer().getId())));



        clientAccountInfo.setAdmin(consumer.isIsAdmin());
        if(consumer.isIsAdmin()){
            clientAccountInfo.setAffiliates(affiliateInfoList(consumerDAO.getAffiliates(consumer.getCustomer().getId()), null));
        }

        clientAccountInfo.setAccountType("client");
        clientAccountInfo.setEmail(consumer.getCustomer().getEmail());
        clientAccountInfo.setName(consumer.getCustomer().getName());
        clientAccountInfo.setPhone(consumer.getCustomer().getMobile());
        clientAccountInfo.setFcmToken(consumer.getUser().getFcmToken());
        clientAccountInfo.setProfilePicture(consumer.getCustomer().getImageUrl());

        return clientAccountInfo;
    }
    
    public static AddressInfo addressInfoMethod(Address address){
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(address.getId());
        addressInfo.setName(address.getName());
        addressInfo.setLatitude(address.getLatitude());
        addressInfo.setLongitude(address.getLongitude());
        
        return addressInfo;
    }
    
    public static TicketInfo ticketInfoMethod(Ticket ticket, AddressDAO addressDAO, ConsumerDAO consumerDAO, TechnicianDAO technicianDAO){
        TicketInfo ticketInfo = new TicketInfo();

        ticketInfo.setName(ticket.getName());
        ticketInfo.setCategory(categoryInfoMethod(ticket.getCategory()));
        ticketInfo.setDate(ticket.getDate());
        ticketInfo.setDiagnostic(ticket.getDiagnostic());
        ticketInfo.setId(ticket.getId());

        if(ticket.getPictures()!=null){
            String[] pictures = new String[ticket.getPictures().size()];

            int i = 0;

            for(Picture picture :ticket.getPictures()){
                pictures[i] = picture.getName();
                i++;
            }

            ticketInfo.setPictures(pictures);
        }

        ticketInfo.setSolution(ticket.getSolution());
        if(ticket.getSolved()==true){
            ticketInfo.setSolved(1);
        } else {
            ticketInfo.setSolved(0);
        }

        ticketInfo.setStatus(ticket.getStatus());

        ticketInfo.setClient(clientAccountInfoMethod(ticket.getConsumer(), addressDAO, consumerDAO));

        ticketInfo.setClientAddress(addressInfoMethod(ticket.getClientAddress()));


        if(ticket.getTimelineItems()!=null && ticket.getTimelineItems().size()>0){
            ticketInfo.setTimelineItems(timelineItemInfoList(ticket.getTimelineItems()));
        }

        if(ticket.getTechnician()!=null){
            ticketInfo.setTechnician(technicianAccountInfoMethod(ticket.getTechnician(), addressDAO, technicianDAO));
        }

        return ticketInfo;
    }
    
    public static CategoryInfo categoryInfoMethod(Category category){
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setIcon(category.getIconPath());
        categoryInfo.setId(category.getId());
        categoryInfo.setName(category.getTitle());        
        
        return categoryInfo;        
    }
    
    public static TimelineItemInfo timelineItemInfoMethod(TimelineItem timelineItem){
        TimelineItemInfo timelineItemInfo = new TimelineItemInfo();
        timelineItemInfo.setId(timelineItem.getId());
        timelineItemInfo.setDate(timelineItem.getDate());
        timelineItemInfo.setItemType(timelineItem.getItemType());
        
        return timelineItemInfo;
    }
    
    public static TechnicianAccountInfo technicianAccountInfoMethod(Technitian technitian, AddressDAO addressDAO, TechnicianDAO technicianDAO){
        TechnicianAccountInfo technicianAccountInfo = new TechnicianAccountInfo();
        
        technicianAccountInfo.setId(technitian.getId());
        technicianAccountInfo.setAbout(technitian.getSupplier().getSupplierInfo().getAbout());
        technicianAccountInfo.setAccountType(technitian.getSupplier().getSupplierInfo().getAccountType());
        technicianAccountInfo.setAddresses(addressInfoList(addressDAO.getAllBySupplierId(technitian.getSupplier().getId())));

        technicianAccountInfo.setAdmin(technitian.isIsAdmin());
        if(technitian.isIsAdmin()){
            technicianAccountInfo.setAffiliates(affiliateInfoList(null, technicianDAO.getAllBySuppleir(technitian.getSupplier().getId())));
        }

        technicianAccountInfo.setAvailable(technitian.isAvailable());
        technicianAccountInfo.setCurrentAddress(addressInfoMethod(technitian.getCurrentAddress()));
        
        technicianAccountInfo.setEmail(technitian.getSupplier().getEmail());
        technicianAccountInfo.setName(technitian.getSupplier().getName());
        technicianAccountInfo.setUserName(technitian.getUser().getName());
        technicianAccountInfo.setType(technitian.getSupplier().getType());

        technicianAccountInfo.setAccountType("technician");
        technicianAccountInfo.setFcmToken(technitian.getUser().getFcmToken());
        technicianAccountInfo.setPhone(technitian.getSupplier().getMobile());
        technicianAccountInfo.setProfilePicture(technitian.getSupplier().getImageUrl());
        technicianAccountInfo.setRating(technitian.getSupplier().getSupplierInfo().getRating());
        technicianAccountInfo.setSkills(technitian.getSupplier().getSupplierInfo().getSkills());
        technicianAccountInfo.setTitle(technitian.getSupplier().getSupplierInfo().getTitle());
        

        
        return technicianAccountInfo;
    }
    
    
    public static AffiliateInfo affiliateInfoMethod(Consumer consumer, Technitian technician){
        AffiliateInfo affiliateInfo = new AffiliateInfo();
        if(technician == null && consumer!=null){
            affiliateInfo.setId(consumer.getId());
            affiliateInfo.setEmail(consumer.getUser().getEmail());
            affiliateInfo.setName(consumer.getUser().getName());
        }

        if(consumer == null && technician!=null){
            affiliateInfo.setId(technician.getId());
            affiliateInfo.setEmail(technician.getUser().getEmail());
            affiliateInfo.setName(technician.getUser().getName());
        }
        
        return affiliateInfo;
    }
    
    public static Collection<AddressInfo> addressInfoList(Collection<Address> addresses){
        List<AddressInfo> addressInfoList = new ArrayList<>();
        if(addresses!=null && addresses.size()>0){
            for (Address address : addresses) {
                addressInfoList.add(addressInfoMethod(address));
            }
        }
        return addressInfoList;
    }
    
    public static Collection<TimelineItemInfo> timelineItemInfoList(Collection<TimelineItem> timelineItems){
        List<TimelineItemInfo> timelineItemInfoList = new ArrayList<>();
        for (TimelineItem item : timelineItems) {
            timelineItemInfoList.add(timelineItemInfoMethod(item));
        }
        return timelineItemInfoList;
    }
    
    public static List<TicketInfo> ticketInfoInfoList(Collection<Ticket> tickets, AddressDAO addressDAO, ConsumerDAO consumerDAO, TechnicianDAO technicianDAO){
        List<TicketInfo> ticketInfoInfoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketInfoInfoList.add(ticketInfoMethod(ticket, addressDAO, consumerDAO, technicianDAO));
        }
        return ticketInfoInfoList;
    }
    
    public static Collection<AffiliateInfo> affiliateInfoList(Collection<Consumer> consumers, Collection<Technitian> technitians){
        List<AffiliateInfo> affiliateInfoList = new ArrayList<>();
        if(consumers == null && technitians!=null && technitians.size()>0){
            for (Technitian technitian : technitians) {
                affiliateInfoList.add(affiliateInfoMethod(null, technitian));
            }
        }

        if(technitians == null && consumers!=null && consumers.size()>0){
            for (Consumer consumer : consumers) {
                affiliateInfoList.add(affiliateInfoMethod(consumer, null));
            }
        }       
        return affiliateInfoList;
    }
}
