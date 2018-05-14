/*
 * Copyright 2013 Integrity Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Zoltan Papp
 * 
 */
package hu.infokristaly.homework.pfext.front.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.extensions.component.dynaform.DynaForm;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.extensions.util.visitcallback.ExecutableVisitCallback;

import hu.infokristaly.homework.pfext.back.model.ColumnModel;
import hu.infokristaly.homework.pfext.back.model.Condition;
import hu.infokristaly.homework.pfext.middle.utils.FieldRightsInputExecutor;

/**
 * The WorkflowBean class.
 * 
 * @author pzoli
 */
@Named
@ManagedBean
@SessionScoped
public class WorkflowBean implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2073307087048785459L;

	private DynaFormModel model = new DynaFormModel();

	private List<Condition> conditions;

	private List<ColumnModel> columns;
	
	private String columnTemplate = "fileName sizeForHumanReader";
	private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("fileName", "sizeForHumanReader");

	@PostConstruct
	public void init() {

		model = new DynaFormModel();
		conditions = new ArrayList<Condition>();

		Condition condition = new Condition("Name0", "a0@b.hu", 0);
		conditions.add(condition);
		
		DynaFormRow row = model.createRegularRow();
		DynaFormLabel label = row.addLabel(condition.getName());
		DynaFormControl control = row.addControl(condition, "value");
		control.setKey(condition.getName());
		label.setForControl(control);
		row.addControl(condition, "clear");
	}

	public static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);
	
	public void setFormRights() {
		FacesContext fc = FacesContext.getCurrentInstance();
		FieldRightsInputExecutor visitTaskExecutor = new FieldRightsInputExecutor();
		ExecutableVisitCallback visitCallback = new ExecutableVisitCallback(visitTaskExecutor);
		DynaForm dynaForm = (DynaForm) fc.getViewRoot().findComponent(":senderForm:peDynaForm");
		if (dynaForm != null) {
			dynaForm.visitTree(VisitContext.createVisitContext(fc, null, VISIT_HINTS), visitCallback);
		}
	}

	public String process() {
	    return "";
	}

	public DynaFormModel getModel() {
		return model;
	}

	public void removeCondition(Condition condition) {
		if (conditions.size() > 1) {
			model.removeRegularRow(condition.getIndex());
			conditions.remove(condition);

			// re-index conditions
			int idx = 0;
			for (Condition cond : conditions) {
				cond.setIndex(idx);
				idx++;
			}
		}
	}

	public void addCondition() {
		Condition condition = new Condition("Name"+conditions.size(), "a"+ conditions.size() +"@b.hu", conditions.size());
		conditions.add(condition);
		DynaFormRow row = model.createRegularRow();
		DynaFormLabel label = row.addLabel(condition.getName());
		DynaFormControl control =  row.addControl(condition, "value");
		control.setKey(condition.getName());
		label.setForControl(control);
		row.addControl(condition, "clear");
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

}
