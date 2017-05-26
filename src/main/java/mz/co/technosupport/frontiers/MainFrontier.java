/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.services.AccountClientServiceImpl;
import mz.co.technosupport.data.services.AccountTechnicianImpl;
import mz.co.technosupport.data.services.UserService;

/**
 *
 * @author Anselmo
 */
@Frontier
@ApplicationScoped
public class MainFrontier {
 
    @Inject AccountClientServiceImpl accountClientServiceImpl;
    @Inject AccountTechnicianImpl accountTechnicianImpl;
    @Inject UserService userService;
    
    
    public String testService(Map params,String target) {
        
            if(target.equalsIgnoreCase("AccountClientServiceImpl.removeAddress")){
                System.out.print("Executing : "+target);
                accountClientServiceImpl.removeAddress(Long.valueOf(params.get("clientId").toString()), Long.valueOf(params.get("addressId").toString()));
            }
            
            if(target.equalsIgnoreCase("AccountClientServiceImpl.addAddress")){
                System.out.print("Executing : "+target);
                accountClientServiceImpl.addAddress(Long.valueOf(params.get("clientId").toString()), params.get("addressName").toString(), Double.valueOf(params.get("lat").toString()), Double.valueOf(params.get("lng").toString()));
            }
		
            if(target.equalsIgnoreCase("AccountClientServiceImpl.editAddress")){
                System.out.print("Executing : "+target);
                System.err.println(params);
                accountClientServiceImpl.editAddress(Long.valueOf(params.get("clientId").toString()), Long.valueOf(params.get("addressId").toString()) , params.get("addressName").toString(), Double.valueOf(params.get("lat").toString()), Double.valueOf(params.get("lng").toString()));
                
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.addAddress")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.addAddress(Long.valueOf(params.get("technicianId").toString()), params.get("addressName").toString(), Double.valueOf(params.get("lat").toString()), Double.valueOf(params.get("lng").toString()));
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.editAddress")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.editAddress(Long.valueOf(params.get("technicianId").toString()), Long.valueOf(params.get("addressId").toString()), params.get("addressName").toString(), Double.valueOf(params.get("lat").toString()), Double.valueOf(params.get("lng").toString()));
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.removeAddress")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.removeAddress(Long.valueOf(params.get("technicianId").toString()), Long.valueOf(params.get("addressId").toString()));
            }
 
            if(target.equalsIgnoreCase("AccountTechnicianImpl.addAffiliate")){
                System.out.print("Executing : "+target);
                try {
                    accountTechnicianImpl.addAffiliate(Long.valueOf(params.get("technicianId").toString()), params.get("email").toString(), params.get("name").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.removeAffiliate")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.removeAffiliate(Long.valueOf(params.get("technicianId").toString()), params.get("email").toString());
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.setStatus")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.setStatus(Long.valueOf(params.get("technicianId").toString()), Boolean.valueOf(params.get("isAvailable").toString()));
            }
            
            if(target.equalsIgnoreCase("AccountTechnicianImpl.addNewRating")){
                System.out.print("Executing : "+target);
                accountTechnicianImpl.addNewRating(Long.valueOf(params.get("technicianId").toString()), Double.valueOf(params.get("rating").toString()));
            }
            
            if(target.equalsIgnoreCase("UserService.areValidCredentials")){
                System.out.print("Executing : "+target);
                userService.areValidCredentials(params.get("username").toString(), params.get("password").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.issueToken")){
                System.out.print("Executing : "+target);
                userService.issueToken(params.get("username").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.validateToken")){
                System.out.print("Executing : "+target);
                userService.validateToken(params.get("token").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.getUserByUsername")){
                System.out.print("Executing : "+target);
                userService.getUserByUsername(params.get("username").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.getUserByToken")){
                System.out.print("Executing : "+target);
                userService.getUserByToken(params.get("token").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.getUserById")){
                System.out.print("Executing : "+target);
                userService.getUserById(Long.valueOf(params.get("userId").toString()));
            }
            
            if(target.equalsIgnoreCase("UserService.isValidUserResetCode")){
                System.out.print("Executing : "+target);
                userService.isValidUserResetCode(params.get("username").toString(), params.get("code").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.changePassword")){
                System.out.print("Executing : "+target);
                userService.changePassword(params.get("code").toString(), params.get("username").toString(), params.get("newPassword").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.changePassword_two")){
                System.out.print("Executing : "+target);
                userService.changePassword(Long.valueOf(params.get("userId").toString()), params.get("newPassword").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.updateProfilePicture")){
                System.out.print("Executing : "+target);
                userService.updateProfilePicture(Long.valueOf(params.get("userId").toString()), params.get("picturePath").toString());
            }
            
            if(target.equalsIgnoreCase("UserService.updateName")){
                System.out.print("Executing : "+target);
                userService.updateName(Long.valueOf(params.get("userId").toString()), params.get("name").toString());
            }
            if(target.equalsIgnoreCase("UserService.updatePhone")){
                System.out.print("Executing : "+target);
                userService.updatePhone(Long.valueOf(params.get("userId").toString()), params.get("phone").toString());
            }
            
         return target;
    }
    
    
}
    
