package hu.exprog.beecomposit.middle.schedulers;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import hu.exprog.beecomposit.back.model.EmailImapParams;
import hu.exprog.beecomposit.back.model.EmailSmtpParams;
import hu.exprog.beecomposit.jobs.FetchSynchMailsJob;
import hu.exprog.beecomposit.jobs.SendRegistrtionhMailsJob;
import hu.exprog.beecomposit.middle.service.AppProperties;
import hu.exprog.beecomposit.middle.service.EmailService;

@Named
@ApplicationScoped
public class SystemJobScheduler implements Serializable {

	private static final long serialVersionUID = 4848808869471471866L;

	private static final int PERIOD_IN_SECOND = 15;

	private static final String triggerId = "system-trigger-";
	private static final String jobId = "system-job-";
	private static final String fetchKeyPart = "fetch-synch";
	private static final String sendRegistrationKeyPart = "send-synch";
	
    @Resource(mappedName = "java:jboss/Homework4Telekocsi/velocityLogFile")
	private String velocityLogFile;
    
    @Resource(mappedName = "java:jboss/Homework4Telekocsi/workingDirectory")
    private String workingDirectory;

    @Resource(mappedName = "java:jboss/Homework4Telekocsi/webContext")
    private String webContext;
    
	@Inject
	private AppProperties appProperties;

	@Inject
	private EmailService emailService;

	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		startSchediler();
		stopSendRegister();
		startSendRegister();
    }
	
	public void startSchediler() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (!scheduler.isStarted()) {
				scheduler.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}
	
	public void stopFetch() {
		if (isScheduledFetch()) {
			unscheduleFetchMails();
		}
	}

	public void startFetch() {
		if (!isScheduledFetch()) {
			scheduleFetchMails();
		}
	}

	public void startSendRegister() {
		if (!isScheduledSendRegistration()) {
			scheduleRegistrationMails();
		}
	}

	public void stopSendRegister() {
		if (isScheduledSendRegistration()) {
			unscheduleSendRegistrationMails();
		}
	}

	public boolean isScheduledFetch() {
		boolean result = false;
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + fetchKeyPart, "system");
				Trigger trigger = scheduler.getTrigger(triggerKey);
				result = trigger.mayFireAgain();
			}
		} catch (Exception e) {

		}
		return result;
	}

	public boolean isScheduledSendRegistration() {
		boolean result = false;
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + sendRegistrationKeyPart, "system");
				Trigger trigger = scheduler.getTrigger(triggerKey);
				result = trigger.mayFireAgain();
			}
		} catch (Exception e) {

		}
		return result;
	}

	public void unscheduleFetchMails() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + fetchKeyPart, "system");
				scheduler.unscheduleJob(triggerKey);
			}
		} catch (Exception e) {

		}
	}

	public void unscheduleSendRegistrationMails() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + sendRegistrationKeyPart, "system");
				scheduler.unscheduleJob(triggerKey);
			}
		} catch (Exception e) {

		}
	}

	public void scheduleFetchMails() {
		FacesMessage msg;
		try {
			TriggerKey triggerKey = new TriggerKey(triggerId + fetchKeyPart, "system");
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				Calendar startCalendar = new GregorianCalendar();
				startCalendar.add(Calendar.SECOND, 2);
				Trigger trigger = newTrigger().withIdentity(triggerKey).startAt(startCalendar.getTime()).withSchedule(simpleSchedule().withIntervalInSeconds(PERIOD_IN_SECOND).repeatForever()).build();
				JobDetail job = newJob(FetchSynchMailsJob.class).withIdentity(jobId + fetchKeyPart, "system").build();

				EmailImapParams emailMailParams = emailService.getMyImapAccount(appProperties.getSystemId());

				job.getJobDataMap().put("instant", false);
				job.getJobDataMap().put("PROP_IMAP_USER", emailMailParams.getImapUser());
				job.getJobDataMap().put("PROP_IMAP_PASSWORD", emailMailParams.getImapPassword());
				job.getJobDataMap().put("PROP_IMAP_HOST", emailMailParams.getImapServerHost());
				job.getJobDataMap().put("PROP_IMAP_PORT", emailMailParams.getImapServerPort());
				scheduler.scheduleJob(job, trigger);
			}
			msg = new FacesMessage("Levelek lekérdezése", "Lekérdezés elindítva");
		} catch (Exception ex) {
			msg = new FacesMessage("Levelek lekérdezésének indítása sikertelen", "[" + ex.getMessage() + "]");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			facesContext.addMessage(null, msg);
		}
	}

	public void scheduleRegistrationMails() {
		FacesMessage msg;
		try {
			TriggerKey triggerKey = new TriggerKey(triggerId + sendRegistrationKeyPart, "system");
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (!scheduler.isStarted()) {
				scheduler.start();
			}
			if (scheduler.isStarted()) {
				Calendar startCalendar = new GregorianCalendar();
				startCalendar.add(Calendar.SECOND, 2);
				Trigger trigger = newTrigger().withIdentity(triggerKey).startAt(startCalendar.getTime()).withSchedule(simpleSchedule().withIntervalInSeconds(PERIOD_IN_SECOND).repeatForever()).build();
				JobDetail job = newJob(SendRegistrtionhMailsJob.class).withIdentity(jobId + sendRegistrationKeyPart, "system").build();

				EmailSmtpParams emailMailParams = emailService.getMySmtpAccount(appProperties.getSystemId());
				if (emailMailParams != null) {
					job.getJobDataMap().put("instant", false);
					
					job.getJobDataMap().put("context", webContext);
					job.getJobDataMap().put("sourcevm", "mail_register_hu.vm");
					job.getJobDataMap().put("PROP_SENDER", emailMailParams.getSenderEmailAddress());
					job.getJobDataMap().put("PROP_SENDER_NAME", emailMailParams.getSenderName());
					job.getJobDataMap().put("PROP_SMTP_USER", emailMailParams.getSmtpUser());
					job.getJobDataMap().put("PROP_SMTP_PASSWORD", emailMailParams.getSmtpPassword());
					job.getJobDataMap().put("PROP_SMTP_HOST", emailMailParams.getSmtpServerHost());
					job.getJobDataMap().put("PROP_SMTP_PORT", emailMailParams.getSmtpServerPort());
					job.getJobDataMap().put("velocityLogFile", velocityLogFile);
					job.getJobDataMap().put("workingDirectory", workingDirectory);
					scheduler.scheduleJob(job, trigger);
				}
			}
			msg = new FacesMessage("Levelek kézbesítése", "Kézbesítés ütemezve");
		} catch (Exception ex) {
			msg = new FacesMessage("Levelek kézbesítésének indítása sikertelen", "[" + ex.getMessage() + "]");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			facesContext.addMessage(null, msg);
		}
	}

	public void deleteSystemJobs() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				scheduler.deleteJob(new JobKey(jobId + fetchKeyPart, "system"));
				scheduler.deleteJob(new JobKey(jobId + sendRegistrationKeyPart, "system"));
			}
		} catch (Exception ex) {

		}
	}

	public void pauseSystemJobs() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + fetchKeyPart, "system");
				scheduler.pauseTrigger(triggerKey);
				triggerKey = new TriggerKey(triggerId + sendRegistrationKeyPart, "system");
				scheduler.pauseTrigger(triggerKey);
			}
		} catch (Exception ex) {

		}
	}

	public void resumeSystemJobs() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			if (scheduler.isStarted()) {
				TriggerKey triggerKey = new TriggerKey(triggerId + fetchKeyPart, "system");
				scheduler.resumeTrigger(triggerKey);
				triggerKey = new TriggerKey(triggerId + sendRegistrationKeyPart, "system");
				scheduler.resumeTrigger(triggerKey);
			}
		} catch (Exception ex) {

		}
	}

}
