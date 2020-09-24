package hu.seacon.back.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import hu.exprog.beecomposit.back.model.SystemUser;
import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;
import hu.exprog.honeyweb.front.annotations.EntitySpecificRights;
import hu.exprog.honeyweb.front.annotations.FieldEntitySpecificRightsInfo;
import hu.exprog.honeyweb.front.annotations.FieldRightsInfo;
import hu.exprog.honeyweb.front.annotations.LookupFieldInfo;

@Entity
@EntitySpecificRights()
public class JaratIgeny {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EntityFieldInfo(info="#{msg['jarat']}", weight=1, required=true, editor="select")
	@LookupFieldInfo(keyField="id",labelField="jaratAzonosito",detailDialogFile="/admin/jarat-dialog", filterField="jarat.jaratAzonosito", sortField="jarat.jaratAzonosito")
	@FieldRightsInfo(admin="#{authBackingBean.checkAdminRights()}")
	@FieldEntitySpecificRightsInfo(disabled="#{jaratIgenyManager.checkStateOfCurrent()}")
	@ManyToOne
	@JoinColumn(name = "jarat")
	private Jarat jarat;

	@EntityFieldInfo(info="#{msg['igenybevevo']}", weight=2, required=true, editor="select")
	@LookupFieldInfo(keyField="id",labelField="userName",detailDialogFile="/admin/users-dialog", filterField="igenybeVevo.userName", sortField="igenybeVevo.userName")
	@FieldRightsInfo(disabled="#{jaratIgenyController.igenybeVevoDisable}", admin="#{authBackingBean.checkAdminRights()}")
	@FieldEntitySpecificRightsInfo(disabled="#{jaratIgenyManager.checkStateOfCurrent()}")
	@ManyToOne
	@JoinColumn(name = "igenybevevo")
	private SystemUser igenybeVevo;

	@EntityFieldInfo(info="#{msg['igenybejelento']}", weight=2, required=false, editor="select")
	@LookupFieldInfo(keyField="id",labelField="userName",detailDialogFile="/admin/users-dialog", filterField="igenyBejelento.userName", sortField="igenyBejelent.userName")
	@FieldRightsInfo(disabled="#{jaratIgenyController.igenybejeletoDisable}", admin="#{authBackingBean.checkAdminRights()}")
	@FieldEntitySpecificRightsInfo(disabled="#{jaratIgenyManager.checkStateOfCurrent()}")
	@ManyToOne
	@JoinColumn(name = "igenyBejelento")
	private SystemUser igenyBejelento;

	@EntityFieldInfo(info="#{msg['igenystatusz']}", weight=3, required=true, editor="select")
	@LookupFieldInfo(keyField="id",labelField="igenyMegnevezes",detailDialogFile="/admin/igenystatusz-dialog", filterField="igenyStatusz.igenyMegnevezes", sortField="igenyStatusz.igenyMegnevezes")
	@FieldRightsInfo(admin="#{authBackingBean.checkAdminRights()}",disabled="#{jaratIgenyController.igenybejeletoDisable}")
	@FieldEntitySpecificRightsInfo(disabled="#{jaratIgenyManager.checkStateOfCurrent()}")
	@ManyToOne
	@JoinColumn(name = "igenystatusz")
	private IgenyStatusz igenyStatusz;
	
	@FieldEntitySpecificRightsInfo(disabled="#{jaratIgenyManager.checkStateOfCurrent()}")
	@EntityFieldInfo(info="#{msg['igenydatuma']}", weight=4, required=true, editor="date", format="yyyy.MM.dd")
	private Date igenyDatuma;
	
	@Version
	private Long version;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Jarat getJarat() {
		return jarat;
	}

	public void setJarat(Jarat jarat) {
		this.jarat = jarat;
	}

	public SystemUser getIgenybeVevo() {
		return igenybeVevo;
	}

	public void setIgenybeVevo(SystemUser igenybeVevo) {
		this.igenybeVevo = igenybeVevo;
	}

	public IgenyStatusz getIgenyStatusz() {
		return igenyStatusz;
	}

	public void setIgenyStatusz(IgenyStatusz igenystatusz) {
		this.igenyStatusz = igenystatusz;
	}

	public Date getIgenyDatuma() {
		return igenyDatuma;
	}

	public void setIgenyDatuma(Date igenyDatuma) {
		this.igenyDatuma = igenyDatuma;
	}
	
	public String getIgenyloNeveIdopont() {
		return (jarat.getJaratAzonosito()) + " / " + (igenybeVevo != null ? igenybeVevo.getUserName() : "") + " - " + (igenyDatuma != null ? SimpleDateFormat.getDateInstance().format(igenyDatuma.getTime()) : "");
	}

	public SystemUser getIgenyBejelento() {
		return igenyBejelento;
	}

	public void setIgenyBejelento(SystemUser igenyBejelento) {
		this.igenyBejelento = igenyBejelento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((igenyDatuma == null) ? 0 : igenyDatuma.hashCode());
		result = prime * result + ((igenyStatusz == null) ? 0 : igenyStatusz.hashCode());
		result = prime * result + ((igenyBejelento == null) ? 0 : igenyBejelento.hashCode());
		result = prime * result + ((igenybeVevo == null) ? 0 : igenybeVevo.hashCode());
		result = prime * result + ((jarat == null) ? 0 : jarat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JaratIgeny other = (JaratIgeny) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (igenyDatuma == null) {
			if (other.igenyDatuma != null)
				return false;
		} else if (!igenyDatuma.equals(other.igenyDatuma))
			return false;
		if (igenyStatusz == null) {
			if (other.igenyStatusz != null)
				return false;
		} else if (!igenyStatusz.equals(other.igenyStatusz))
			return false;
		if (igenyBejelento == null) {
			if (other.igenyBejelento != null)
				return false;
		} else if (!igenyBejelento.equals(other.igenyBejelento))
			return false;
		if (igenybeVevo == null) {
			if (other.igenybeVevo != null)
				return false;
		} else if (!igenybeVevo.equals(other.igenybeVevo))
			return false;
		if (jarat == null) {
			if (other.jarat != null)
				return false;
		} else if (!jarat.equals(other.jarat))
			return false;
		return true;
	}

}
