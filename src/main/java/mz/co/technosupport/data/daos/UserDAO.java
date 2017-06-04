/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.daos;

import mz.co.technosupport.data.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author Anselmo
 */
@ApplicationScoped
public class UserDAO extends DAO<User> {

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public User getUserByCredentials(String username, String password) {
        User user = new User();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            user = em.createQuery("select t from User t where t.username =:username and  t.password =:password ", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password).setMaxResults(1).getSingleResult();

        } catch (NoResultException e) {
            user = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return user;
    }
    
    
       public User getUserByPassword(long user_id , String password) {
        User user = new User();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();

        try {
            user = em.createQuery("select u from User u where u.id =:user_id and  u.password =:password ", User.class)
                    .setParameter("user_id", user_id)
                    .setParameter("password", password).setMaxResults(1).getSingleResult();

        } catch (NoResultException e) {
            user = null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return user;
    }
    

    public User verifyValidResetCode(String username, String code) {
        User user = new User();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<User> members = null;
        try {
            Query query = em.createQuery("select t from " + getEntityClass().getName() + " t where t.username =:username and  t.passwordResetCode =:code ");
            query.setParameter("username", username);
            query.setParameter("code", code);

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

        return user;
    }
    
    

    public User getUserByEmail(String email){
        User user = null;
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<User> members = null;
        try {
            Query query = em.createQuery("select t from " + getEntityClass().getName() + " t where t.email =:email");
            query.setParameter("email", email);

            members = query.getResultList();
            if (members.size() > 0) {

                return user=members.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return user;
    }
    
    
     public List<String> getUserEmails(){
        User user = new User();
        EntityManager em = DAO.getEntityManagerFactory().createEntityManager();
        List<String> members = null;
        try {
            Query query = em.createQuery("select u.email from " + getEntityClass().getName() + " u ");
            members = query.getResultList();
            if (members.size() > 0) {

                return members;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao recuperar dados na Base de Dados");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return members;
    }
    

}
