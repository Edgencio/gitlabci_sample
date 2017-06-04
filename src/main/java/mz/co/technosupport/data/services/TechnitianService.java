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
import mz.co.technosupport.data.daos.SupplierDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.daos.UserDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.data.model.User;

/**
 * Created by Edgêncio da Calista.
 */
@ApplicationScoped
public class TechnitianService {

    @Inject
    TechnicianDAO technitianDAO;

    @Inject
    CustomerDAO customerDAO;

    @Inject
    SupplierDAO supplierDAO;

    @Inject
    UserDAO userDAO;

    public char saveTechnician(long supplierId, String affiliateEmail) {
        User user = null;
        char errorController = 'C';
        Supplier supplier = null;
        Technitian verifytechnitian = null;

        try {
            user = userDAO.getUserByEmail(affiliateEmail);

            verifytechnitian = technitianDAO.getByEmail(affiliateEmail);
            if (verifytechnitian == null) {

                supplier = supplierDAO.find(supplierId);

                Technitian tech = new Technitian();
                tech.setUser(user);
                tech.setIsAdmin(false);
                tech.setDisabled(false);
                tech.setSupplier(supplier);
                tech.setLatitude(-25.998345);
                tech.setlongitude(32.445455);
                tech.setAvailable(false);
                tech.setCreated_at(new Date());
                technitianDAO.create(tech);
                errorController = 'B'; // Means affilate successfully linked;

            } else {
                if (verifytechnitian.isDisabled()) {
                    supplier = supplierDAO.find(supplierId);

                    verifytechnitian.setAvailable(false);
                    technitianDAO.update(verifytechnitian);
                    errorController = 'B';
                } else if (!verifytechnitian.isDisabled()) {
                    errorController = 'A';
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 'C'; // Means There was an error during the operation;
        }

        return errorController;

    }

}
