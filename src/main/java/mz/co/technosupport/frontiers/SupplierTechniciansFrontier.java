/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.component.HiList;
import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.data.services.AccountTechnicianImpl;
import mz.co.technosupport.data.services.CustomerService;
import mz.co.technosupport.data.services.SupplierService;
import mz.co.technosupport.data.services.TechnitianService;
import mz.co.technosupport.data.services.UserService;
import mz.co.technosupport.dto.UserDTO;
import mz.co.technosupport.info.UserInfo;
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
    private CustomerService customerService;

    @Inject
    private UserDAO userDAO;
    
    @Inject
    private UserService userService;

    @Inject
    private TechnitianService technitianService;

    @Inject
    private AccountTechnicianImpl accountTechnicianServiceImpl;

    public List<Technitian> getTechnicians() {
        List<Technitian> tecnhnicians = null;

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            tecnhnicians = supplierService.getSupplierAffiliates(supplierId);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return tecnhnicians;
    }

    public boolean createTechnician(String fullName, String phone, String email) {
        if (email == null || email == " " || email == "") {
            return false;
        }
        UserDTO user = (UserDTO) activeUser.getProperty("user");
        try {
            TechnicianAccountInfo accountInfo = accountTechnicianServiceImpl.addAffiliate(user.getSupplier().getSupplierID(), email, fullName, phone);
            if (accountInfo != null) {
                EmailSender.sendActivationMail(email, user.getUserName(),
                        user.getUserName());

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

        if (email == null || email == " " || email == "") {
            return false;
        }

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

            userDAO.update(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void refreshPage() {
        frontEnd.ajaxRedirect("supplier/technicians");
    }

    public Map fetchTechniciansPerPage(int pageNumber, int itemsPerPage, Map filter, Map ordering) {

        Map customerAffiliates = new HashMap(0);
        int totalMatchedPages = 0;
        int totalMatchedRows = 0;
        List<Consumer> affiliates = new ArrayList();

        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            customerAffiliates = supplierService.getSupplierAffiliatesPerPage(supplierId, pageNumber, itemsPerPage, filter, ordering);
            affiliates = (List<Consumer>) customerAffiliates.get("list");
            totalMatchedRows = (int) customerAffiliates.get("total");
            totalMatchedPages = (int) customerAffiliates.get("pages");
            return HiList.listEncode(affiliates, totalMatchedRows, pageNumber, totalMatchedPages);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public Collection<String> fetchEmails() {
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            return customerService.fetchUsersEmails(supplierId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public char linkAffiliate(String affiliateEmail) {
        char response = ' ';
        try {
            UserDTO user = (UserDTO) activeUser.getProperty("user");
            long supplierId = user.getSupplier().getSupplierID();
            response = technitianService.saveTechnician(supplierId, affiliateEmail);
//            EmailSender.sendActivationMail(affiliateEmail, user.getUserName(),
//                        user.getUserName());
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 'C';
        }

    }
    
    
       public boolean checkIfUserExists(String affiliateEmail) {
        boolean exists = false;
        UserInfo usr = new UserInfo();
        try {

            usr = userService.getUserByUsername(affiliateEmail);
            if (usr.getUsername() == null) {
                exists = false;
            } else {
                exists = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return exists;

    }

}
