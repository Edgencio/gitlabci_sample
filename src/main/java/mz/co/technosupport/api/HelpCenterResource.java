package mz.co.technosupport.api;

import com.google.gson.Gson;
import mz.co.technosupport.info.help.ProblemItemInfo;
import mz.co.technosupport.security.Secured;
import mz.co.technosupport.service.HelpService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Americo Chaquisse
 */
@Path("help")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class HelpCenterResource {

    @Inject
    private HelpService helpService;

    Gson gson = new Gson();

    @GET
    @Secured
    @Path("categories")
    public Response categories() {

        return Response.ok(gson.toJson(helpService.getCategories())).build();

    }

    @GET
    @Secured
    @Path("category/{id}")
    public Response categoryItems(@PathParam("id") long id) {
        return Response.ok(gson.toJson(helpService.getProblemsOfCategory(id))).build();
    }

    @GET
    @Secured
    @Path("category/{categoryId}/item/{itemId}")
    public Response item(@PathParam("categoryId") String categoryId, @PathParam("itemId") long itemId) {
        ProblemItemInfo problemItemInfo = helpService.getProblemById(itemId);
        if (problemItemInfo != null && problemItemInfo.getId() > 0) {
            return Response.ok(gson.toJson(problemItemInfo)).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @POST
    @Secured
    @Path("category/{categoryId}/item/{itemId}/feedback")
    public Response problemFeedback(@PathParam("categoryId") long categoryId, @PathParam("itemId") long itemId,
            @FormParam("clientId") long clientId,
            @FormParam("rating") double rating, @FormParam("comment") String comment) {

        ProblemItemInfo problemItemInfo = helpService.getProblemById(itemId);
        if (problemItemInfo != null && problemItemInfo.getId() > 0) {
            helpService.saveClientFeedback(clientId, itemId, rating, comment);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

    }
}
