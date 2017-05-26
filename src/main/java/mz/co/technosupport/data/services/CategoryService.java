/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.services;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.technosupport.data.daos.CategoryDAO;
import mz.co.technosupport.data.model.Category;
import mz.co.technosupport.data.model.Supplier;

/**
 *
 * @author Edgêncio da Calista
 */
@ApplicationScoped
public class CategoryService {

    @Inject
    CategoryDAO categoryDAO;

    public List<String> getCategories() {
        List<String> categories = new ArrayList();
        List<Category> cats = null;
        try {
            cats = categoryDAO.getAll();
            for (Category cat : cats) {
                categories.add(cat.getTitle());
            }
            return categories;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
