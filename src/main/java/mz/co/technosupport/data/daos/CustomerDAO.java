/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Customer;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Anselmo
 */
@ApplicationScoped
public class CustomerDAO extends DAO<Customer> {

    @Override
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }
    
}
