/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.model;

import mz.co.technosupport.info.util.TimelineItemTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author zJohn
 */
@Entity
public class TimelineItem implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Enumerated(EnumType.STRING)
    private TimelineItemTypeEnum itemType;

    @ManyToOne
    private Ticket ticket;
    
    
    public TimelineItem(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TimelineItemTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(TimelineItemTypeEnum itemType) {
        this.itemType = itemType;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    
    
}
