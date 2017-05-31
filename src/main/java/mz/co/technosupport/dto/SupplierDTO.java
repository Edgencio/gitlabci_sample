/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.dto;

import mz.co.technosupport.data.model.Address;

/**
 *
 * @author Edgencio da Calista
 */
public class SupplierDTO {

    private long supplierID;
    private String supplierName;
    private String supplierMail;
    private Address supplierAddress;
    private String supplierPhone;
    private String supplierType;

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String suppliertype) {
        this.supplierType = suppliertype;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierrID) {
        this.supplierID = supplierrID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierMail() {
        return supplierMail;
    }

    public void setSupplierMail(String supplierMail) {
        this.supplierMail = supplierMail;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public Address getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(Address suppplierAddress) {
        this.supplierAddress = suppplierAddress;
    }

}
