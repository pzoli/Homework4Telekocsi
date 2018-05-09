package hu.seacon.front.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import hu.exprog.beecomposit.back.resources.AuthBackingBean;

@Named
@RequestScoped
public class JaratIgenyController implements Serializable {

	private static final long serialVersionUID = 8950502005921460042L;
	
	@Inject
	private AuthBackingBean authBackingBean;

	public boolean getIgenybejeletoDisable() {
		return !authBackingBean.checkAdminRights();
	}
	
	public boolean getIgenybeVevoDisable() {
		return !authBackingBean.checkAdminRights();
	}

}
