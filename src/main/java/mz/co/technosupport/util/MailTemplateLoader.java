package mz.co.technosupport.util;

import org.apache.tika.io.IOUtils;

import java.io.IOException;

/**
 * @author Americo Chaquisse
 */
public class MailTemplateLoader {

    public String load(String name){

        String result = "";

        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream("mail/"+name+".html"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
