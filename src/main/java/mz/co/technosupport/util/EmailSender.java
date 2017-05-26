package mz.co.technosupport.util;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;

import javax.mail.Message;

/**
 * @author Americo Chaquisse
 */
public class EmailSender {

    public static void sendActivationMail(String mailAddress, String sponsorName, String sponsorAdmin) {
        Email email = new Email();
        MailTemplateLoader loader = new MailTemplateLoader();
        String html = loader.load("affiliate_mail");
        html = html.replace("{{sponsorName}}", sponsorName);
        html = html.replace("{{sponsorAdmin}}", sponsorAdmin);

        email.addRecipient(mailAddress, mailAddress, Message.RecipientType.TO);
        email.setTextHTML(html);

        new Mailer().sendMail(email);

    }

}
