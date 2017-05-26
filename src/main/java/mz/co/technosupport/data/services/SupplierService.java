package mz.co.technosupport.data.services;

import java.util.ArrayList;
import java.util.List;
import mz.co.technosupport.data.daos.CustomerDAO;
import mz.co.technosupport.data.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.SupplierDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.model.Technitian;

/**
 * 
 * @author Edgêncio da Calista
 */
@ApplicationScoped
public class SupplierService {

    @Inject
    SupplierDAO supplierDAO;

    @Inject
    TechnicianDAO technicianDAO;

    @Inject
    AddressDAO addressDAO;

    public List<Technitian> getSupplierAffiliates(long supplierId) {
        List<Technitian> technicians = new ArrayList();

        try {

            technicians = technicianDAO.getAffiliates(supplierId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao buscar técnicos");
        }
        return technicians;
    }

    public List<Address> getSupplierAddresses(long supplierId) {
        List<Address> addresses = new ArrayList();
        try {
            addresses = addressDAO.getAllBySupplierId(supplierId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao buscar endereços.");
        }
        return addresses;
    }

    public List<String> getSuppliers() {
        List<String> suppliers = new ArrayList();
        List<Supplier> sups = null;
        try {
            sups = supplierDAO.getAll();
            for (Supplier sup : sups) {
                suppliers.add(sup.getName());
            }
            return suppliers;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
