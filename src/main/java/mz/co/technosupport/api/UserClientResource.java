package mz.co.technosupport.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.data.daos.ConsumerDAO;
import mz.co.technosupport.data.daos.CustomerDAO;
import mz.co.technosupport.data.model.Address;
import mz.co.technosupport.data.model.Consumer;
import mz.co.technosupport.data.model.Customer;
import mz.co.technosupport.info.AddressInfo;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.security.Secured;
import mz.co.technosupport.service.AccountClientService;
import mz.co.technosupport.service.TicketService;
import mz.co.technosupport.util.EmailSender;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Objects;
import mz.co.technosupport.data.services.AccountClientServiceImpl;

/**
 * @author Americo Chaquisse
 */
@SuppressWarnings("ALL")
@Path("user/client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class UserClientResource {

    @Inject
    private AccountClientServiceImpl accountClientService;

    @Inject
    private TicketService ticketService;

    @Inject
    private AddressDAO addressDAO;

    @Inject
    private ConsumerDAO consumerDAO;

    @GET
    @Path("{id}")
    @Secured
    public Response view(@PathParam("id") long id) {

        ClientAccountInfo clientAccountInfo = accountClientService.getById(id);

        Gson gson = new Gson();

        return Response.ok(gson.toJson(clientAccountInfo)).build();
    }

    @GET
    @Secured
    @Path("{id}/history")
    public Response history(@PathParam("id") long id) {

        Collection<TicketInfo> tickets = ticketService.getClientHistory(id, 0, 500);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

        return Response.ok(gson.toJson(tickets)).build();

    }

    @POST
    @Secured
    @Path("{id}/address/add")
    public Response addAddress(@PathParam("id") long clientId, @FormParam("name") String addressName,@FormParam("desc") String description,
            @FormParam("lat") double lat, @FormParam("lng") double lng) {
        accountClientService.addAddress(clientId, addressName,description, lat, lng);
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/address/{addressId}/remove")
    public Response removeAddress(@PathParam("id") long id, @PathParam("addressId") long addressId) {
        accountClientService.removeAddress(id, addressId);
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/address/{addressId}/default")
    public Response defaultAddress(@PathParam("id") long id, @PathParam("addressId") long addressId) {

        try {
            Address address = addressDAO.find(addressId);
            Consumer consumer = consumerDAO.find(id);

            if (Objects.equals(address.getCustomer().getId(), consumer.getCustomer().getId()) && !address.isDisabled()) {
                consumer.setCurrentAddress(address);
                consumerDAO.update(consumer);
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
    @Path("{id}/address/{addressId}/edit")
    public Response editAddress(@PathParam("id") long clientId, @PathParam("addressId") long addressId,
            @FormParam("name") String addressName,
            @FormParam("lat") double lat, @FormParam("lng") double lng) {
        accountClientService.editAddress(clientId, addressId, addressName, lat, lng);
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/affiliate/add")
    public Response addAffiliate(@PathParam("id") long id, @FormParam("email") String email, @FormParam("name") String name) {
        try {
            Consumer consumer = consumerDAO.find(id);
            ClientAccountInfo accountInfo = accountClientService.addAffiliate(id, email, name);
            if (accountInfo != null) {
                EmailSender.sendActivationMail(email, consumer.getCustomer().getName(),
                        consumer.getUser().getName());
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
        accountClientService.removeAffiliate(id, email);
        return Response.ok().build();
    }

}
