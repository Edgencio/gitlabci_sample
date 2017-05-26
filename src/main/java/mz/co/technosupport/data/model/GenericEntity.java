/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.model;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 
 * @author Romildo Cumbe
 */
@MappedSuperclass
public class GenericEntity implements Serializable{
    
    private static final long serialVersionUID = -6468535868748071777L;

    public GenericEntity() {
        this.status = 0;
    }
    
    private Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    
}
