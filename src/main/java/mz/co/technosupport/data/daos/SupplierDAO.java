/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Supplier;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Adelino José
 */

@ApplicationScoped
public class SupplierDAO extends DAO<Supplier> {

    @Override
    public Class<Supplier> getEntityClass() {
        return Supplier.class;
    }
    
}
