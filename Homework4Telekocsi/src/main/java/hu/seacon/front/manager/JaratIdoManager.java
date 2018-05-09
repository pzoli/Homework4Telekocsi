package hu.seacon.front.manager;

import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.velocity.VelocityContext;
import org.primefaces.PrimeFaces;

import hu.exprog.beecomposit.front.manager.LocaleManager;
import hu.exprog.honeyweb.front.exceptions.ActionAccessDeniedException;
import hu.exprog.honeyweb.front.manager.BasicManager;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.exprog.honeyweb.utils.FieldModel;
import hu.exprog.honeyweb.utils.LookupFieldModel;
import hu.seacon.back.model.JaratIdo;
import hu.seacon.middle.service.JaratIdoService;
import hu.seacon.utils.velocity.TemplateTools;

@Named
@WindowScoped
public class JaratIdoManager extends BasicManager<JaratIdo> implements Serializable {

	private static final long serialVersionUID = -7535731155464256212L;

	@Inject
	private Logger logger;

	@Inject
	private LocaleManager localeManager;

	@Inject
	private JaratIdoService jaratIdoService;

	public JaratIdoManager() {

	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "[" + this.getClass().getName() + "] constructor finished.");
		initValue();
		initModel();
	}

	public void initValue() {
		if ((current == null) || (!current.isPresent()) || current.get().getId() != null) {
			current = Optional.of(new JaratIdo());
			clearProperties();
		}
	}

	public void save() {
		setCurrentBeanProperties();
		try {
			if (current.isPresent()) {
				if (current.get().getId() == null) {
					jaratIdoService.persist(current.get());
				} else {
					jaratIdoService.merge(current.get());
				}
			}
			current = Optional.of(new JaratIdo());
			clearProperties();
			PrimeFaces.current().ajax().addCallbackParam(VALIDATION_FAULT, false);
		} catch (EJBException e) {
			Throwable ex = e.getCause();
			if (ex instanceof ConstraintViolationException) {
				Set<ConstraintViolation<?>> msg = ((ConstraintViolationException) ex).getConstraintViolations();
				msg.forEach(c -> {
					StringBuffer validationExcMsg = new StringBuffer();
					ConstraintDescriptor<?> desc = c.getConstraintDescriptor();
					logger.info(desc.getAttributes().get("message").toString());
					String temp = c.getMessageTemplate();
					logger.info(temp);
					validationExcMsg.append(c.getPropertyPath()).append(":").append(c.getMessage());
					FacesMessage message = new FacesMessage(validationExcMsg.toString());
					FacesContext.getCurrentInstance().addMessage(null, message);
				});
			} else {
				FacesMessage message = new FacesMessage("Failed: " + e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			PrimeFaces.current().ajax().addCallbackParam(VALIDATION_FAULT, true);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected BasicService<JaratIdo> getService() {
		return jaratIdoService;
	}

	@Override
	protected Object getDetailFieldValue(LookupFieldModel model) {
		return null;
	}

	@Override
	protected Locale getLocale() {
		return localeManager.getLocale();
	}

	@Override
	public boolean checkListRight() throws ActionAccessDeniedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean checkSaveRight() throws ActionAccessDeniedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean checkDeleteRight() throws ActionAccessDeniedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object postProcess(FieldModel field, Object value) {
		return value;
	}

	@Override
	public Object preProcess(FieldModel field, Object value) {
		return value;
	}

	public void generateXHTMLFromTemplate() {
		TemplateTools templateTools = new TemplateTools(getDomainClass(), getColumns(), getEntityInfoMap(), getLookupFieldInfoMap(), "d:/work/new/");
		templateTools.setCreateConverter(true);
		getRightParamss(templateTools.getContext());
		templateTools.getContext().put("addDocument", false);
		templateTools.getContext().put("SEAFLEET_HOME", "d:/development/seacon/SeaFleet");
		templateTools.generateXHTMLFromTemplate();
		templateTools.moveFiles();
	}
	
	private void getRightParamss(VelocityContext context) {
		context.put("functionId", 1137);
		context.put("functionName", getDomainClass().getSimpleName());
		context.put("functionLabel", "Járat idő");
		context.put("functionURL", "/faces/seafleet/" + StringUtils.uncapitalize(getDomainClass().getSimpleName()) + "Table.xhtml");
		context.put("menuId", 289);
		context.put("menuLabelsId", 283);
	}

	@Override
	public boolean checkEditableRights(JaratIdo entity) throws ActionAccessDeniedException { 
		return true;
	}

	@Override
	public boolean checkDeleteRight(JaratIdo entity) throws ActionAccessDeniedException {
		return true;
	}
}
