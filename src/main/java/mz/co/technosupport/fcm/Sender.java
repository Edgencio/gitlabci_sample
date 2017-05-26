package mz.co.technosupport.fcm;

import mz.co.technosupport.info.ticket.TimelineItemInfo;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Americo Chaquisse
 */
public class Sender {

    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    public static void toSingle(String userDeviceIdKey, String title,String body){

        String authKey = Constants.FCM_SERVER_KEY;
        String FMCurl = API_URL_FCM;

        try {
            URL url = new URL(FMCurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+authKey);
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject json = new JSONObject();
            json.put("to",userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", title);   // Notification title
            info.put("body", body); // Notification body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
