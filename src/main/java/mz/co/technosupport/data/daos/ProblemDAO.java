/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Problem;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author zJohn
 */
@ApplicationScoped
public class ProblemDAO extends DAO<Problem> {

    @Override
    public Class<Problem> getEntityClass() {
        return Problem.class;
    }

    public List<Problem> findByCategoryId(long categoryId) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Problem> problems = null;
        try{
            
            Query query = em.createQuery("select p from " + getEntityClass().getName()+ " p where p.category.id =:category_id");       
            query.setParameter("category_id",categoryId);
            
            problems = query.getResultList();
            if(problems.size()>0){
                return problems;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Falha ao recuperar dados na Base de Dados");
        }finally {
            if(em!=null)
                em.close();
        }
        
        return problems;
    }
    
}
