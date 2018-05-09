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

import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import hu.exprog.beecomposit.back.model.Company;
import hu.exprog.beecomposit.front.manager.LocaleManager;
import hu.exprog.beecomposit.middle.service.CompanyService;
import hu.exprog.honeyweb.front.exceptions.ActionAccessDeniedException;
import hu.exprog.honeyweb.front.manager.BasicManager;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.exprog.honeyweb.utils.FieldModel;
import hu.exprog.honeyweb.utils.LookupFieldModel;
import hu.seacon.back.model.Gepjarmu;
import hu.seacon.back.model.GepjarmuFlotta;
import hu.seacon.middle.service.GepjarmuFlottaService;
import hu.seacon.middle.service.GepjarmuService;

@Named
@WindowScoped
public class GepjarmuFlottaManager extends BasicManager<GepjarmuFlotta> implements Serializable {

	private static final long serialVersionUID = -7535731155464256212L;

	@Inject
	private Logger logger;

	@Inject
	private LocaleManager localeManager;

	@Inject
	private GepjarmuFlottaService gepjarmuFlottaService;

    @Inject
    private GepjarmuService gepjarmuService;
	
    public List<Gepjarmu> getGepjarmu() {
        return gepjarmuService.findAll();
    }
    @Inject
    private CompanyService companyService;
	
    public List<Company> getCompany() {
        return companyService.findAll();
    }

	
	public GepjarmuFlottaManager() {

	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "[" + this.getClass().getName() + "] constructor finished.");
		initValue();
		initModel();
	}

	public void initValue() {
		if ((current == null) || (!current.isPresent()) || current.get().getId() != null) {
			current = Optional.of(new GepjarmuFlotta());
			clearProperties();
		}
	}

	public void save() {
		setCurrentBeanProperties();
		try {
			if (current.isPresent()) {
				if (current.get().getId() == null) {
					gepjarmuFlottaService.persist(current.get());
				} else {
					gepjarmuFlottaService.merge(current.get());
				}
			}
			current = Optional.of(new GepjarmuFlotta());
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
	protected BasicService<GepjarmuFlotta> getService() {
		return gepjarmuFlottaService;
	}

	@Override
	protected Object getDetailFieldValue(LookupFieldModel model) {
		Object result = null;
		if ("gepjarmu".equals(model.getPropertyName())) {
			result = gepjarmuService.find(Gepjarmu.class, Long.parseLong((String) model.getValue()));
		} else {
		}
		if ("company".equals(model.getPropertyName())) {
			result = companyService.find(Company.class, Long.parseLong((String) model.getValue()));
		} else {
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
		if (event.getObject() instanceof Gepjarmu) {
			Gepjarmu selected = (Gepjarmu) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("gepjarmu")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		}
		if (event.getObject() instanceof Company) {
			Company selected = (Company) event.getObject();
			if (current != null) {
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("company")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}
		}
		else {
		}
	}


	@Override
	public boolean checkEditableRights(GepjarmuFlotta entity) throws ActionAccessDeniedException {
		return true;
	}


	@Override
	public boolean checkDeleteRight(GepjarmuFlotta entity) throws ActionAccessDeniedException {
		return true;
	}
}
