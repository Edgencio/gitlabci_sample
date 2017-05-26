/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.model.Technitian;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.DistanceSortField;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author Anselmo
 */
@ApplicationScoped
public class AddressDAO extends DAO<Address> {

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
    
    public List<Address> getAllBySupplierId(long supplierId) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Address> addresses = null;
        try{
            
            Query query = em.createQuery("select p from Address p where p.supplier.id =:supplier_id and p.disabled=false ");
            query.setParameter("supplier_id",supplierId);
            
            addresses = query.getResultList();
            if(addresses.size()>0){
                return addresses;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Falha ao recuperar dados na Base de Dados");
        }finally {
            if(em!=null)
                em.close();
        }
        return addresses;
    }
    
    public List<Address> getAllByCustomerId(long customer) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Address> addresses = null;
        try{
            
            Query query = em.createQuery("select p from Address p where p.customer.id =:customer_id and p.disabled=false ");
            query.setParameter("customer_id",customer);
            
            addresses = query.getResultList();
            if(addresses.size()>0){
                return addresses;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException("Falha ao recuperar dados na Base de Dados");
        }finally {
            if(em!=null)
                em.close();
        }
        return addresses;
    }

    public List<Address> getNearest(double lat, double lng){

        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        FullTextEntityManager fullTextSession= Search.getFullTextEntityManager(em);

        QueryBuilder builder = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Address.class).get();

        org.apache.lucene.search.Query luceneQuery = builder
                .spatial()
                //.onField("currentAddress")
                .within(100, Unit.KM)
                .ofLatitude(lat)
                .andLongitude(lng)
                .createQuery();

        FullTextQuery hibQuery = fullTextSession.createFullTextQuery(luceneQuery, Address.class);

        /*Sort distanceSort = new Sort(
                new DistanceSortField(lat, lng, "latitude"));
        hibQuery.setSort(distanceSort);*/

        return hibQuery.setMaxResults(100).getResultList();
    }
    
}
