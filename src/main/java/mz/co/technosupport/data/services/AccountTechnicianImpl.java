/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.data.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.SupplierDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.*;
import mz.co.technosupport.info.AffiliateInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.service.AccountTechnicianService;
import mz.co.technosupport.util.Middleware;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@ApplicationScoped
public class AccountTechnicianImpl implements AccountTechnicianService {


    @Inject
    AddressDAO addressDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    SupplierDAO supplierDAO;

    @Inject
    TechnicianDAO technicianDAO;

    @Override
    public TechnicianAccountInfo getById(long l) {
        TechnicianAccountInfo technicianAccountInfo = new TechnicianAccountInfo();

        try {
            Technitian technitian = technicianDAO.find(l);
            if(technitian==null) throw new NoResultException("No valid user with id " +l+" found");

            technicianAccountInfo = Middleware.technicianAccountInfoMethod(technitian, addressDAO, technicianDAO);
            technicianAccountInfo.setFcmToken(technitian.getUser().getFcmToken());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return technicianAccountInfo;
    }

    @Override
    public void addAddress(long technicianId, String string, double d, double d1) {

        try {
           
            
            Technitian technician= technicianDAO.find(technicianId);
            
            Address address = new Address();
            address.setSupplier(technician.getSupplier());
            address.setName(string);
            address.setLatitude(d);
            address.setLongitude(d1);
            addressDAO.create(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Gravar o Address");
        }
    }
    
    
   /**
    * @param technicianId
    * @param string
    * @param description
    * @param d
    * @param d1 
    */
    public void addAddress(long technicianId, String string, String description, double d, double d1) {

        try {
           
            
            Technitian technician= technicianDAO.find(technicianId);
            
            Address address = new Address();
            address.setSupplier(technician.getSupplier());
            address.setName(string);
            address.setDescription(description);
            address.setLatitude(d);
            address.setLongitude(d1);
            addressDAO.create(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Gravar o Address");
        }
    }

    @Override
    public void removeAddress(long l, long addressId) {
        try {
            Address address = new Address();
            address.setId(addressId);

            addressDAO.delete(address.getId());
        } catch (Exception ex) {
            Address address = null;
            try {
                address = addressDAO.find(addressId);
                address.setDisabled(true);
                addressDAO.update(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void editAddress(long l, long l1, String string, double d, double d1) {
        try {
            Supplier supplier = new Supplier();
            supplier.setId(l);

            Address address = new Address();
            address.setId(l1);

            address.setSupplier(supplier);
            address.setName(string);
            address.setLatitude(d);
            address.setLongitude(d1);

            addressDAO.update(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Editar o Address");
        }

    }
    
    /**
     * 
     * @param clientId
     * @param addressId
     * @param addressName
     * @param addressDescription
     * @param lat
     * @param lng 
     */
    
      public void editAddress(long supplierId, long addressId, String addressName, String addressDescription, double lat, double lng) {
        try {
            Supplier supplier = new Supplier();
            supplier.setId(supplierId);
            
            Address address = new Address();
            address.setId(addressId);
            
            address.setSupplier(supplier);
            address.setName(addressName);
            address.setDescription(addressDescription);
            address.setLatitude(lat);
            address.setLongitude(lng);
            
            addressDAO.update(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Editar o Address");
        }
        
        
    }
    

    @Override
    public TechnicianAccountInfo addAffiliate(long technicianId, String email, String name) throws Exception{

        TechnicianAccountInfo technicianAccountInfo = null;

        Technitian technitian = technicianDAO.find(technicianId);

        if(!technitian.isIsAdmin()){
            throw new Exception("this user is not the admin of this account");
        }

        User user = userDAO.findByKey("email",email);
        if(user==null){

            String username = email.replace("@","");
            username = username.replace(".","_");

            user = new User();
            user.setStatus(1);
            user.setUsername(username);
            user.setEmail(email);
            user.setName(name);
            user.setPassword("123456");
            userDAO.create(user);
        }


        Technitian newTechnician = technicianDAO.getAffiliateByUser(technitian.getSupplier().getId(), user.getId());
        if(newTechnician==null){
            newTechnician = new Technitian();
            newTechnician.setUser(user);
            newTechnician.setSupplier(technitian.getSupplier());
            newTechnician.setIsAdmin(false);
            newTechnician.setCreated_at(new Date());
            newTechnician.setCurrentAddress(technitian.getCurrentAddress());

            technicianDAO.create(newTechnician);

            technicianAccountInfo = Middleware.technicianAccountInfoMethod(newTechnician, addressDAO, technicianDAO);

        } else {
            if(newTechnician.isDisabled()){
                newTechnician.setDisabled(false);
                technicianDAO.update(newTechnician);
                technicianAccountInfo = Middleware.technicianAccountInfoMethod(newTechnician, addressDAO, technicianDAO);
            } else{
                throw new Exception("This user is already an afiliate of this customer");
            }
        }


        return technicianAccountInfo;
    }
    
    
    /**
     * @author Edgêncio da Calista
     * @param technicianId
     * @param email
     * @param name
     * @param phone
     * @return
     * @throws Exception 
     */
    
    
        public TechnicianAccountInfo addAffiliate(long technicianId, String email, String name,String phone) throws Exception{

        TechnicianAccountInfo technicianAccountInfo = null;

        Technitian technitian = technicianDAO.find(technicianId);

        if(!technitian.isIsAdmin()){
            throw new Exception("this user is not the admin of this account");
        }

        User user = userDAO.findByKey("email",email);
        if(user==null){

//            String username = email.replace("@","");
//            username = username.replace(".","_");
            String username=email;
            user = new User();
            user.setStatus(1);
            user.setUsername(username);
            user.setEmail(email);
            user.setName(name);
            user.setMobile(phone);
            user.setPassword(getMD5("support123"));
            
            userDAO.create(user);
        }


        Technitian newTechnician = technicianDAO.getAffiliateByUser(technitian.getSupplier().getId(), user.getId());
        if(newTechnician==null){
            newTechnician = new Technitian();
            newTechnician.setUser(user);
            newTechnician.setSupplier(technitian.getSupplier());
            newTechnician.setIsAdmin(false);
            newTechnician.setStatus(1);
            newTechnician.setCreated_at(new Date());
            newTechnician.setCurrentAddress(technitian.getCurrentAddress());
            newTechnician.setLatitude(technitian.getCurrentAddress().getLatitude());
            newTechnician.setlongitude(technitian.getCurrentAddress().getLongitude());

            technicianDAO.create(newTechnician);

            technicianAccountInfo = Middleware.technicianAccountInfoMethod(newTechnician, addressDAO, technicianDAO);

        } else {
            if(newTechnician.isDisabled()){
                newTechnician.setDisabled(false);
                technicianDAO.update(newTechnician);
                technicianAccountInfo = Middleware.technicianAccountInfoMethod(newTechnician, addressDAO, technicianDAO);
            } else{
                throw new Exception("This user is already an afiliate of this customer");
            }
        }


        return technicianAccountInfo;
    }
        
        
        
    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            System.out.println("passe encriptada: "+hashtext);
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
 
    
    

    @Override
    public void removeAffiliate(long technicianId, String email) {

        TechnicianAccountInfo technicianAccountInfo = getById(technicianId);

        for(AffiliateInfo affiliate : technicianAccountInfo.getAffiliates()){

            if(affiliate.getEmail().equalsIgnoreCase(email.trim())){

                try {
                    Technitian technitian = technicianDAO.find(affiliate.getId());
                    if(technitian!=null){
                        technitian.setDisabled(true);
                        technicianDAO.update(technitian);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public void setStatus(long l, boolean bln) {
        try {
            Technitian technitian;
            try {
                technitian = technicianDAO.find(l);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Falha a tentar Encontrar o Tecnico");
            }
            
            if (bln) {
                technitian.setStatus(1);
            } else {
                technitian.setStatus(0);
            }
            technicianDAO.update(technitian);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar actualizar");
        }
    }

    /*
    * This method alters the rating of the supplier witch the Technician represents(Not the Technician rating)
    **/
    @Override
    public void addNewRating(long l, double d) {
        Technitian technician;
        try {
            technician = technicianDAO.find(l);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar Technician");
        }
        Supplier supplier;
        try {
            supplier = supplierDAO.find(technician.getSupplier().getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar o Supplier do Technician");
        }
        double old_rating = supplier.getSupplierInfo().getRating();
        old_rating += d;
        supplier.getSupplierInfo().setRating(old_rating);

        try {
            supplierDAO.update(supplier);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar actualizar o novo rating Technician");
        }
    }

/*
    public Collection<TechnicianAccountInfo> ownViewNearest(double d, double d1) {

        List<Technitian> technitianList = technicianDAO.getNearestTechnicians(d, d1);
        Collection<TechnicianAccountInfo> technicianAccountInfos = new ArrayList<>();

        for(Technitian technitian : technitianList){
            technicianAccountInfos.add(Middleware.technicianAccountInfoMethod(technitian,
                    addressDAO, technicianDAO));
        }

        return technicianAccountInfos;

    }
    */
    
    @Override
    public Collection<TechnicianAccountInfo> viewNearest(double d, double d1) {

        long last_supplier_id=0;
        List<Technitian> technitianList = technicianDAO.getNearestTechnicians(d, d1);
        Collection<TechnicianAccountInfo> technicianAccountInfos = new ArrayList<>();

        for(Technitian technitian : technitianList){ 
           // if(technitian.getSupplier().getId() != last_supplier_id && technitian.isIsAdmin())
             if(technitian.isIsAdmin()){
            technicianAccountInfos.add(Middleware.technicianAccountInfoMethod(technitian,
                    addressDAO, technicianDAO));}
            
             last_supplier_id=technitian.getSupplier().getId();
        }
        
       

        return technicianAccountInfos;

    }
    
    
    
    
    
    

    @Override
    public TechnicianAccountInfo viewProfile(long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
