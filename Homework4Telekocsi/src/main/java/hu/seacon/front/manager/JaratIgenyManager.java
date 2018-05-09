package hu.seacon.front.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import hu.exprog.beecomposit.back.model.SystemUser;
import hu.exprog.beecomposit.back.resources.AuthBackingBean;
import hu.exprog.beecomposit.front.manager.LocaleManager;
import hu.exprog.beecomposit.middle.service.SystemUserService;
import hu.exprog.honeyweb.front.exceptions.ActionAccessDeniedException;
import hu.exprog.honeyweb.front.manager.BasicManager;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.exprog.honeyweb.utils.FieldModel;
import hu.exprog.honeyweb.utils.LookupFieldModel;
import hu.seacon.back.model.IgenyStatusz;
import hu.seacon.back.model.Jarat;
import hu.seacon.back.model.JaratIgeny;
import hu.seacon.back.model.JaratIrany;
import hu.seacon.middle.service.IgenyStatuszService;
import hu.seacon.middle.service.JaratIgenyService;
import hu.seacon.middle.service.JaratService;
import hu.seacon.utils.velocity.TemplateTools;

@Named
@WindowScoped
public class JaratIgenyManager extends BasicManager<JaratIgeny> implements Serializable {

	private static final long serialVersionUID = 8500183063968557347L;

	@Inject
	private Logger logger;

	@Inject
	private LocaleManager localeManager;

	@Inject
	private JaratIgenyService jaratIgenyService;

	@Inject
	private JaratService jaratService;

	@Inject
	private SystemUserService systemUserService;

	@Inject
	private IgenyStatuszService igenyStatuszService;

	@Inject
	private AuthBackingBean authBackingBean;

	public JaratIgenyManager() {

	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "[" + this.getClass().getName() + "] constructor finished.");
		if (!authBackingBean.checkAdminRights() && !getStaticFilters().containsKey("igenybeVevo")) {
			getStaticFilters().put("igenybeVevo", authBackingBean.getLoggedInUser());
		}
		initModel();
		initValue();
	}

	public void initValue() {
		current = Optional.of(new JaratIgeny());
		current.get().setIgenybeVevo(authBackingBean.getLoggedInUser());
		current.get().setIgenyBejelento(authBackingBean.getLoggedInUser());
		setControlData(current.get());
	}

	public void save() {
		setCurrentBeanProperties();
		try {
			if (current.isPresent()) {
				if (current.get().getId() == null) {
					jaratIgenyService.persist(current.get());
				} else {
					jaratIgenyService.merge(current.get());
				}
			}
			current = Optional.of(new JaratIgeny());
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
	protected BasicService<JaratIgeny> getService() {
		return jaratIgenyService;
	}

	@Override
	protected Object getDetailFieldValue(LookupFieldModel model) {
		Object result = null;
		if ("jarat".equals(model.getPropertyName())) {
			result = jaratService.find(Jarat.class, Long.parseLong((String) model.getValue()));
		} else if ("igenyStatusz".equals(model.getPropertyName())) {
			result = igenyStatuszService.find(IgenyStatusz.class, Long.parseLong((String) model.getValue()));
		} else if ("igenybeVevo".equals(model.getPropertyName())) {
			result = systemUserService.find(SystemUser.class, Long.parseLong((String) model.getValue()));
		} else if ("igenyBejelento".equals(model.getPropertyName())) {
			result = systemUserService.find(SystemUser.class, Long.parseLong((String) model.getValue()));
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

	public void handleReturn(SelectEvent event) {
		if (event.getObject() instanceof IgenyStatusz) {
			IgenyStatusz selected = (IgenyStatusz) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("igenyStatusz")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		} else if (event.getObject() instanceof JaratIrany) {
			JaratIrany selected = (JaratIrany) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("jaratIgeny")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		} else if (event.getObject() instanceof SystemUser) {
			SystemUser selected = (SystemUser) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("igenybeVevo")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		}
	}

	public List<Jarat> getJarat() {
		return jaratService.findAll();
	}

	public List<IgenyStatusz> getIgenyStatusz() {
		List<IgenyStatusz> result = igenyStatuszService.findAll();
		if (!authBackingBean.checkAdminRights()) {
			result = result.stream().filter(s -> s.getIgenyKod() != null && !s.getIgenyKod().equalsIgnoreCase("v")).collect(Collectors.toList());
		}
		return result; 
	}

	public List<SystemUser> getIgenybeVevo() {
		return systemUserService.findAll();
	}

	public List<SystemUser> getIgenyBejelento() {
		return systemUserService.findAll();
	}

	public void generateXHTMLFromTemplate() {
		TemplateTools templateTools = new TemplateTools(getDomainClass(), getColumns(), getEntityInfoMap(), getLookupFieldInfoMap(), "d:/work/new/");
		getRightParams(templateTools.getContext());
		templateTools.getContext().put("SEAFLEET_HOME", "d:/development/seacon/SeaFleet");
		templateTools.setCreateConverter(true);
		templateTools.getContext().put("addDocument", false);
		templateTools.generateXHTMLFromTemplate();
		// templateTools.moveFiles();
	}

	private void getRightParams(VelocityContext context) {
		context.put("functionId", 1147);
		context.put("functionName", getDomainClass().getSimpleName());
		context.put("functionLabel", "Járat igény");
		context.put("functionURL", "/faces/seafleet/" + StringUtils.uncapitalize(getDomainClass().getSimpleName()) + "Table.xhtml");
		context.put("menuId", 291);
		context.put("menuLabelsId", 285);
	}

	public boolean checkStateOfCurrent() {
		boolean result = current.isPresent() && current.get().getIgenyStatusz() != null && current.get().getIgenyStatusz().getIgenyKod().equals("V");
		return result;
	}

	@Override
	public boolean checkEditableRights(JaratIgeny entity) throws ActionAccessDeniedException {
		//boolean result = entity != null && entity.getIgenyStatusz() != null && !entity.getIgenyStatusz().getIgenyKod().equalsIgnoreCase("V");
		boolean result = true;
		return result;
	}

	@Override
	public boolean checkDeleteRight(JaratIgeny entity) throws ActionAccessDeniedException {
		return checkEditableRights(entity);
	}

}
