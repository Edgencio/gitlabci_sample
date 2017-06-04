package mz.co.technosupport.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mz.co.technosupport.data.daos.CustomerDAO;
import mz.co.technosupport.data.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.User;

/**
 * Created by Edgêncio da Calista.
 */
@ApplicationScoped
public class ConsumerService {

    @Inject
    ConsumerDAO consumerDAO;

    @Inject
    CustomerDAO customerDAO;

    @Inject
    UserDAO userDAO;

    public char saveConsumer(long customerId, String affiliateEmail) {
        User user = null;
        char errorController = 'C';
        Customer customer = null;
        Consumer verifyConsumer = null;

        try {
            user = userDAO.getUserByEmail(affiliateEmail);

            verifyConsumer = consumerDAO.getByEmail(affiliateEmail);
            if (verifyConsumer == null) {
                customer = customerDAO.find(customerId);

                Consumer consumer = new Consumer();
                consumer.setUser(user);
                consumer.setIsAdmin(false);
                consumer.setDisabled(false);
                consumer.setCustomer(customer);
                consumer.setCreated_at(new Date());
                consumerDAO.create(consumer);
                errorController = 'B'; // Means affilate successfully linked; 
            } else {
                if (verifyConsumer.isDisabled()) {

                    customer = customerDAO.find(customerId);

                    verifyConsumer.setDisabled(false);
                    consumerDAO.update(verifyConsumer);
                    errorController = 'B';
                } else if (!verifyConsumer.isDisabled()) {

                    errorController = 'A'; // Means user is already an affiliate; 
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return 'C'; // Means There was an error during the operation;
        }
        return errorController;

    }

}
