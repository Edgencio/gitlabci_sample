/* 
* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.frontiers;

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

    public boolean updatePassword(String new_pass) {
        User user;
        try {
            UserDTO usr = (UserDTO) activeUser.getProperty("user");
            long userId = usr.getUserId();
            user = userDAO.find(userId);
            user.setPassword(new_pass);
            userDAO.update(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
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
