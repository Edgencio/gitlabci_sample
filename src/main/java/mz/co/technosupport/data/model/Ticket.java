/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.model;

import mz.co.technosupport.info.util.TicketStatusEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zJohn
 */

@Entity
public class Ticket implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String name;
    
    @OneToOne
    private Category category;

    @OneToOne
    private Consumer consumer;

    @OneToOne
    private Address clientAddress;

    @OneToOne
    private Technitian technician;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnum status;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Picture> pictures;

    private String diagnostic;

    private String solution;

    private boolean solved;
    
    private String messageConsumer;
    
    private String messageTechnician;
    
    private double rating;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket", fetch = FetchType.EAGER)
    private Collection<TimelineItem> timelineItems;
    
    public Ticket(){
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Technitian getTechnician() {
        return technician;
    }

    public void setTechnician(Technitian technician) {
        this.technician = technician;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TicketStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TicketStatusEnum status) {
        this.status = status;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public boolean getSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Collection<TimelineItem> getTimelineItems() {
        return timelineItems;
    }

    public void setTimelineItems(Collection<TimelineItem> timelineItems) {
        this.timelineItems = timelineItems;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMessageConsumer() {
        return messageConsumer;
    }

    public void setMessageConsumer(String messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public String getMessageTechnician() {
        return messageTechnician;
    }

    public void setMessageTechnician(String messageTechnician) {
        this.messageTechnician = messageTechnician;
    }
    
    
}
