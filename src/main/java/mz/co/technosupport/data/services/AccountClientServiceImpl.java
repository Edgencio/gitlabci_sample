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
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.*;
import mz.co.technosupport.info.AddressInfo;
import mz.co.technosupport.info.AffiliateInfo;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.service.AccountClientService;
import mz.co.technosupport.util.Middleware;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Anselmo
 */
@SuppressWarnings("ALL")
@ApplicationScoped
public class AccountClientServiceImpl implements AccountClientService {

    @Inject
    AddressDAO addressDAO;
    
    @Inject
    UserDAO userDAO;
    
    @Inject
    ConsumerDAO consumerDAO;
    
    

    @Override
    public ClientAccountInfo getById(long l) {

        ClientAccountInfo clientAccountInfo = new ClientAccountInfo();

        try {
            Consumer consumer = consumerDAO.find(l);
            if(consumer==null) throw new NoResultException("No valid user with id " +l+" found");

            clientAccountInfo = Middleware.clientAccountInfoMethod(consumer, addressDAO, consumerDAO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientAccountInfo;
    }

    private AddressInfo addressInfoMethod(Address address){
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(address.getId());
        addressInfo.setName(address.getName());
        addressInfo.setLatitude(address.getLatitude());
        addressInfo.setLongitude(address.getLongitude());

        return addressInfo;
    }

    private Collection<AffiliateInfo> affiliateInfoList(Collection<Consumer> consumers, Collection<Technitian> technitians){
        List<AffiliateInfo> affiliateInfoList = new ArrayList<>();
        if(consumers == null){
            for (Technitian technitian : technitians) {
                affiliateInfoList.add(affiliateInfoMethod(null, technitian));
            }
        }else if(technitians == null){
            for (Consumer consumer : consumers) {
                affiliateInfoList.add(affiliateInfoMethod(consumer, null));
            }
        }
        return affiliateInfoList;
    }

    private AffiliateInfo affiliateInfoMethod(Consumer consumer, Technitian technician){
        AffiliateInfo affiliateInfo = new AffiliateInfo();
        if(technician == null){
            affiliateInfo.setId(consumer.getId());
            affiliateInfo.setEmail(consumer.getCustomer().getEmail());
            affiliateInfo.setName(consumer.getCustomer().getName());
        }else if(consumer == null){
            affiliateInfo.setId(technician.getId());
            affiliateInfo.setEmail(technician.getSupplier().getEmail());
            affiliateInfo.setName(technician.getSupplier().getName());
        }

        return affiliateInfo;
    }

    private Collection<AddressInfo> addressInfoList(Collection<Address> addresses){
        List<AddressInfo> addressInfoList = new ArrayList<>();
        for (Address address : addresses) {
            addressInfoList.add(addressInfoMethod(address));
        }
        return addressInfoList;
    }

    @Override
    public void addAddress(long clientId, String addressName, double lat, double lng) {
        
        try {

            Consumer consumer = consumerDAO.find(clientId);

            Address address = new Address();
            address.setCustomer(consumer.getCustomer());
            address.setName(addressName);
            address.setLatitude(lat);
            address.setLongitude(lng);
            
            addressDAO.create(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Gravar o Address");
        }
    }
    
    
    /**
     * 
     * @param clientId
     * @param addressName
     * @param lat
     * @param lng 
     */
   
    public void addAddress(long clientId, String addressName, String description, double lat, double lng) {
        
        try {

            Consumer consumer = consumerDAO.find(clientId);

            Address address = new Address();
            address.setCustomer(consumer.getCustomer());
            address.setName(addressName);
            address.setDescription(description);
            address.setLatitude(lat);
            address.setLongitude(lng);
            
            addressDAO.create(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Gravar o Address");
        }
    }
    

    @Override
    public void removeAddress(long clientId, long addressId) {

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
    public void editAddress(long clientId, long addressId, String addressName, double lat, double lng) {
        try {
            Customer customer = new Customer();
            customer.setId(clientId);
            
            Address address = new Address();
            address.setId(addressId);
            
            address.setCustomer(customer);
            address.setName(addressName);
            address.setLatitude(lat);
            address.setLongitude(lng);
            
            addressDAO.update(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Impossivel Editar o Address");
        }
        
        }
    
    
      
    public void editAddress(long clientId, long addressId, String addressName, String addressDescription, double lat, double lng) {
        try {
            Customer customer = new Customer();
            customer.setId(clientId);
            
            Address address = new Address();
            address.setId(addressId);
            
            address.setCustomer(customer);
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
    public ClientAccountInfo addAffiliate(long clientId, String email, String name) throws Exception{

        ClientAccountInfo clientAccountInfo = null;

        Consumer consumer = consumerDAO.find(clientId);

        if(!consumer.isIsAdmin()){
            throw new Exception("this user is not the admin of this account");
        }

        User user = userDAO.findByKey("email",email);
        if(user==null){

            String username = email.replace("@","");
            username = username.replace(".","_");

            user = new User();
            user.setStatus(1);
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPassword("12345");
            userDAO.create(user);
        }
        

        Consumer newConsumer = consumerDAO.getAffiliateByUser(consumer.getCustomer().getId(), user.getId());
        if(newConsumer==null){
            newConsumer = new Consumer();
            newConsumer.setUser(user);
            newConsumer.setCustomer(consumer.getCustomer());
            newConsumer.setIsAdmin(false);
            newConsumer.setCreated_at(new Date());
            newConsumer.setCurrentAddress(consumer.getCurrentAddress());

            consumerDAO.create(newConsumer);

            clientAccountInfo = Middleware.clientAccountInfoMethod(newConsumer, addressDAO, consumerDAO);

        } else {
            if(newConsumer.isDisabled()){
                newConsumer.setDisabled(false);
                consumerDAO.update(newConsumer);
                clientAccountInfo = Middleware.clientAccountInfoMethod(newConsumer, addressDAO, consumerDAO);
            } else{
                throw new Exception("This user is already an afiliate of this customer");
            }

        }

        return clientAccountInfo;
    }
    
    
    /**
     * @author Edgêncio da Calista
     * @param clientId Customer ID
     * @param email    Affiliate email to be saved
     * @param name     Affiliate name to be saved
     * @param phone    Added new param (affiliate phone) to be saved)
     * @return
     * @throws Exception 
     */
    
      public ClientAccountInfo addAffiliate(long clientId, String email, String name, String phone) throws Exception{

        ClientAccountInfo clientAccountInfo = null;

        Consumer consumer = consumerDAO.find(clientId);

        if(!consumer.isIsAdmin()){
            throw new Exception("this user is not the admin of this account");
        }

        User user = userDAO.findByKey("email",email);
        if(user==null){
//
//            String username = email.replace("@","");
//            username = username.replace(".","_");
            String username=email;
            user = new User();
            user.setStatus(1);
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setMobile(phone);
            user.setPassword(getMD5("support123"));
            userDAO.create(user);
        }
        

        Consumer newConsumer = consumerDAO.getAffiliateByUser(consumer.getCustomer().getId(), user.getId());
        if(newConsumer==null){
            newConsumer = new Consumer();
            newConsumer.setUser(user);
            newConsumer.setCustomer(consumer.getCustomer());
            newConsumer.setIsAdmin(false);
            newConsumer.setCreated_at(new Date());
            newConsumer.setCurrentAddress(consumer.getCurrentAddress());

            consumerDAO.create(newConsumer);

            clientAccountInfo = Middleware.clientAccountInfoMethod(newConsumer, addressDAO, consumerDAO);

        } else {
            if(newConsumer.isDisabled()){
                newConsumer.setDisabled(false);
                consumerDAO.update(newConsumer);
                clientAccountInfo = Middleware.clientAccountInfoMethod(newConsumer, addressDAO, consumerDAO);
            } else{
                throw new Exception("This user is already an afiliate of this customer");
            }

        }

        return clientAccountInfo;
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
    public void removeAffiliate(long clientId, String email) {

        ClientAccountInfo clientAccountInfo = getById(clientId);

        for(AffiliateInfo affiliate : clientAccountInfo.getAffiliates()){

            if(affiliate.getEmail().equalsIgnoreCase(email.trim())){

                try {
                    Consumer consumer = consumerDAO.find(affiliate.getId());
                    if(consumer!=null){
                        consumer.setDisabled(true);
                        consumerDAO.update(consumer);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
                        
    }

    
}
