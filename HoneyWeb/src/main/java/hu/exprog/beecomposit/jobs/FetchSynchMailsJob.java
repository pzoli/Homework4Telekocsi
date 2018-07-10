package hu.exprog.beecomposit.jobs;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.util.MailSSLSocketFactory;

import hu.exprog.beecomposit.middle.service.LogService;

public class FetchSynchMailsJob implements Job {
    private LogService logService;

    private Object getService(String name) throws NamingException {
        InitialContext ic = new InitialContext();
        Object service = ic.lookup("java:/global/Homework4Telekocsi/" + name);
        return service;
    }

    private LogService getLogService() {
        LogService result = null;
        try {
            Object service = getService("LogService");
            if (service instanceof LogService) {
                result = (LogService) service;
            }
        } catch (NumberFormatException ex) {

        } catch (Throwable t) {
            System.out.println("service lookup failed");
        }
        return result;
    }
     
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logService = getLogService();
        
        StringBuffer msg = new StringBuffer();
        
        final String username = context.getJobDetail().getJobDataMap().getString("PROP_IMAP_USER");
        final String password = context.getJobDetail().getJobDataMap().getString("PROP_IMAP_PASSWORD");

        // Assuming you are sending email through relay.jangosmtp.net
        String host = context.getJobDetail().getJobDataMap().getString("PROP_IMAP_HOST");
        String port = context.getJobDetail().getJobDataMap().getString("PROP_IMAP_PORT");

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.store.ssl.enable", "true");
        MailSSLSocketFactory socketFactory;
        try {
            socketFactory = new MailSSLSocketFactory();
            socketFactory.setTrustAllHosts(true);
            props.put("mail.store.ssl.socketFactory", socketFactory);
        } catch (GeneralSecurityException e) {
            msg.append("hiba: " + e.getMessage());
            e.printStackTrace();
        }
        //
        props.setProperty("mail.store.ssl.trust", "*");
        props.setProperty("mail.store.auth", "true");
        props.setProperty("mail.store.host", host);
        props.setProperty("mail.store.port", port);

        logService.getLog().finer("Fetch synch mails...");
        // Get the Session object.
        Session session = Session.getInstance(props, null);
        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore();
            store.connect(host, username, password);
            inbox = store.getFolder("INBOX");
            if (!inbox.isOpen()) {
                inbox.open(Folder.READ_WRITE);
            }
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            
            //Flags recent = new Flags(Flags.Flag.RECENT);
            //FlagTerm recentFlagTerm = new FlagTerm(recent, true);            
            //SearchTerm searchTerm = new AndTerm(unseenFlagTerm,recentFlagTerm);
            //SortTerm[] sortTerm = {SortTerm.DATE};
            
            //Message messages[] = ((IMAPFolder)inbox).getSortedMessages(sortTerm,searchTerm);
            //Message messages[] = ((IMAPFolder)inbox).search(searchTerm);

            Message messages[] = ((IMAPFolder)inbox).search(unseenFlagTerm);
            msg.append(" Üzenetek száma " + messages.length + "\n");
            int i = 0;
            for (; i < messages.length; i++) {
                Message message = messages[i];
                message.setFlag(Flag.SEEN, true);                
            }
            logService.getLog().finer("Fetched mail count: "+i);
        } catch (MessagingException e) {
            e.printStackTrace();
            msg.append("Hiba: " + e.getMessage());
        } finally {
            if ((inbox != null) && inbox.isOpen()) {
                try {
					inbox.close(true);
					logService.getLog().finer("Mail inbox closed.");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
            }
            if (store != null) {
                try {
                    store.close();
                    logService.getLog().finer("Mail store closed.");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
