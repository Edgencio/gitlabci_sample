package mz.co.technosupport.fcm;

import com.pushraven.Pushraven;

/**
 * @author Americo Chaquisse
 */
public class Downstream {

    public Downstream(){
        Pushraven.setKey(Constants.FCM_SERVER_KEY);
    }
}
