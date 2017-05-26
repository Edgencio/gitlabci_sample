package mz.co.technosupport.fcm;

import com.pushraven.Notification;
import com.pushraven.Pushraven;

/**
 * @author Americo Chaquisse
 */
public class TicketDownstream extends Downstream{

    public void sendTicketRequestToTechnician(String clientKey, long ticketId){

        Notification notification = new Notification();
        notification.title("Um cliente precisa de si");
        notification.to(clientKey);
        notification.addNotificationAttribute("body", ticketId);

        Pushraven.push(notification);

    }

}
