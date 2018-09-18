package servicio.transaccion.sofasa;


import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmailUtil {
    private static final String TAG = SendEmailUtil.class.getSimpleName();

    public static void sendTextEmail(final String asunto, final String cuerpo, final String destinatario) {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    Properties properties = System.getProperties();
                    Properties prop = new Properties();
                    prop.put("mail.smtp.starttls.enable", "true");
                    prop.put("mail.smtp.host", ConstantsUtil.SMTP_HOST);
                    prop.put("mail.smtp.auth", "true");
                    prop.put("mail.smtp.port", ConstantsUtil.SMTP_PORT);
                    
                    Session session = Session.getDefaultInstance(prop, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(ConstantsUtil.SMTP_USER, ConstantsUtil.SMTP_PASSWORD);
                        }
                    });

                    try {
                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(ConstantsUtil.SMTP_USER));
                        String to = destinatario;
                        InternetAddress[] address = InternetAddress.parse(to, true);
                        message.addRecipients(RecipientType.TO, address);
                        message.setSubject(asunto);
                        message.setText(cuerpo);
                        Transport.send(message);
                        System.out.println("--Sent message successfully....!: ");
                    } catch (MessagingException var5) {
                        var5.printStackTrace();
                        System.out.println("--Error!: " + var5.getMessage());
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                    System.out.println("--Error!: " + var6.getMessage());
                }

            }
        })).start();
    }
    
  

}
