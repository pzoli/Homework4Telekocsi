package hu.exprog.honeyweb.front.controller;

import java.util.Map;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.extensions.util.visitcallback.VisitTaskExecutor;

import hu.exprog.honeyweb.front.annotations.FieldEntitySpecificRightsInfo;
import hu.exprog.honeyweb.utils.FieldModel;

public class FieldRightsInputExecutor implements VisitTaskExecutor {

	private Map<String, FieldModel> fieldModelMap;

	public FieldRightsInputExecutor(Map<String, FieldModel> fieldModelMap) {
		this.fieldModelMap = fieldModelMap;
	}

	@Override
	public VisitResult execute(UIComponent component) {
		VisitResult result = VisitResult.ACCEPT;
		UIInput input = (UIInput) component;
		String propertyName = (String) input.getAttributes().get("propertyName");
		FieldModel fieldModel = fieldModelMap.get(propertyName);
		FieldEntitySpecificRightsInfo rights = fieldModel.getFieldEntitySpecificRightsInfo();
		((SelectOneMenu) input).setDisabled(evalELToBoolean(rights.disabled()));
		return result;
	}

	private Boolean evalELToBoolean(String expression) {
		Boolean result = null;
		if (expression != null && !expression.isEmpty()) {
			FacesContext context = FacesContext.getCurrentInstance();
			try {
				result = context.getApplication().evaluateExpressionGet(context, expression, Boolean.class);
			} catch (ELException ex) {

			}
		}
		return result;
	}

	@Override
	public boolean shouldExecute(UIComponent component) {
		boolean result = false;
		if (component instanceof UIInput) {
			UIInput input = (UIInput) component;
			String propertyName = (String) input.getAttributes().get("propertyName");
			if (propertyName != null) {
				FieldModel fieldModel = fieldModelMap.get(propertyName);
				if (fieldModel != null) {
					FieldEntitySpecificRightsInfo rights = fieldModel.getFieldEntitySpecificRightsInfo();
					if (rights != null && rights.disabled() != null && !rights.disabled().isEmpty()) {
						if (input instanceof SelectOneMenu) {
							result = true;
						}
					}
				}
			}
		}
		return result;
	}

}
