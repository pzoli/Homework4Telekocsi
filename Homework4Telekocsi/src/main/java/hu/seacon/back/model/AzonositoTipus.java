package hu.seacon.back.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;

@Entity
public class AzonositoTipus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@EntityFieldInfo(info = "#{msg['azonositoTipusNev']}", weight = 1, required = true, editor = "txt")
	private String azonositoTipusNev;
	@EntityFieldInfo(info = "#{msg['ervenyessegiIdoValtozhat']}", weight = 2, required = true, editor = "booleancheckbox")
	private Boolean ervenyessegiIdoValtozhat;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAzonositoTipusNev() {
		return azonositoTipusNev;
	}

	public void setAzonositoTipusNev(String azonositoTipusNev) {
		this.azonositoTipusNev = azonositoTipusNev;
	}

	public Boolean getErvenyessegiIdoValtozhat() {
		return ervenyessegiIdoValtozhat;
	}

	public void setErvenyessegiIdoValtozhat(Boolean ervenyessegiIdoValtozhat) {
		this.ervenyessegiIdoValtozhat = ervenyessegiIdoValtozhat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((azonositoTipusNev == null) ? 0 : azonositoTipusNev.hashCode());
		result = prime * result + ((ervenyessegiIdoValtozhat == null) ? 0 : ervenyessegiIdoValtozhat.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AzonositoTipus other = (AzonositoTipus) obj;
		if (azonositoTipusNev == null) {
			if (other.azonositoTipusNev != null)
				return false;
		} else if (!azonositoTipusNev.equals(other.azonositoTipusNev))
			return false;
		if (ervenyessegiIdoValtozhat == null) {
			if (other.ervenyessegiIdoValtozhat != null)
				return false;
		} else if (!ervenyessegiIdoValtozhat.equals(other.ervenyessegiIdoValtozhat))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
