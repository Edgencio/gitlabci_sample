/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author zJohn
 */
@Embeddable
public class SupplierInfo implements Serializable{
    
    private String title;
    private double rating;
    private String about;
    private String skills;
    private String accountType;
    
    public SupplierInfo(){
        this.title = "";
        this.rating = 0;
        this.about = "";
        this.skills = "";
        this.accountType = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    
    
}
