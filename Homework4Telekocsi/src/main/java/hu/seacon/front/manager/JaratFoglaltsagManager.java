package hu.seacon.front.manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import hu.exprog.beecomposit.front.manager.LocaleManager;
import hu.exprog.beecomposit.middle.service.SystemUserService;
import hu.exprog.honeyweb.front.exceptions.ActionAccessDeniedException;
import hu.exprog.honeyweb.front.manager.BasicManager;
import hu.exprog.honeyweb.middle.services.BasicService;
import hu.exprog.honeyweb.utils.FieldModel;
import hu.exprog.honeyweb.utils.JSFUtil;
import hu.exprog.honeyweb.utils.LookupFieldModel;
import hu.seacon.back.model.IgenyStatusz;
import hu.seacon.back.model.Jarat;
import hu.seacon.back.model.JaratFoglaltsag;
import hu.seacon.back.model.JaratIgeny;
import hu.seacon.back.model.JaratIrany;
import hu.seacon.middle.service.IgenyStatuszService;
import hu.seacon.middle.service.JaratFoglaltsagService;
import hu.seacon.middle.service.JaratIgenyService;
import hu.seacon.middle.service.JaratIranyService;
import hu.seacon.middle.service.JaratService;
import hu.seacon.utils.velocity.TemplateTools;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Named
@WindowScoped
public class JaratFoglaltsagManager extends BasicManager<JaratFoglaltsag> implements Serializable {

	private static final long serialVersionUID = 5931946999778177396L;

	@Inject
	private Logger logger;
	
	@Inject
	private LocaleManager localeManager;

	@Inject
	private JaratIgenyService jaratIgenyService;

	@Inject
	private JaratFoglaltsagService jaratFoglaltsagService;

	@Inject
	private JaratService jaratService;
	
	@Inject
	private JaratIranyService jaratIranyService;
	
	@Inject
	private SystemUserService systemUserService;
	
	@Inject
	private IgenyStatuszService igenyStatuszService;

	private StreamedContent exportFile;

	public JaratFoglaltsagManager() {

	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "[" + this.getClass().getName() + "] constructor finished.");
		initValue();
		initModel();
	}

	public void initValue() {
		if ((current == null) || (!current.isPresent()) || current.get().getId() != null) {
			current = Optional.of(new JaratFoglaltsag());
			clearProperties();
		}
	}

	public void save() {
		setCurrentBeanProperties();
		try {
			if (current.isPresent()) {
				if (current.get().getId() == null) {
					jaratFoglaltsagService.persist(current.get());
				} else {
					jaratFoglaltsagService.merge(current.get());
				}
			}
			current = Optional.of(new JaratFoglaltsag());
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
	protected BasicService<JaratFoglaltsag> getService() {
		return jaratFoglaltsagService;
	}

	@Override
	protected Object getDetailFieldValue(LookupFieldModel model) {
		Object result = null;
		if ("jarat".equals(model.getPropertyName())) {
			result = jaratService.find(Jarat.class, Long.parseLong((String) model.getValue()));
		} else if ("jaratIgeny".equals(model.getPropertyName())) {
			result = jaratIgenyService.find(JaratIgeny.class, Long.parseLong((String) model.getValue()));
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
				formModel.getControls().stream().filter(c -> ((FieldModel) c.getData()).getPropertyName().equals("jaratIrany")).forEach(d -> setDetailFieldValue((LookupFieldModel) d.getData(), selected.getId()));
			}			
		}
	}

	public List<JaratIgeny> getJaratIgeny() {
		return jaratIgenyService.findAll();
	}

	public List<Jarat> getJarat() {
		return jaratService.findAll();
	}

	public void generateXHTMLFromTemplate() {
		TemplateTools templateTools = new TemplateTools(getDomainClass(), getColumns(), getEntityInfoMap(), getLookupFieldInfoMap(), "d:/work/new/");
		getRightParamss(templateTools.getContext());
		templateTools.getContext().put("SEAFLEET_HOME", "d:/development/seacon/SeaFleet");
		templateTools.getContext().put("addDocument", false);
		templateTools.generateXHTMLFromTemplate();
		//templateTools.moveFiles();
	}
	
	private void getRightParamss(VelocityContext context) {
		context.put("functionId", 1152);
		context.put("functionName", getDomainClass().getSimpleName());
		context.put("functionLabel", "Járat foglaltság");
		context.put("functionURL", "/faces/seafleet/" + StringUtils.uncapitalize(getDomainClass().getSimpleName()) + "Table.xhtml");
		context.put("menuId", 292);
		context.put("menuLabelsId", 286);
	}

	@Override
	public boolean checkEditableRights(JaratFoglaltsag entity) throws ActionAccessDeniedException {
		return true;
	}

	@Override
	public boolean checkDeleteRight(JaratFoglaltsag entity) throws ActionAccessDeniedException {
		return true;
	}
	
    public void reportSelectedAssets() {
        try {
            try {            
				InputStream reportStream = this.getClass().getClassLoader().getResourceAsStream("jaratfoglaltsag.jrxml");
                JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("ReportTitle", JSFUtil.evalELToString("#{msg['jaratfoglaltsag-table']}"));
                params.put("ReportDate", new Date());

                params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
                params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.getDefault());
                params.put(JRParameter.REPORT_LOCALE, Locale.getDefault());
                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(selectedItems);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);
                byte[] exportData = JasperExportManager.exportReportToPdf(jasperPrint);
                InputStream stream = new ByteArrayInputStream(exportData);
                exportFile = new DefaultStreamedContent(stream, "application/pdf", "jaratfoglaltsag-report.pdf");
            } catch (JRException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, JSFUtil.evalELToString("#{msg['reporting-exception-msg']}"), ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            exportFile = null;
        }
    }

	public StreamedContent getExportFile() {
		return exportFile;
	}

	public void setExportFile(StreamedContent exportFile) {
		this.exportFile = exportFile;
	}

}
