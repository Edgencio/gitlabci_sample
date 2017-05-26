/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.model;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Date;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;

/**
 *
 * @author Romildo Cumbe
 */
@Entity
@Indexed
@Spatial
public class Technitian extends GenericEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    
  
    @OneToOne
    @JoinColumn(name = "current_address_id")
    private Address currentAddress;

    private boolean available;
    
    @Latitude
    private Double latitude;
    
    @Longitude
    private Double longitude;

    private boolean isAdmin;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created_at;

    private boolean disabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    
    public double getLatitude( ){
        return latitude;
    }
    
    public void setLatitude( double latitude){
        this.latitude=latitude;
    }
    
    public double getLongitude( ){
        return longitude;
    }
    
    public void setlongitude(double longitude){
     this.longitude=longitude;   
       
    }
}
