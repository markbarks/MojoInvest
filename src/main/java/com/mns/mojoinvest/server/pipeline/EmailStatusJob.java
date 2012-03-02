package com.mns.mojoinvest.server.pipeline;

import com.google.appengine.tools.pipeline.Job1;
import com.google.appengine.tools.pipeline.Value;
import com.google.common.base.Joiner;
import org.joda.time.LocalDate;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class EmailStatusJob extends Job1<Void, List<String>> {

    @Override
    public Value<Void> run(List<String> messages) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("marknuttallsmith@gmail.com", "Mojoinvest Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("marknuttallsmith@gmail.com", "Mark Nuttall-Smith"));
            msg.setSubject("Daily pipeline status for " + new LocalDate());
            msg.setText(Joiner.on("\n").join(messages));
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        }

        return null;
    }
}
