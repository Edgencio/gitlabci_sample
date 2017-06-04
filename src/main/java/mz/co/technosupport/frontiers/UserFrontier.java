/* 
* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.RequestContext;

import mz.co.hi.web.meta.Frontier;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.data.services.UserService;
import mz.co.technosupport.dto.UserDTO;

/**
 *
 * @author Edgêncio da Calista
 */
@Frontier
@ApplicationScoped
public class UserFrontier {

    @Inject
    FrontEnd frontEnd;

    @Inject
    private ActiveUser activeUser;

    @Inject
    private UserService userService;

    @Inject
    private UserDAO userDAO;

    @Inject
    private RequestContext context;

    @Inject
    HttpSession session;

    /**
     * Authenticates users
     *
     * @param username - User username (email)
     * @param pass - User password
     * @param isCustomer-just to know wich account to fetch
     * @return void
     */
    public UserDTO login(String user, String pass, boolean isCustomer) {

        User usr = userService.authenticateUser(user, pass);
      
        UserDTO userInfo = null;
        if (usr != null) {

            try {
                userInfo = userService.getGenericUserInfo(usr, isCustomer);
                activeUser.setProperty("user", userInfo);
                activeUser.setProperty("authorized", true);

                if (userInfo.getUserType().equalsIgnoreCase("customer")) {
                    frontEnd.ajaxRedirect("customer/tickets");
                } else {
                    frontEnd.ajaxRedirect("supplier/tickets");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                return null;
            }
            return userInfo;
        } else {
            System.out.println("Error");
            return null;
        }
    }

    public void logout() {
        try {
            userService.logout();
        } catch (Exception ex) {

        }
    }

    public boolean updatePassword(String old_pass, String new_pass) {
        User user;
        String old_pass_hash;
        String new_pass_hash;
        try {
            UserDTO usr = (UserDTO) activeUser.getProperty("user");
            long userId = usr.getUserId();
            old_pass_hash=getMD5(old_pass);
            new_pass_hash=getMD5((new_pass));
            user = userDAO.getUserByPassword(userId, old_pass_hash);
            user.setPassword(new_pass_hash);
            userDAO.update(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

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
    
    
    
    

    public void refreshPage() {
        UserDTO user = (UserDTO) activeUser.getProperty("user");

        if (user.getUserType().equalsIgnoreCase("customer")) {
            frontEnd.ajaxRedirect("customer/tickets");
        } else {
            frontEnd.ajaxRedirect("supplier/tickets");
        }
    }

}
