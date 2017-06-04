package mz.co.technosupport.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Romildo Cumbe 
 */
@Entity
public class Supplier extends GenericEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    
    @NotNull
    private String email;
    
    @NotNull
    private String imageUrl;

    private String phone;
    
    private String mobile;
    
    @Lob
    private String notes;
    
    @NotNull
    private String type;
    
    private String address;
    
    private String linkedInProfile;
     
    @Embedded
    private SupplierInfo supplierInfo;
     

    public Supplier() {
        this.phone = "";
        this.mobile = "";
        this.notes = "";
        this.type = "";
        this.address = "";
        this.supplierInfo = new SupplierInfo();
    }

    public String getLinkedInProfile() {
        return linkedInProfile;
    }

    public void setLinkedInProfile(String linkedInProfile) {
        this.linkedInProfile = linkedInProfile;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SupplierInfo getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(SupplierInfo supplierInfo) {
        this.supplierInfo = supplierInfo;
    }


    
    
}
