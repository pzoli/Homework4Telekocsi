package hu.seacon.utils.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DisplayTool;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;
import hu.exprog.honeyweb.front.annotations.LookupFieldInfo;
import hu.exprog.honeyweb.utils.ColumnModel;
import hu.exprog.honeyweb.utils.velocity.VelocityTransformUtils;

public class TemplateTools {

	private final Properties properties = new Properties();
	private VelocityContext context = new VelocityContext();
	private Class<?> domainClass;
	private List<ColumnModel> columnModel;
	private Map<String, EntityFieldInfo> entityInfoMap;
	private Map<String, LookupFieldInfo> lookupFieldInfoMap;
	private String tableXhtmlName;
	private String editorXhtmlName;
	private String rightsSqlName;
	private String managerBeanName;
	private String workingDirectory;
	private String i18nName;
	private String entityFindAllValidFile;
	private String converterName;
	private boolean createConverter;

	public TemplateTools(Class<?> domainClass, List<ColumnModel> list, Map<String, EntityFieldInfo> entityInfoMap, Map<String, LookupFieldInfo> lookupFieldInfoMap, String workingDirecotry) {
		this.domainClass = domainClass;
		this.columnModel = list;
		this.entityInfoMap = entityInfoMap;
		this.lookupFieldInfoMap = lookupFieldInfoMap;
		this.workingDirectory = workingDirecotry;
	}

	public void generateXHTMLFromTemplate() {
		properties.put(RuntimeConstants.INPUT_ENCODING, StandardCharsets.UTF_8.name());
		properties.put(RuntimeConstants.OUTPUT_ENCODING, StandardCharsets.UTF_8.name());
		properties.put(RuntimeConstants.ENCODING_DEFAULT, StandardCharsets.UTF_8.name());
		File templateFilePath = new File("d:\\work\\new");
		String absolutePath = templateFilePath.getAbsolutePath();
		properties.setProperty("file.resource.loader.path", absolutePath);
		properties.setProperty("runtime.log", "d:\\work\\velocity.log");

		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init(properties);

		Template table = velocityEngine.getTemplate("templateTable.vm");
		Template editor = velocityEngine.getTemplate("templateEditor.vm");
		Template manager = velocityEngine.getTemplate("templateManager.vm");
		Template rights = velocityEngine.getTemplate("rights.vm");
		Template generateFiles = velocityEngine.getTemplate("generateFiles.vm");
		Template i18n = velocityEngine.getTemplate("i18n.vm");
		Template entityFindAllValid = velocityEngine.getTemplate("entity.vm");
		Template converter = null;
		if (createConverter) {
			converter = velocityEngine.getTemplate("converter.vm");
		}
		String dateFormat = SimpleDateFormat.getDateInstance().format(new Date()).replaceAll("\\.", "");

		context.put("display", new DisplayTool());
		context.put("String", new String());
		context.put("date", dateFormat);
		context.put("functionName", domainClass.getSimpleName());
		context.put("managerBeanName", StringUtils.uncapitalize(domainClass.getSimpleName()) + "Manager");
		context.put("tableId", StringUtils.uncapitalize(domainClass.getSimpleName()) + "Table");
		context.put("tableVar", "j");
		context.put("panelName", StringUtils.uncapitalize(domainClass.getSimpleName()) + "Panel");
		context.put("tableModel", StringUtils.uncapitalize(domainClass.getSimpleName()) + "TableModel");
		context.put("panelListRendered", StringUtils.uncapitalize(domainClass.getSimpleName()) + "PanelListaRendered");
		context.put("columnModel", columnModel);
		context.put("entityInfoMap", entityInfoMap);
		context.put("lookupFieldInfoMap", lookupFieldInfoMap);
		
		tableXhtmlName = workingDirectory + domainClass.getSimpleName() + "Table.xhtml";
		editorXhtmlName = workingDirectory + domainClass.getSimpleName() + "Editor.xhtml";
		managerBeanName = workingDirectory + domainClass.getSimpleName() + "Manager.java";
		rightsSqlName = workingDirectory + dateFormat + "_" + domainClass.getSimpleName() + ".sql";
		i18nName = workingDirectory + domainClass.getSimpleName() + "_i18n.properties";
		entityFindAllValidFile = workingDirectory + domainClass.getSimpleName() + "_entityFindAllValid.txt";
		converterName = workingDirectory + domainClass.getSimpleName() + "Converter.java";

		VelocityTransformUtils.transformTemplateToFile(table, context, tableXhtmlName);
		VelocityTransformUtils.transformTemplateToFile(editor, context, editorXhtmlName);
		VelocityTransformUtils.transformTemplateToFile(manager, context, managerBeanName);
		VelocityTransformUtils.transformTemplateToFile(rights, context, rightsSqlName);
		VelocityTransformUtils.transformTemplateToFile(generateFiles, context, workingDirectory + domainClass.getSimpleName() + "-generated.properties");
		VelocityTransformUtils.transformTemplateToFile(i18n, context, i18nName);
		VelocityTransformUtils.transformTemplateToFile(entityFindAllValid, context, entityFindAllValidFile);
		if (createConverter) {
			VelocityTransformUtils.transformTemplateToFile(converter, context, converterName);
		}
	}

	public VelocityContext getContext() {
		return context;
	}

	public void moveFile(String src, String dest) throws IOException {
		File destFile = new File(dest);
		if (destFile.exists()) {
			destFile.delete();
		}
		FileUtils.moveFile(new File(src), destFile);
	}

	public void moveFiles() {
		Properties props = new Properties();
		File files = new File(new File(workingDirectory), domainClass.getSimpleName() + "-generated.properties");
		try (FileInputStream stream = new FileInputStream(files)) {
			props.load(stream);
			moveFile(tableXhtmlName, props.getProperty("table"));
			moveFile(editorXhtmlName, props.getProperty("editor"));
			moveFile(managerBeanName, props.getProperty("manager"));
			moveFile(rightsSqlName, props.getProperty("menurights"));
			if (createConverter) {
				moveFile(converterName, props.getProperty("converter"));
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Source generated."));
		} catch (IOException ex) {
			ex.printStackTrace();
			FacesMessage message = new FacesMessage("Failed: " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public boolean isCreateConverter() {
		return createConverter;
	}

	public void setCreateConverter(boolean createConverter) {
		this.createConverter = createConverter;
	}

}
