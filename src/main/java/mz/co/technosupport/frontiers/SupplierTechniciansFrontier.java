/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.data.services.AccountTechnicianImpl;
import mz.co.technosupport.data.services.SupplierService;
import mz.co.technosupport.dto.UserDTO;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.util.EmailSender;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class SupplierTechniciansFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private SupplierService supplierService;
    
    @Inject
    private UserDAO userDAO;
    
    @Inject
    private AccountTechnicianImpl accountTechnicianServiceImpl;

    public List<Technitian> getTechnicians() {
        List<Technitian> tecnhnicians = null;

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            tecnhnicians= supplierService.getSupplierAffiliates(supplierId);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return tecnhnicians;
    }

    public boolean createTechnician(String fullName, String phone, String email) {
        UserDTO user = (UserDTO) activeUser.getProperty("user");
        try {
            TechnicianAccountInfo accountInfo = accountTechnicianServiceImpl.addAffiliate(user.getSupplier().getSupplierID(), email, fullName, phone);
            if (accountInfo != null) {
                EmailSender.sendActivationMail(email, user.getUserName(),
                        user.getUserName());
                frontEnd.ajaxRedirect("supplier/technicians");
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean removeTechnician(String email) {

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            accountTechnicianServiceImpl.removeAffiliate(supplierId, email);
            frontEnd.ajaxRedirect("supplier/technicians");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
    
    
    public boolean editTechnician(String name, String phone, String email, long user_id) {
        User user = null;
        try {
            user = userDAO.find(user_id);
            user.setEmail(email);
            user.setMobile(phone);
            user.setName(name);
            System.out.println(name);
            System.out.println(phone);
            System.out.println(email);
            
      
            userDAO.update(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void refreshPage() {
        frontEnd.ajaxRedirect("supplier/affiliates");
    }

    

}
