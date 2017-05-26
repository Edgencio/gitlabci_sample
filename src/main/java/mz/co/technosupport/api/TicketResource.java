package mz.co.technosupport.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mz.co.technosupport.data.daos.PictureDAO;
import mz.co.technosupport.data.daos.TechnicianDAO;
import mz.co.technosupport.data.daos.TicketDAO;
import mz.co.technosupport.data.model.*;
import mz.co.technosupport.fcm.Sender;
import mz.co.technosupport.fcm.TicketDownstream;
import mz.co.technosupport.info.AddressInfo;
import mz.co.technosupport.info.UserInfo;
import mz.co.technosupport.info.account.ClientAccountInfo;
import mz.co.technosupport.info.account.TechnicianAccountInfo;
import mz.co.technosupport.info.help.CategoryInfo;
import mz.co.technosupport.info.ticket.TicketInfo;
import mz.co.technosupport.info.ticket.TimelineItemInfo;
import mz.co.technosupport.info.util.TicketStatusEnum;
import mz.co.technosupport.info.util.TimelineItemTypeEnum;
import mz.co.technosupport.security.Secured;
import mz.co.technosupport.service.AccountClientService;
import mz.co.technosupport.service.AccountTechnicianService;
import mz.co.technosupport.service.TicketService;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.util.Collection;
import java.util.Date;

/**
 * @author Americo Chaquisse
 */
@SuppressWarnings("ALL")
@Path("ticket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class TicketResource {

    @Inject
    private TicketService ticketService;

    @Inject
    private TicketDAO ticketDAO;

    @Inject
    private TechnicianDAO technicianDAO;

    @Inject
    private AccountTechnicianService accountTechnicianService;

    @Inject
    private AccountClientService accountClientService;

    @Inject
    private PictureDAO pictureDAO;

    @Context
    SecurityContext securityContext;

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    @GET
    @Secured
    @Path("{id}")
    public Response view(@PathParam("id") long id) {
        return Response.ok(gson.toJson(ticketService.getById(id))).build();
    }

    @POST
    @Secured
    @Path("open")
    public Response openTicket(@FormParam("description") String description,
            @FormParam("categoryId") long categoryId,
            @FormParam("clientAddressId") long clientAddressId,
            @FormParam("technicianId") long technicianId,
            @FormParam("clientId") long clientId) {

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setName(description);
        ticketInfo.setStatus(TicketStatusEnum.PENDING);
        ticketInfo.setDate(new Date());

        CategoryInfo category = new CategoryInfo();
        category.setId(categoryId);
        ticketInfo.setCategory(category);

        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(clientAddressId);
        ticketInfo.setClientAddress(addressInfo);

        TechnicianAccountInfo technician = accountTechnicianService.getById(technicianId);
        ticketInfo.setTechnician(technician);

        ClientAccountInfo clientAccountInfo = accountClientService.getById(clientId);
        ticketInfo.setClient(clientAccountInfo);

        ticketService.openTicket(ticketInfo);

        TimelineItemInfo timelineItemInfo = new TimelineItemInfo();
        timelineItemInfo.setDate(new Date());
        timelineItemInfo.setItemType(TimelineItemTypeEnum.TICKET_OPENED);
        ticketService.addTimelineInfo(ticketInfo.getId(), timelineItemInfo);

        TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
        timelineItemInfo1.setDate(new Date());
        timelineItemInfo1.setItemType(TimelineItemTypeEnum.REQUEST_TECHNICIAN);
        ticketService.addTimelineInfo(ticketInfo.getId(), timelineItemInfo1);

        Sender.toSingle(ticketInfo.getTechnician().getFcmToken(),
                "Um cliente precisa de si",
                ticketInfo.getClient().getName() + " abriu um novo ticket.");

        return Response.ok(gson.toJson(ticketInfo)).build();

    }

    @GET
    @Secured
    @Path("{id}/technician/accept")
    public Response technicianAccept(@PathParam("id") long id) {

        try {

            Ticket ticket = ticketDAO.find(id);
            ticket.setStatus(TicketStatusEnum.OPENED);
            ticketDAO.update(ticket);

            TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
            timelineItemInfo1.setDate(new Date());
            timelineItemInfo1.setItemType(TimelineItemTypeEnum.TECHNICIAN_ACCEPT_JOB);
            ticketService.addTimelineInfo(id, timelineItemInfo1);

            Sender.toSingle(ticket.getConsumer().getUser().getFcmToken(),
                    ticket.getName(),
                    ticket.getTechnician().getSupplier().getName() + " aceitou vir ao seu encontro resolver este problema.");

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Secured
    @Path("{id}/technician/reject")
    public Response technicianReject(@PathParam("id") long id) {

        try {

            Ticket ticket = ticketDAO.find(id);

            TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
            timelineItemInfo1.setDate(new Date());
            timelineItemInfo1.setItemType(TimelineItemTypeEnum.TECHNICIAN_REJECT_JOB);
            ticketService.addTimelineInfo(id, timelineItemInfo1);

            Sender.toSingle(ticket.getConsumer().getUser().getFcmToken(),
                    ticket.getName(),
                    ticket.getTechnician().getSupplier().getName() + " recusou este pedido de assistência. Por favor requisite outro tecnico.");

            ticket.setTechnician(null);
            ticketDAO.update(ticket);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Secured
    @Path("{id}/technician/checkin")
    public Response technicianChecking(@PathParam("id") long id) {

        TicketInfo ticketInfo = ticketService.getById(id);

        try {
            Technitian technitian = technicianDAO.find(ticketInfo.getTechnician().getId());
            technitian.setAvailable(false);
            technicianDAO.update(technitian);

            TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
            timelineItemInfo1.setDate(new Date());
            timelineItemInfo1.setItemType(TimelineItemTypeEnum.TECHNICIAN_CHECKIN);
            ticketService.addTimelineInfo(ticketInfo.getId(), timelineItemInfo1);

            Sender.toSingle(ticketInfo.getClient().getFcmToken(),
                    ticketInfo.getName(),
                    ticketInfo.getTechnician().getName() + " deu entrada no seu endereço, para a resolução deste problema");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/technician/checkout")
    public Response technicianCheckout(@PathParam("id") long id,
            @FormParam("solved") boolean solved,
            @FormParam("rating") double rating,
            @FormParam("comment") String comment) {

        try {
            Ticket ticket = ticketDAO.find(id);
            ticket.setSolved(solved);
            ticket.setStatus(TicketStatusEnum.CLOSED);
            ticketDAO.update(ticket);

            ticketService.saveTechnicianFeedback(ticket.getTechnician().getId(),
                    id, rating, comment);

            TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
            timelineItemInfo1.setDate(new Date());
            timelineItemInfo1.setItemType(TimelineItemTypeEnum.TECHNICIAN_CHECKOUT);
            ticketService.addTimelineInfo(id, timelineItemInfo1);

            TimelineItemInfo timelineItemInfo2 = new TimelineItemInfo();
            timelineItemInfo2.setDate(new Date());
            timelineItemInfo2.setItemType(TimelineItemTypeEnum.TECNICIAN_JOB_FEEDBACK);
            ticketService.addTimelineInfo(id, timelineItemInfo2);

            Technitian technitian = technicianDAO.find(ticket.getTechnician().getId());
            technitian.setAvailable(true);
            technicianDAO.update(technitian);

            Sender.toSingle(ticket.getConsumer().getUser().getFcmToken(),
                    ticket.getName(),
                    "O técnico terminou as suas actividades. Por favor dê o feedback desta experiência");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @POST
    @Secured
    @Path("{id}/diagnostic")
    public Response technicianDiagnostic(@PathParam("id") long id,
            @FormParam("diagnostic") String diagnostic
    ) {

        Ticket ticket;
        try {
            ticket = ticketDAO.find(id);
            ticket.setDiagnostic(diagnostic);
            ticketDAO.update(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();

    }

    @POST
    @Secured
    @Path("{id}/solution")
    public Response technicianSolution(@PathParam("id") long id,
            @FormParam("solution") String solution
    ) {

        Ticket ticket;
        try {
            ticket = ticketDAO.find(id);
            ticket.setSolution(solution);
            ticketDAO.update(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();

    }

    @POST
    @Secured
    @Path("{id}/client/feedback")
    public Response clientFeedback(@PathParam("id") long id,
            @FormParam("rating") double rating,
            @FormParam("comment") String comment
    ) {

        TicketInfo ticketInfo = ticketService.getById(id);

        ticketService.saveClientFeedback(ticketInfo.getClient().getId(), id, rating, comment);

        TimelineItemInfo timelineItemInfo1 = new TimelineItemInfo();
        timelineItemInfo1.setDate(new Date());
        timelineItemInfo1.setItemType(TimelineItemTypeEnum.CLIENT_JOB_FEEDBACK);
        ticketService.addTimelineInfo(id, timelineItemInfo1);

        TimelineItemInfo timelineItemInfo2 = new TimelineItemInfo();
        timelineItemInfo2.setDate(new Date());
        timelineItemInfo2.setItemType(TimelineItemTypeEnum.TICKET_CLOSED);
        ticketService.addTimelineInfo(id, timelineItemInfo2);

        Sender.toSingle(ticketInfo.getTechnician().getFcmToken(),
                ticketInfo.getName(),
                "O cliente deu o feedback de " + rating + " pontos e fechou este ticket.");

        return Response.ok().build();

    }

    @POST
    @Secured
    @Path("{id}/technician/feedback")
    public Response technicianFeedback(@PathParam("id") long id,
            @FormParam("technicianId") long technicianId,
            @FormParam("rating") double rating,
            @FormParam("comment") String comment
    ) {

        ticketService.saveTechnicianFeedback(technicianId, id, rating, comment);
        return Response.ok().build();

    }

    @POST
    @Secured
    @Path("{id}/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProfilePicture(@PathParam("id") long id, @FormDataParam("file") InputStream fileInputStream) {

        try {
            Ticket ticket = ticketDAO.find(id);

            String fileName = "ticket_" + id + "_picture_" + ticket.getPictures().size();

            String uploadedFileLocation = "/var/www/technosupport-uploads/" + fileName + ".png";
            String uploadedFileURL = "http://support.technoplus.co.mz:70/technosupport-uploads/" + fileName + ".png";

            File objFile = new File(uploadedFileLocation);
            if (objFile.exists()) {
                objFile.delete();
            }

            saveToFile(fileInputStream, uploadedFileLocation);

            Picture picture = new Picture();
            picture.setTicket(ticket);
            picture.setName(uploadedFileURL);

            pictureDAO.create(picture);

            return Response.status(200).entity(uploadedFileLocation).build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().build();
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
}
