package mz.co.technosupport.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Technitian;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.security.Secured;
import mz.co.technosupport.service.AccountTechnicianService;
import mz.co.technosupport.service.TicketService;
import mz.co.technosupport.util.EmailSender;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Objects;
import mz.co.technosupport.data.model.Supplier;
import mz.co.technosupport.data.services.AccountTechnicianImpl;
import mz.co.technosupport.info.UserInfo;

/**
 * @author Americo Chaquisse
 */
@Path("user/technician")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class UserTechnicianResource {

    @Inject
    private AccountTechnicianImpl technicianService;

    @Inject
    private TicketService ticketService;

    @Inject
    private AddressDAO addressDAO;

    @Inject
    private TechnicianDAO technicianDAO;

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    @GET
    @Path("{id}")
    @Secured
    public Response view(@PathParam("id") long id) {
        TechnicianAccountInfo technicianAccountInfo = technicianService.getById(id);
        return Response.ok(gson.toJson(technicianAccountInfo)).build();
    }

    @GET
    @Secured
    @Path("{id}/history")
    public Response history(@PathParam("id") long id) {

        Collection<TicketInfo> tickets = ticketService.geTechniciantHistory(id, 0, 500);

        return Response.ok(gson.toJson(tickets)).build();

    }

    @GET
    @Secured
    @Path("nearest")
    public Response nearest(@QueryParam("latitude") double lat, @QueryParam("longitude") double lng) {
        return Response.ok(gson.toJson(technicianService.viewNearest(lat, lng))).build();
    }

    @POST
    @Secured
    @Path("{id}/address/add")
    public Response addAddress(@PathParam("id") long technicianId, @FormParam("name") String addressName,@FormParam("desc") String description,
            @FormParam("lat") double lat, @FormParam("lng") double lng) {
        technicianService.addAddress(technicianId, addressName, description, lat, lng);
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/address/current/update")
    public Response setCurrent(@PathParam("id") long technicianId, @FormParam("lat") double lat,
            @FormParam("lng") double lng) {
        try {
            //TechnicianAccountInfo tech =technicianService.getById(technicianId);
            Technitian technitian = technicianDAO.find(technicianId);
            Supplier sup = technitian.getSupplier();
            long AddressId = technitian.getCurrentAddress().getId();
            String address_name = technitian.getCurrentAddress().getName();

            Address address = new Address();
            address.setId(AddressId);
            address.setLatitude(lat);
            address.setLongitude(lng);
            address.setName(address_name);
            address.setSupplier(sup);

            System.out.println(sup.getName());
            System.out.println();

            addressDAO.update(address);
        } catch (Exception ex) {
            ex.printStackTrace();
            Response.status(0);
        }
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/address/{addressId}/remove")
    public Response removeAddress(@PathParam("id") long id, @PathParam("addressId") long addressId) {
        technicianService.removeAddress(id, addressId);
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/address/{addressId}/default")
    public Response defaultAddress(@PathParam("id") long id, @PathParam("addressId") long addressId) {

        try {
            Address address = addressDAO.find(addressId);
            Technitian technitian = technicianDAO.find(id);

            if (Objects.equals(address.getSupplier().getId(), technitian.getSupplier().getId()) && !address.isDisabled()) {
                technitian.setCurrentAddress(address);
                technicianDAO.update(technitian);
            } else {
                throw new RuntimeException("this address do not belong to this user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/available/{isAvailable}")
    public Response isAvailable(@PathParam("id") long id, @PathParam("isAvailable") int isAvailable) {

        try {
            Technitian technitian = technicianDAO.find(id);
            if (isAvailable == 1) {
                technitian.setAvailable(true);
            } else {
                technitian.setAvailable(false);
            }
            technicianDAO.update(technitian);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/affiliate/add")
    public Response addAffiliate(@PathParam("id") long id, @FormParam("email") String email, @FormParam("name") String name) {

        try {
            Technitian technitian = technicianDAO.find(id);
            TechnicianAccountInfo accountInfo = technicianService.addAffiliate(id, email, name);
            if (accountInfo != null) {
                EmailSender.sendActivationMail(email, technitian.getSupplier().getName(),
                        technitian.getUser().getName());
                return Response.ok().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("email with problems: " + email).build();
    }

    @POST
    @Secured
    @Path("{id}/affiliate/remove")
    public Response removeAffiliate(@PathParam("id") long id, @FormParam("email") String email) {
        technicianService.removeAffiliate(id, email);
        return Response.ok().build();
    }

}
