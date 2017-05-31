/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import java.util.HashMap;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Customer;
import mz.co.technosupport.data.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import mz.co.technosupport.data.model.Technitian;

/**
 *
 * @author Anselmo
 */
@ApplicationScoped
public class ConsumerDAO extends DAO<Consumer> {
    
     
    @Override
    public Class<Consumer> getEntityClass() {
        return Consumer.class;
    }

    public Consumer findConsumer(Customer customer, User usr) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Consumer> members = null;
        try{
            
            Query query = em.createQuery("select c from " + getEntityClass().getName()+" c where c.customer.id = :cs and c.user.id =:us");
            query.setParameter("cs",customer.getId());
            query.setParameter("us",usr.getId());
            
            members = query.getResultList();
            if(members.size()>0){
                return members.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return null;
    }
    
    
    
        /**
     * @author Edgencio da Calista
     * @param usr
     * @return Consumer
     */
    public Consumer findAdminConsumer(User usr) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Consumer> members = null;
        try {
            Query query = em.createQuery("select c from " + getEntityClass().getName() + " c where c.user.id=:ui and c.isAdmin=true");
            query.setParameter("ui", usr.getId());
            members = query.getResultList();
            if (members.size() > 0) {
                return members.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
    
    
    

    public List<Consumer> getAffiliates(Long customer_id) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Consumer> members = null;
        try{
            
            Query query = em.createQuery("select c from Consumer c where c.customer.id = :cs  and c.isAdmin = false and c.disabled = false ");
            query.setParameter("cs",customer_id);
            
            members = query.getResultList();
            if(members.size()>0){
                return members;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return null;
    }

    
    
    
    public List<String> getNonAffiliates(Long customer_id) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<String> members = null;
        try{
            
            Query query = em.createQuery("select c.user.email from Consumer c where c.customer.id != :cs  and c.isAdmin = false");
            query.setParameter("cs",customer_id);
            
            members = query.getResultList();
            if(members.size()>0){
                return members;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return null;
    }
    
    
    public Consumer getAffiliateByUser(long customerId, long userId) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Consumer member = null;
        try{

            Query query = em.createQuery("select c from Consumer c where c.customer.id = :cs  and c.user.id = :uid", Consumer.class);
            query.setParameter("cs",customerId);
            query.setParameter("uid", userId);

            member = (Consumer) query.getSingleResult();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return member;
    }

    public List<Consumer> getAllByUserId(long userId) {

        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Consumer> members = null;
        try{

            members = em.createQuery("select c from Consumer c where c.disabled = false  and c.user.id = :cs ", Consumer.class)
            .setParameter("cs",userId).getResultList();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return members;

    }

    public Consumer getByUserId(long userId){
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Consumer consumer = null;
        try{
            consumer = em.createQuery("select c from Consumer c where c.user.id = :cs ", Consumer.class)
                    .setParameter("cs",userId).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return consumer;
    }
    
    
    public Consumer getByEmail(String email){
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Consumer consumer = null;
        try{
            consumer = em.createQuery("select c from Consumer c where c.user.email = :ue ", Consumer.class)
                    .setParameter("ue",email).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
        return consumer;
    }
    
    public boolean removeAffiliate(Customer customer, User usr) {
        /*EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Consumer> members = null;
        try{
            
            Query query = em.createQuery("delete c from " + getEntityClass().getName()+" c where c.customer.id = :cs and c.user.id =:us");
            query.setParameter("cs",customer.getId());
            query.setParameter("us",usr.getId());
            
            members = query.getResultList();
            if(members.size()>0){
//                return members.get(0);
                  return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(em!=null)
                em.close();
        }
//        return null;
           return false;*/
        return false;
    }
    
    
    public Map fetchAffiliatesByCustomer( long customerId, int pageNumber, int itemsPerPage, Map filter, Map ordering) throws Exception {
        Map results = new HashMap();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            pageNumber--;

            String query = " ";

            TypedQuery<Long> queryCount = em.createQuery("select count(m) from "+ getEntityClass().getSimpleName() + " m where m.customer.id = "+customerId+"  and m.isAdmin = false and m.disabled = false", Long.class);
            Query querySelect = em.createQuery("select c from Consumer c where c.customer.id = "+customerId+"  and c.isAdmin = false and c.disabled = false ");

            int totalRows = queryCount.getResultList().get(0).intValue();
            results.put("total", totalRows);

            results.put("list", querySelect.setFirstResult(pageNumber * itemsPerPage).setMaxResults(itemsPerPage).getResultList());

            results.put("pages", (int) Math.ceil(totalRows / ((Integer) itemsPerPage).doubleValue()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed trying to Get DATA");
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return results;

    }
    
    
    
}
