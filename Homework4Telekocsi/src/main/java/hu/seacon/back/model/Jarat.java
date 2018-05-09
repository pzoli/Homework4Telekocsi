package hu.seacon.back.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;
import hu.exprog.honeyweb.front.annotations.EntityInfo;
import hu.exprog.honeyweb.front.annotations.LookupFieldInfo;

@Entity
@EntityInfo(info = "#{msg['jarat-table']}")
public class Jarat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EntityFieldInfo(info = "#{msg['gepjarmu']}", weight = 1, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "belsoAzonosito", detailDialogFile = "/admin/gepjarmu-dialog", filterField = "gepjarmu.belsoAzonosito", sortField = "gepjarmu.belsoAzonosito")
	@ManyToOne
	@JoinColumn(name = "gepjarmu")
	private Gepjarmu gepjarmu;

	@EntityFieldInfo(info = "#{msg['jaratido']}", weight = 1, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "idopont", detailDialogFile = "/admin/jaratido-dialog", filterField = "jaratIdoId.idopont", sortField = "jaratIdoId.idopont")
	@ManyToOne
	@JoinColumn(name = "jaratido")
	private JaratIdo jaratIdo;

	@EntityFieldInfo(info = "#{msg['jaratirany']}", weight = 2, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "honnanhova", detailDialogFile = "/admin/jaratirany-dialog", filterField = "jaratIranyId.honnan", sortField = "jaratIranyId.honnan")
	@ManyToOne
	@JoinColumn(name = "jaratirany")
	private JaratIrany jaratIrany;

	@EntityFieldInfo(info = "#{msg['munkanap']}", weight = 3, required = true, editor = "booleancheckbox")
	private Boolean munkanap;

	@EntityFieldInfo(info = "#{msg['jaratazonosito']}", weight = 4)
	private String jaratAzonosito;

	@EntityFieldInfo(info = "#{msg['tavolsag']}", weight = 5, required = false, editor = "integer")
	private Integer tavolsag;

	@EntityFieldInfo(info = "#{msg['ervenyesseg-kezdete']}", weight = 6, required = true, editor = "date", format = "yyyy.MM.dd")
	private Date ervenyessegKezdodatum;

	@EntityFieldInfo(info = "#{msg['ervenyesseg-vege']}", weight = 7, required = false, editor = "date", format = "yyyy.MM.dd")
	private Date ervenyessegZarodatum;
	
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JaratIdo getJaratIdo() {
		return jaratIdo;
	}

	public void setJaratIdo(JaratIdo jaratIdo) {
		this.jaratIdo = jaratIdo;
	}

	public JaratIrany getJaratIrany() {
		return jaratIrany;
	}

	public void setJaratIrany(JaratIrany jaratIrany) {
		this.jaratIrany = jaratIrany;
	}

	public Boolean getMunkanap() {
		return munkanap;
	}

	public void setMunkanap(Boolean munkanap) {
		this.munkanap = munkanap;
	}

	public String getJaratAzonosito() {
		return jaratAzonosito;
	}

	public void setJaratAzonosito(String jaratAzonosito) {
		this.jaratAzonosito = jaratAzonosito;
	}

	public Integer getTavolsag() {
		return tavolsag;
	}

	public void setTavolsag(Integer tavolsag) {
		this.tavolsag = tavolsag;
	}

	public Date getErvenyessegKezdodatum() {
		return ervenyessegKezdodatum;
	}

	public void setErvenyessegKezdodatum(Date ervenyessegKezdodatum) {
		this.ervenyessegKezdodatum = ervenyessegKezdodatum;
	}

	public Date getErvenyessegZarodatum() {
		return ervenyessegZarodatum;
	}

	public void setErvenyessegZarodatum(Date ervenyessegZarodatum) {
		this.ervenyessegZarodatum = ervenyessegZarodatum;
	}

	public Gepjarmu getGepjarmu() {
		return gepjarmu;
	}

	public void setGepjarmu(Gepjarmu gepjarmu) {
		this.gepjarmu = gepjarmu;
	}

	@PreUpdate
	public void preUpdate() {
		updateJaratAzonosito();
	}
	
	@PrePersist
	public void prePersist() {
		updateJaratAzonosito();
	}

	public void updateJaratAzonosito() {
		this.jaratAzonosito = gepjarmu.getBelsoAzonosito() + " / " + jaratIrany.getHonnanhova() + " / " + jaratIdo.getIdopont();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ervenyessegKezdodatum == null) ? 0 : ervenyessegKezdodatum.hashCode());
		result = prime * result + ((ervenyessegZarodatum == null) ? 0 : ervenyessegZarodatum.hashCode());
		result = prime * result + ((gepjarmu == null) ? 0 : gepjarmu.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jaratAzonosito == null) ? 0 : jaratAzonosito.hashCode());
		result = prime * result + ((jaratIdo == null) ? 0 : jaratIdo.hashCode());
		result = prime * result + ((jaratIrany == null) ? 0 : jaratIrany.hashCode());
		result = prime * result + ((munkanap == null) ? 0 : munkanap.hashCode());
		result = prime * result + ((tavolsag == null) ? 0 : tavolsag.hashCode());
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
		Jarat other = (Jarat) obj;
		if (ervenyessegKezdodatum == null) {
			if (other.ervenyessegKezdodatum != null)
				return false;
		} else if (!ervenyessegKezdodatum.equals(other.ervenyessegKezdodatum))
			return false;
		if (ervenyessegZarodatum == null) {
			if (other.ervenyessegZarodatum != null)
				return false;
		} else if (!ervenyessegZarodatum.equals(other.ervenyessegZarodatum))
			return false;
		if (gepjarmu == null) {
			if (other.gepjarmu != null)
				return false;
		} else if (!gepjarmu.equals(other.gepjarmu))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jaratAzonosito == null) {
			if (other.jaratAzonosito != null)
				return false;
		} else if (!jaratAzonosito.equals(other.jaratAzonosito))
			return false;
		if (jaratIdo == null) {
			if (other.jaratIdo != null)
				return false;
		} else if (!jaratIdo.equals(other.jaratIdo))
			return false;
		if (jaratIrany == null) {
			if (other.jaratIrany != null)
				return false;
		} else if (!jaratIrany.equals(other.jaratIrany))
			return false;
		if (munkanap == null) {
			if (other.munkanap != null)
				return false;
		} else if (!munkanap.equals(other.munkanap))
			return false;
		if (tavolsag == null) {
			if (other.tavolsag != null)
				return false;
		} else if (!tavolsag.equals(other.tavolsag))
			return false;
		return true;
	}

}
