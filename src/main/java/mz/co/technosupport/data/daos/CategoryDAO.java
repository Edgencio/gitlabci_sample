/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Category;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author zJohn
 */
@ApplicationScoped
public class CategoryDAO extends DAO<Category> {

    @Override
    public Class<Category> getEntityClass() {
        return Category.class;
    }
    
}
