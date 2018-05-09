package hu.seacon.back.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;
import hu.exprog.honeyweb.front.annotations.LookupFieldInfo;

@Entity
public class Gepjarmu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EntityFieldInfo(info = "#{msg['belsoazonosito']}", weight = 1, required = true, editor = "txt")
	private String belsoAzonosito;

	@EntityFieldInfo(info = "#{msg['gepjarmu-tipus']}", weight = 2, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "tipusMegnevezes", service="GepjarmuTipusService", detailDialogFile = "/admin/gepjarmutipus-dialog")
	@ManyToOne
	@JoinColumn(name = "tipus")
	private GepjarmuTipus tipus;
	
	@EntityFieldInfo(info = "#{msg['atlagfogyasztas']}", weight = 3, required = true, editor = "float")
	private Float atlagfogyasztas;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBelsoAzonosito() {
		return belsoAzonosito;
	}

	public void setBelsoAzonosito(String belsoAzonosito) {
		this.belsoAzonosito = belsoAzonosito;
	}

	public GepjarmuTipus getTipus() {
		return tipus;
	}

	public void setTipus(GepjarmuTipus tipus) {
		this.tipus = tipus;
	}
	
	public Float getAtlagfogyasztas() {
		return atlagfogyasztas;
	}

	public void setAtlagfogyasztas(Float atlagfogyasztas) {
		this.atlagfogyasztas = atlagfogyasztas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atlagfogyasztas == null) ? 0 : atlagfogyasztas.hashCode());
		result = prime * result + ((belsoAzonosito == null) ? 0 : belsoAzonosito.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tipus == null) ? 0 : tipus.hashCode());
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
		Gepjarmu other = (Gepjarmu) obj;
		if (atlagfogyasztas == null) {
			if (other.atlagfogyasztas != null)
				return false;
		} else if (!atlagfogyasztas.equals(other.atlagfogyasztas))
			return false;
		if (belsoAzonosito == null) {
			if (other.belsoAzonosito != null)
				return false;
		} else if (!belsoAzonosito.equals(other.belsoAzonosito))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipus == null) {
			if (other.tipus != null)
				return false;
		} else if (!tipus.equals(other.tipus))
			return false;
		return true;
	}

}
