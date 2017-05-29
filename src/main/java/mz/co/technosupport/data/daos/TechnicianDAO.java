/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.data.model.User;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.DistanceSortField;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import mz.co.technosupport.data.model.Consumer;

/**
 *
 * @author zJohn
 */
@ApplicationScoped
public class TechnicianDAO extends DAO<Technitian> {

    @Override
    public Class<Technitian> getEntityClass() {
        return Technitian.class;
    }

    public Technitian findTechnician(Supplier supplier, User usr) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try {

            Query query = em.createQuery("select t from " + getEntityClass().getName() + " t where t.user.id =:us and  t.supplier.id =:as ");
            query.setParameter("us", usr.getId());
            query.setParameter("as", supplier.getId());

            members = query.getResultList();
            if (members.size() > 0) {
                return members.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na Base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    /**
     * @author Edgencio da Calista
     * @param usr
     * @return Technician
     */
    public Technitian findAdminTechnician(User usr) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try {
            Query query = em.createQuery("select t from " + getEntityClass().getName() + " t where t.user.id=:ui and t.isAdmin=true");
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
    
    
    
    
      public List<Technitian> getAffiliates(Long supplier_id) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try{
            
            Query query = em.createQuery("select t from Technitian t where t.supplier.id = :si  and t.isAdmin = false and t.disabled = false ");
            query.setParameter("si",supplier_id);
            
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
    

    public List<Technitian> getAllBySuppleir(Long supplier_id) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try {

            Query query = em.createQuery("select t from Technitian t where t.supplier.id =:as and t.disabled=false and t.isAdmin=false ");
            query.setParameter("as", supplier_id);

            members = query.getResultList();
            if (members.size() > 0) {
                return members;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na Base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public List<Technitian> getAllByUser(long userId) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try {

            members = em.createQuery("select t from Technitian t where t.disabled = false  and t.user.id =:as ", Technitian.class)
                    .setParameter("as", userId).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na Base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return members;
    }

    public Technitian getAffiliateByUser(long customerId, long userId) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Technitian member = null;
        try {

            Query query = em.createQuery("select c from Technitian c where c.supplier.id = :cs  and c.user.id = :uid", Technitian.class);
            query.setParameter("cs", customerId);
            query.setParameter("uid", userId);

            member = (Technitian) query.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return member;
    }


    public List<Technitian> getNearestTechnicians(double latitude, double longitude) {

        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        List<Technitian> final_list = new ArrayList<Technitian>();

        try {

            members = em.createQuery("select t from Technitian t where t.disabled=false and t.user.name <> ''", Technitian.class)
                    .getResultList();

            for (Technitian tec : members) {

                if (tec.getCurrentAddress().getLatitude() != null && tec.getCurrentAddress().getLongitude() != null) {

                    double distance = getDistance(latitude, longitude, tec.getCurrentAddress().getLatitude(), tec.getCurrentAddress().getLongitude());

                    if (distance <= 100) {
                        final_list.add(tec);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na Base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return bubbleSort(final_list, latitude, longitude);
        //return final_list;

    }

    public List<Technitian> bubbleSort(List<Technitian> array, double latitude, double longitude) {
        int real_array_size = array.size() - 1;

        try {
            for (int i = 0; i < array.size(); i++) {
                for (int j = 0; j < real_array_size; j++) {
                    double distance1 = getDistance(latitude, longitude, array.get(j).getCurrentAddress().getLatitude(), array.get(j).getCurrentAddress().getLongitude());
                    double distance2 = getDistance(latitude, longitude, array.get(j + 1).getCurrentAddress().getLatitude(), array.get(j + 1).getCurrentAddress().getLongitude());

                    if (distance1 > distance2) {

                        Technitian tech_aux, tech_second;

                        tech_aux = array.get(j);
                        tech_second = array.get(j + 1);

                        array.add(j, tech_second);
                        array.remove(j + 1);
                        array.add(j + 1, tech_aux);
                        array.remove(j + 2);

                    }

                }

            }
        } catch (Exception z) {
            z.printStackTrace();
        }

        return array;
    }

    public List<Technitian> owngetNearestTechnicians(double latitude, double longitude) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(em);

        QueryBuilder builder = fullTextSession.getSearchFactory()
                .buildQueryBuilder().forEntity(Technitian.class).get();

        org.apache.lucene.search.Query luceneQuery = builder
                .spatial()
                .within(1000000, Unit.KM)
                .ofLatitude(latitude)
                .andLongitude(longitude)
                .createQuery();

        FullTextQuery hibQuery = fullTextSession.createFullTextQuery(luceneQuery, Technitian.class);

        /*  Sort distanceSort = new Sort(
                new DistanceSortField(latitude, longitude, "currentAddress"));
        hibQuery.setSort(distanceSort);*/
        return hibQuery.setMaxResults(10).getResultList();
    }

    public boolean removeTechnician(Supplier supplier, User usr) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<Technitian> members = null;
        try {

            Query query = em.createQuery("delete  from " + getEntityClass().getName() + " t where t.supplier.id =:sp and t.user.id =:us");
            query.setParameter("sp", supplier.getId());
            query.setParameter("us", usr.getId());

            int rowsDeleted = query.executeUpdate();
            members = query.getResultList();
//            if(members.size()>0){
            if (rowsDeleted > 0) {
//                return members.get(0);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
//        return null;
        return false;
    }

    public Technitian getByUserId(long l) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Technitian technician = null;
        try {
            technician = em.createQuery("select c from Technitian c where c.user.id = :cs ", Technitian.class)
                    .setParameter("cs", l).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return technician;
    }

    public Technitian getByTechnicianId(long l) {
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        Technitian technician = null;
        try {
            technician = em.createQuery("select c from Technitian c where c.id = :cs ", Technitian.class)
                    .setParameter("cs", l).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return technician;
    }

    public double getDistance(double lat_start, double lng_start, double lat_end, double lng_end) {

        Coordinate lat1 = new DegreeCoordinate(lat_start);
        Coordinate lng1 = new DegreeCoordinate(lng_start);
        Point position1 = new Point(lat1, lng1);

        //Richmond, London
        lat1 = new DegreeCoordinate(lat_end);
        lng1 = new DegreeCoordinate(lng_end);
        Point position2 = new Point(lat1, lng1);

        double distance = EarthCalc.getDistance(position2, position1); //in meters

        return (distance / 1000);
    }
    
    
    
      public Map fetchAffiliatesBySupplier( long supplierId, int pageNumber, int itemsPerPage, Map filter, Map ordering) throws Exception {
        Map results = new HashMap();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            pageNumber--;

            String query = " ";

            TypedQuery<Long> queryCount = em.createQuery("select count(m) from Technitian m where m.supplier.id = "+supplierId+"  and m.isAdmin = false and m.disabled = false ", Long.class);
            Query querySelect = em.createQuery("select t from Technitian t where t.supplier.id = "+supplierId+"  and t.isAdmin = false and t.disabled = false ");

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
