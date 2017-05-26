package mz.co.technosupport.api;

import mz.co.technosupport.data.daos.AddressDAO;
import mz.co.technosupport.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author Americo Chaquisse
 */
@Path("util")
public class UtilResource {

    @Inject
    private AddressDAO addressDAO;

    @Path("online")
    @GET
    public Response isOnline() throws Exception {
        return Response.ok(addressDAO.count()).build();
    }
}
