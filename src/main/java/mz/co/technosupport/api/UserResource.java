package mz.co.technosupport.api;

import com.google.gson.Gson;
import mz.co.technosupport.data.daos.*;
import mz.co.technosupport.data.model.*;
import mz.co.technosupport.info.UserInfo;
import mz.co.technosupport.info.account.GenericAccountInfo;
import mz.co.technosupport.security.Secured;
import mz.co.technosupport.service.UserService;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.util.Collection;

/**
 * @author Americo Chaquisse
 */
@SuppressWarnings("ALL")
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private TechnicianDAO technicianDAO;

    @Inject
    private SupplierDAO supplierDAO;

    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private ConsumerDAO consumerDAO;

    @Inject
    private UserDAO userDAO;

    @Context
    SecurityContext securityContext;

    @POST
    @Path("auth")

    public Response auth(@FormParam("username") String username, @FormParam("password") String password) {
        if (userService.areValidCredentials(username, password)) {
            return Response.ok(userService.issueToken(username)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Secured
    @Path("me/profile/password")
    public Response passwordReset(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) {

        if (userService.areValidCredentials(securityContext.getUserPrincipal().getName(), oldPassword)) {

            UserInfo userInfo = userService.getUserByUsername(securityContext.getUserPrincipal().getName());
            userService.changePassword(userInfo.getId(), newPassword);
            return Response.ok().build();

        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @GET
    @Secured
    @Path("me/profile")
    public Response profile() {
        String username = securityContext.getUserPrincipal().getName();
        UserInfo userInfo = userService.getUserByUsername(username);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(userInfo)).build();
    }

    @POST
    @Secured
    @Path("me/update")
    public Response profileUpdate(@FormParam("name") String name,
            @FormParam("mobile") String mobile,
            @FormParam("password") String password) {

        String username = securityContext.getUserPrincipal().getName();
        try {
            User user = userDAO.findByKey("username", username);
            if (user != null) {
                user.setName(name);
                user.setMobile(mobile);
                user.setPassword(password);
                userDAO.update(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("fcmtoken")
    public Response response(@FormParam("fcm") String fcmToken) {

        String username = securityContext.getUserPrincipal().getName();

        userService.issueFcmToken(username, fcmToken);

        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("me/profile/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProfilePicture(@FormDataParam("file") InputStream fileInputStream) {

        String username = securityContext.getUserPrincipal().getName();

        String uploadedFileLocation = "/var/www/technosupport-uploads/" + username + ".png";
        String uploadedFileURL = "http://support.technoplus.co.mz:70/technosupport-uploads/" + username + ".png";

        UserInfo userInfo = userService.getUserByUsername(username);

        System.out.println(uploadedFileLocation);
        // save it

        File objFile = new File(uploadedFileLocation);
        if (objFile.exists()) {
            objFile.delete();
        }

        saveToFile(fileInputStream, uploadedFileLocation);

        userService.updateProfilePicture(userInfo.getId(), uploadedFileURL);

        try {

            Collection<Technitian> technitianCollection = technicianDAO.getAllByUser(userInfo.getId());
            if (technitianCollection != null) {
                for (Technitian technitian : technitianCollection) {
                    if (technitian.isIsAdmin()) {
                        Supplier supplier = supplierDAO.find(technitian.getSupplier().getId());
                        supplier.setImageUrl(uploadedFileURL);
                        supplierDAO.update(supplier);
                    }
                }
            }

            Collection<Consumer> consumerCollection = consumerDAO.getAllByUserId(userInfo.getId());
            if (consumerCollection != null) {
                for (Consumer consumer : consumerCollection) {
                    if (consumer.isIsAdmin()) {
                        Customer customer = customerDAO.find(consumer.getCustomer().getId());
                        customer.setImageUrl(uploadedFileURL);
                        customerDAO.update(customer);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;

        return Response.status(200).entity(output).build();

    }

    private void saveToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {

        try {
            OutputStream out = null;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @POST
    @Path("auth/recovery")
    public Response recoveryPassword(@QueryParam("email") String email) {

        if (userService.getUserByUsername(email) != null) {
            userService.requestUserPasswordReset(email);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("auth/recovery/confirm")
    public Response confirmRecoveryPassword(@QueryParam("email") String email, @QueryParam("code") String recoveryCode) {

        if (userService.isValidUserResetCode(email, recoveryCode)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("auth/recovery/newPass")
    public Response passwordResetByCode(@FormParam("email") String email, @FormParam("code") String code,
            @FormParam("newPassword") String newPassword) {

        if (userService.isValidUserResetCode(email, code)) {
            userService.changePassword(code, email, newPassword);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

}
