package hu.seacon.front.manager;

import java.io.Serializable;
import java.util.List;
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
import org.primefaces.event.SelectEvent;

import hu.exprog.beecomposit.front.manager.LocaleManager;
import hu.exprog.honeyweb.front.exceptions.ActionAccessDeniedException;
import hu.exprog.honeyweb.front.manager.BasicManager;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.exprog.honeyweb.utils.FieldModel;
import hu.exprog.honeyweb.utils.LookupFieldModel;
import hu.seacon.back.model.Gepjarmu;
import hu.seacon.back.model.Jarat;
import hu.seacon.back.model.JaratIdo;
import hu.seacon.back.model.JaratIrany;
import hu.seacon.middle.service.GepjarmuService;
import hu.seacon.middle.service.JaratIdoService;
import hu.seacon.middle.service.JaratIranyService;
import hu.seacon.middle.service.JaratService;
import hu.seacon.utils.velocity.TemplateTools;

@Named
@WindowScoped
public class JaratManager extends BasicManager<Jarat> implements Serializable {

	private static final long serialVersionUID = 6704334947011843239L;

	@Inject
	private Logger logger;

	@Inject
	private LocaleManager localeManager;

	@Inject
	private JaratService jaratService;

	@Inject
	private JaratIdoService jaratIdoService;

	@Inject
	private JaratIranyService jaratIranyService;
	
	@Inject
	private GepjarmuService gepjarmuService;

	public JaratManager() {

	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "[" + this.getClass().getName() + "] constructor finished.");
		initValue();
		initModel();
	}

	public void initValue() {
		if ((current == null) || (!current.isPresent()) || current.get().getId() != null) {
			current = Optional.of(new Jarat());
			clearProperties();
		}
	}

	public void save() {
		setCurrentBeanProperties();
		try {
			if (current.isPresent()) {
				if (current.get().getId() == null) {
					jaratService.persist(current.get());
				} else {
					jaratService.merge(current.get());
				}
			}
			current = Optional.of(new Jarat());
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
	
	public List<?> autoComplete(String rendszam) {
		List<Gepjarmu> result = gepjarmuService.findByAzonosito(rendszam);
		return result;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected BasicService<Jarat> getService() {
		return jaratService;
	}

	@Override
	protected Object getDetailFieldValue(LookupFieldModel model) {
		Object result = null;
		if ("jaratIdo".equals(model.getPropertyName())) {
			result = jaratIdoService.find(JaratIdo.class, Long.parseLong((String) model.getValue()));
		} else if ("gepjarmu".equals(model.getPropertyName())) {
			result = gepjarmuService.find(Gepjarmu.class, Long.parseLong((String) model.getValue()));
		} else if ("jaratIrany".equals(model.getPropertyName())) {
			result = jaratIranyService.find(JaratIrany.class, Long.parseLong((String) model.getValue()));
		}
		return result;
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

	public List<JaratIdo> getJaratIdo() {
		return jaratIdoService.findAll();
	}

	public List<JaratIrany> getJaratIrany() {
		return jaratIranyService.findAll();
	}
	
	public List<Gepjarmu> getGepjarmu() {
		return gepjarmuService.findAll();
	}

	public void handleReturn(SelectEvent event) {
		if (event.getObject() instanceof JaratIdo) {
			JaratIdo selected = (JaratIdo) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("jaratIdo")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		} else if (event.getObject() instanceof JaratIrany) {
			JaratIrany selected = (JaratIrany) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("jaratIrany")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}			
		}
	}

	public void generateXHTMLFromTemplate() {
		TemplateTools templateTools = new TemplateTools(getDomainClass(), getColumns(), getEntityInfoMap(), getLookupFieldInfoMap(), "d:/work/new/");
		getRightParamss(templateTools.getContext());
		templateTools.getContext().put("SEAFLEET_HOME", "d:/development/seacon/SeaFleet");
		templateTools.setCreateConverter(true);
		templateTools.getContext().put("addDocument", false);
		templateTools.generateXHTMLFromTemplate();
		//templateTools.moveFiles();
	}
	
	private void getRightParamss(VelocityContext context) {
		context.put("functionId", 1142);
		context.put("functionName", getDomainClass().getSimpleName());
		context.put("functionLabel", "Járat");
		context.put("functionURL", "/faces/seafleet/" + StringUtils.uncapitalize(getDomainClass().getSimpleName()) + "Table.xhtml");
		context.put("menuId", 290);
		context.put("menuLabelsId", 284);
	}

	@Override
	public boolean checkEditableRights(Jarat entity) throws ActionAccessDeniedException {
		return true;
	}

	@Override
	public boolean checkDeleteRight(Jarat entity) throws ActionAccessDeniedException {
		return true;
	}
}
