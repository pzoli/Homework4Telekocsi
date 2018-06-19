package hu.infokristaly.homework.pfext.middle.utils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitResult;

import org.primefaces.PrimeFaces;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.util.visitcallback.VisitTaskExecutor;

public class FieldRightsInputExecutor implements VisitTaskExecutor {

	@Override
	public VisitResult execute(UIComponent component) {
		VisitResult result = VisitResult.ACCEPT;
		UIInput input = (UIInput) component;
		Integer rcIndex = (Integer) input.getAttributes().get("rcIndex");
		if (rcIndex != null && rcIndex.equals(1) && input instanceof InputText) {
			//((InputText)input).setDisabled(true);
			((InputText)input).setReadonly(true);
			PrimeFaces.current().ajax().update(input.getClientId());
		}
		return result;
	}

	@Override
	public boolean shouldExecute(UIComponent component) {
		boolean result = component instanceof UIInput;
		return result;
	}

}
