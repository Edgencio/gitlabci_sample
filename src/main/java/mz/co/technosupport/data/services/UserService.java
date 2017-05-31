package mz.co.technosupport.data.services;

import java.io.IOException;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.data.model.User;
import mz.co.technosupport.info.AddressInfo;
import mz.co.technosupport.info.AffiliateInfo;
import mz.co.technosupport.info.UserInfo;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.account.GenericAccountInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.RequestContext;
import mz.co.hi.web.mvc.exceptions.MvcException;
import mz.co.technosupport.dto.CustomerDTO;
import mz.co.technosupport.dto.SupplierDTO;
import mz.co.technosupport.dto.UserDTO;

/**
 * Created by Romildo Cumbe
 */
@SuppressWarnings("ALL")
@Default
@Dependent
public class UserService implements mz.co.technosupport.service.UserService {

    @Inject
    UserDAO userDAO;

    @Inject
    TechnicianDAO technicianDAO;

    @Inject
    ConsumerDAO consumerDAO;

    @Inject
    AddressDAO addressDAO;

    @Inject
    HttpSession session;

    @Inject
    RequestContext req;

    @Inject
    ActiveUser activeUser;

    @Inject
    FrontEnd frontEnd;

    public void logout() throws MvcException {
        activeUser.setProperty("authorized", false);
        session.invalidate();
        String path = req.getRequest().getContextPath();
        try {

            frontEnd.refresh(path + "/entrance/login");
        } catch (Exception ex) {

            throw new MvcException("Failed to redirect", ex);

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
    
    
   
    

    @Override
    public boolean areValidCredentials(String username, String password) {
        return userDAO.getUserByCredentials(username, getMD5(password)) == null ? false : true;
    }

    
    public User authenticateUser(String username, String password) {
        return userDAO.getUserByCredentials(username, getMD5(password));
    }

    public UserDTO getGenericUserInfo(User usr, boolean isCustomer) {
        UserDTO userInfo = null;

        try {
            userInfo = new UserDTO();
            userInfo.setUserName(usr.getName());
            userInfo.setUserMail(usr.getEmail());
            userInfo.setUserPhone(usr.getMobile());
            userInfo.setUserId(usr.getId());
            if (isCustomer) {
                Consumer cons = consumerDAO.findAdminConsumer(usr);
                userInfo.setUserType("customer");

                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerID(cons.getId());
                customer.setCustomerMail(cons.getCustomer().getEmail());
                customer.setCustomerName(cons.getCustomer().getName());
                customer.setCustomerAddress(cons.getCurrentAddress());
                customer.setCustomerPhone(cons.getCustomer().getMobile());
                customer.setCustomerType(cons.getCustomer().getType());
                userInfo.setCustomer(customer);
            } else if (!isCustomer) {
                Technitian tech = technicianDAO.findAdminTechnician(usr);
                userInfo.setUserType("supplier");

                SupplierDTO supplier = new SupplierDTO();
                supplier.setSupplierID(tech.getId());
                supplier.setSupplierMail(tech.getSupplier().getEmail());
                supplier.setSupplierName(tech.getSupplier().getName());
                supplier.setSupplierAddress(tech.getCurrentAddress());
                supplier.setSupplierPhone(tech.getSupplier().getPhone());
                supplier.setSupplierType(tech.getSupplier().getType());
                userInfo.setSupplier(supplier);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar buscar dados do usuário!");

        }
        return userInfo;
    }

    @Override
    public String issueToken(String username) {
        String token = "";
        User user = null;
        try {
            user = userDAO.findByKey("username", username);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Encontrar o User".toUpperCase());
        }

        if (user == null) {
            try {
                user = userDAO.findByKey("email", username);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Falha ao tentar Encontrar o User".toUpperCase());
            }
        }

        if (user == null) {
            throw new RuntimeException("Username inexistente".toUpperCase());
        }
        token = generateRandomString();
        user.setActiveToken(token);

        try {
            userDAO.update(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar atribuir token ao User".toUpperCase());
        }

        return token;
    }

    @Override
    public String issueFcmToken(String username, String s1) {
        try {
            User usr = userDAO.findByKey("username", username);
            usr.setFcmToken(s1);
            userDAO.update(usr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s1;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return userDAO.findByKey("activeToken", token) == null ? false : true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar Verificar se o token é Válido");
        }
    }

    @Override
    public UserInfo getUserByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        User usr = null;
        try {
            usr = userDAO.findByKey("username", username);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar User".toUpperCase());
        }
        if (usr == null) {
            throw new RuntimeException("User " + username + " não encontrado".toUpperCase());
        }
        userInfo = userInfoReturn(usr);

        return userInfo;
    }

    @Override
    public UserInfo getUserByToken(String token) {
        UserInfo userInfo = new UserInfo();
        User user = null;
        try {
            user = userDAO.findByKey("activeToken", token);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar User".toUpperCase());
        }
        if (user == null) {
            throw new RuntimeException("User não encontrado".toUpperCase());
        }
        userInfo = userInfoReturn(user);

        return userInfo;
    }

    @Override
    public UserInfo getUserById(long userId) {
        UserInfo userInfo = new UserInfo();
        User usr = null;
        try {
            usr = userDAO.find(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha ao tentar encontrar User".toUpperCase());
        }
        if (usr == null) {
            throw new RuntimeException("User não encontrado".toUpperCase());
        }
        userInfo = userInfoReturn(usr);

        return userInfo;
    }

    @Override
    public void requestUserPasswordReset(String username) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValidUserResetCode(String username, String code) {
        return false;//return userDAO.verifyValidResetCode(username, code) == null ? false : true;
    }

    @Override
    public void changePassword(String code, String username, String newPassword) {
        User usr = new User();
        try {
            usr = userDAO.verifyValidResetCode(username, code);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar localizar o User".toUpperCase());
        }

        if (usr != null) {

            usr.setPassword(newPassword);

            try {
                userDAO.update(usr);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Falha a tentar actulizar Password".toUpperCase());
            }
        } else {
            throw new RuntimeException("User nao encontrado".toUpperCase());
        }
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        User usr = new User();
        try {
            usr = userDAO.find(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar localizar o User".toUpperCase());
        }
        usr.setPassword(newPassword);

        try {
            userDAO.update(usr);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar actulizar Password".toUpperCase());
        }
    }

    @Override
    public void updateProfilePicture(long userId, String picturePath) {
        User usr = new User();
        try {
            usr = userDAO.find(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar localizar o User".toUpperCase());
        }
        usr.setImageUrl(picturePath);

        try {
            userDAO.update(usr);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar actulizar a Profile Image".toUpperCase());
        }

    }

    @Override
    public void updateName(long userId, String name) {
        User usr = new User();
        try {
            usr = userDAO.find(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar localizar o User".toUpperCase());
        }
        usr.setName(name);

        try {
            userDAO.update(usr);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar actulizar o Name".toUpperCase());
        }
    }

    @Override
    public void updatePhone(long userId, String phone) {
        User usr = new User();
        try {
            usr = userDAO.find(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar localizar o User".toUpperCase());
        }
        usr.setMobile(phone);

        try {
            userDAO.update(usr);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Falha a tentar actulizar o Phone".toUpperCase());
        }

    }

    private String generateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private UserInfo userInfoReturn(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setEmail(user.getEmail());
        userInfo.setUsername(user.getUsername());
        userInfo.setPhone(user.getMobile());
        userInfo.setProfilePicture(user.getImageUrl());
        userInfo.setId(user.getId());

        userInfo.setAccounts(new ArrayList<GenericAccountInfo>());

        Collection<Technitian> technitianCollection = technicianDAO.getAllByUser(user.getId());
        for (Technitian technitian : technitianCollection) {
            TechnicianAccountInfo technitianInfo = technicianAccountInfoMethod(technitian);
            userInfo.getAccounts().add(technitianInfo);

        }

        Collection<Consumer> consumerCollection = consumerDAO.getAllByUserId(user.getId());
        for (Consumer consumer : consumerCollection) {
            ClientAccountInfo consumerInfo = clientAccountInfoMethod(consumer);
            consumerInfo.setType(consumerInfo.getType());
            userInfo.getAccounts().add(consumerInfo);
        }

        return userInfo;
    }
    
    
    
    
    
    
    

    private TechnicianAccountInfo technicianAccountInfoMethod(Technitian technitian) {

        TechnicianAccountInfo technicianAccountInfo = new TechnicianAccountInfo();

        technicianAccountInfo.setId(technitian.getId());
        technicianAccountInfo.setAbout(technitian.getSupplier().getSupplierInfo().getAbout());
        technicianAccountInfo.setAccountType(technitian.getSupplier().getSupplierInfo().getAccountType());
        technicianAccountInfo.setAddresses(addressInfoList(addressDAO.getAllBySupplierId(technitian.getSupplier().getId())));

        technicianAccountInfo.setAffiliates(affiliateInfoList(null, technicianDAO.getAllBySuppleir(technitian.getSupplier().getId())));

        technicianAccountInfo.setAvailable(technitian.isAvailable());

        if (technitian.getCurrentAddress() != null) {
            technicianAccountInfo.setCurrentAddress(addressInfoMethod(technitian.getCurrentAddress()));
        } else {
            technicianAccountInfo.setCurrentAddress(new AddressInfo());
        }

        technicianAccountInfo.setEmail(technitian.getSupplier().getEmail());
        technicianAccountInfo.setName(technitian.getSupplier().getName());
        technicianAccountInfo.setAdmin(technitian.isIsAdmin());
        technicianAccountInfo.setType(technitian.getSupplier().getType());

        technicianAccountInfo.setAccountType("technician");
        technicianAccountInfo.setPhone(technitian.getSupplier().getMobile());
        technicianAccountInfo.setProfilePicture(technitian.getSupplier().getImageUrl());
        technicianAccountInfo.setRating(technitian.getSupplier().getSupplierInfo().getRating());
        technicianAccountInfo.setSkills(technitian.getSupplier().getSupplierInfo().getSkills());
        technicianAccountInfo.setTitle(technitian.getSupplier().getSupplierInfo().getTitle());

        return technicianAccountInfo;

    }

    private ClientAccountInfo clientAccountInfoMethod(Consumer consumer) {
        ClientAccountInfo clientAccountInfo = new ClientAccountInfo();

        clientAccountInfo.setId(consumer.getId());
        clientAccountInfo.setAccountType(consumer.getCustomer().getType());
        clientAccountInfo.setCurrentAddress(addressInfoMethod(consumer.getCurrentAddress()));
        clientAccountInfo.setAddresses(addressInfoList(addressDAO.getAllByCustomerId(consumer.getCustomer().getId())));

        clientAccountInfo.setAffiliates(affiliateInfoList(consumerDAO.getAffiliates(consumer.getCustomer().getId()), null));
        clientAccountInfo.setAccountType("client");

        clientAccountInfo.setEmail(consumer.getCustomer().getEmail());
        clientAccountInfo.setName(consumer.getCustomer().getName());
        clientAccountInfo.setPhone(consumer.getCustomer().getMobile());
        clientAccountInfo.setProfilePicture(consumer.getCustomer().getImageUrl());
        clientAccountInfo.setAdmin(consumer.isIsAdmin());
        clientAccountInfo.setType(consumer.getCustomer().getType());

        return clientAccountInfo;
    }

    private AddressInfo addressInfoMethod(Address address) {
        if (address != null) {
            AddressInfo addressInfo = new AddressInfo();
            addressInfo.setId(address.getId());
            addressInfo.setName(address.getName());
            addressInfo.setLatitude(address.getLatitude());
            addressInfo.setLongitude(address.getLongitude());

            return addressInfo;
        } else {
            return null;
        }
    }

    private AffiliateInfo affiliateInfoMethod(Consumer consumer, Technitian technician) {
        AffiliateInfo affiliateInfo = new AffiliateInfo();
        if (technician == null) {
            affiliateInfo.setId(consumer.getId());
            affiliateInfo.setEmail(consumer.getCustomer().getEmail());
            affiliateInfo.setName(consumer.getCustomer().getName());
        } else if (consumer == null) {
            affiliateInfo.setId(technician.getId());
            affiliateInfo.setEmail(technician.getSupplier().getEmail());
            affiliateInfo.setName(technician.getSupplier().getName());
        }

        return affiliateInfo;
    }

    private Collection<AddressInfo> addressInfoList(Collection<Address> addresses) {
        List<AddressInfo> addressInfoList = new ArrayList<>();
        for (Address address : addresses) {
            addressInfoList.add(addressInfoMethod(address));
        }
        return addressInfoList;
    }

    private Collection<AffiliateInfo> affiliateInfoList(Collection<Consumer> consumers, Collection<Technitian> technitians) {
        List<AffiliateInfo> affiliateInfoList = new ArrayList<>();
        if (consumers == null && technitians != null) {
            for (Technitian technitian : technitians) {
                affiliateInfoList.add(affiliateInfoMethod(null, technitian));
            }
        }
        if (technitians == null && consumers != null) {
            for (Consumer consumer : consumers) {
                affiliateInfoList.add(affiliateInfoMethod(consumer, null));
            }
        }

        return affiliateInfoList;
    }
}
