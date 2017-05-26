/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Ticket;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import mz.co.technosupport.info.util.TicketStatusEnum;

/**
 *
 * @author zJohn
 */
@SuppressWarnings("ALL")
@ApplicationScoped
public class TicketDAO extends DAO<Ticket> {

    @Override
    public Class<Ticket> getEntityClass() {
        return Ticket.class;
    }

    public List<Ticket> getByConsumerIdAndSkipLimit(long clientId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();
        String sqlQuery = "select t from Ticket t where consumer.id=" + clientId + " order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    
    
    /**
     * @author Edgêncio da Calista
     * @param customerId
     * @param skip
     * @param limit
     * @return
     * @throws Exception 
     */
     public List<Ticket> getByCustomerIdAndSkipLimit(long customerId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();
        String sqlQuery = "select t from Ticket t where consumer.customer.id=" + customerId + " order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    

    /**
     * @author Edgencio da Calista
     * @param clientId
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Ticket> getPendingByCostumerIdAndSkipLimit(long customerId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();
        TicketStatusEnum status = TicketStatusEnum.PENDING;
        String sqlQuery = "select t from Ticket t where consumer.customer.id=" + customerId + " and t.status=:status order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    
    
      /**
     * @author Edgencio da Calista
     * @param technicianId
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */
    public List<Ticket> getPendingBySupplierIdAndSkipLimit(long supplierId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();
        TicketStatusEnum status = TicketStatusEnum.PENDING;
        String sqlQuery = "select t from Ticket t where technician.supplier.id=" + supplierId + " and t.status=:status order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    
    
    /**
     * @author Edgencio da Calista
     * @param  clientId
     * @return totalRows (long)
     * @throws Exception 
     */
    public long countClientTickets(long clientId) throws Exception {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        String stringQuery = "select count(t) from Ticket t where consumer.id=" + clientId + "";
        Long totalRows = null;

        try {
            Query query = em.createQuery(stringQuery);
            totalRows = (Long) query.getSingleResult();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Falha ao tentar buscar tickets!");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return totalRows;

    }
    
        /**
     * @author Edgencio da Calista
     * @param  technicianId
     * @return totalRows (long)
     * @throws Exception 
     */
    public long countSupplierTickets(long supplierId) throws Exception {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        String stringQuery = "select count(t) from Ticket t where technician.supplier.id=" + supplierId + "";
        Long totalRows = null;

        try {
            Query query = em.createQuery(stringQuery);
            totalRows = (Long) query.getSingleResult();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Falha ao tentar buscar tickets!");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return totalRows;

    }
    
    

    public List<Ticket> getByTechnicianIdAndSkipLimit(long technicianId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();
        String sqlQuery = "select t from Ticket t where technician.id=" + technicianId + " order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do tecnico!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ticket> getBySupplierIdSkipLimit(long supplierId, int skip, int limit) throws Exception {
        List<Ticket> tickets = new ArrayList();

        String sqlQuery = "select t from Ticket t where technician.supplier.id=" + supplierId + " order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do tecnico!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
      
    
     /**
      * 
      * @param customerId
      * @param skip
      * @param limit
      * @param category
      * @param techncian
      * @param startDate
      * @param endDate
      * @return
      * @throws Exception 
      */
     public List<Ticket> getByCustomerfullSearch(long customerId, int skip, int limit, String category, String technician, String startDate) throws Exception {
        List<Ticket> tickets = new ArrayList();
        java.sql.Date data=null;
        if(startDate==null||startDate==" "|| startDate==""){
          data=java.sql.Date.valueOf("2017-01-01");
        }else{
        data=java.sql.Date.valueOf(startDate);
        }
       
     
        String sqlQuery = "select t from Ticket t where consumer.customer.id=" + customerId + " and t.category.title like :cat and t.technician.supplier.name like :tec and t.date > :dt order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            query.setParameter("cat", "%"+category+"%");
            query.setParameter("tec", "%"+technician+"%");
            query.setParameter("dt", data);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
     
     
     
     
        public List<Ticket> getBySupplierFullSearch(long supplierId, int skip, int limit, String category, String customer, String startDate) throws Exception {
        List<Ticket> tickets = new ArrayList();
        java.sql.Date data=null;
        if(startDate==null||startDate==" "|| startDate==""){
          data=java.sql.Date.valueOf("2017-01-01");
        }else{
        data=java.sql.Date.valueOf(startDate);
        }
       
     
        String sqlQuery = "select t from Ticket t where technician.supplier.id=" + supplierId + " and t.category.title like :cat and t.consumer.customer.name like :tec and t.date > :dt order by t.date desc";
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        try {
            Query query = em.createQuery(sqlQuery).setFirstResult(skip).setMaxResults(limit);
            query.setParameter("cat", "%"+category+"%");
            query.setParameter("tec", "%"+customer+"%");
            query.setParameter("dt", data);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha ao tentar buscar Dados do cliente!".toUpperCase());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
     
     
     
     
     public Map fetchTicketsBySupplier( long supplierId, int pageNumber, int itemsPerPage, Map filter, Map ordering) throws Exception {
        Map results = new HashMap();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            pageNumber--;

            String query = " ";

            TypedQuery<Long> queryCount = em.createQuery("select count(m) from " + getEntityClass().getSimpleName() + " m " + query, Long.class);
            Query querySelect = em.createQuery("select m from " + getEntityClass().getName() + " m where technician.supplier.id=" +supplierId+" ORDER BY m.id DESC");

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

    
    
     
     public Map fetchTicketsByCustomer( long customerId, int pageNumber, int itemsPerPage, Map filter, Map ordering) throws Exception {
        Map results = new HashMap();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            pageNumber--;

            String query = " ";

            TypedQuery<Long> queryCount = em.createQuery("select count(m) from " + getEntityClass().getSimpleName() + " m " + query, Long.class);
            Query querySelect = em.createQuery("select m from " + getEntityClass().getName() + " m where consumer.customer.id="+customerId+ " ORDER BY m.id DESC");

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
