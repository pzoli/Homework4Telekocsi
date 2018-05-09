package hu.seacon.back.model;

import java.util.Date;

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
public class GepjarmuAzonosito {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@EntityFieldInfo(info = "#{msg['azonositoTipus']}", weight = 1, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "azonositoTipusNev", service="AzonositoTipusService", detailDialogFile = "/admin/azonositotipus-dialog")
	@ManyToOne
	@JoinColumn(name = "azonositotipus")
	private AzonositoTipus azonositoTipus;
	@EntityFieldInfo(info = "#{msg['gepjarmu']}", weight = 2, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "belsoAzonosito", service="GepjarmuService", detailDialogFile = "/admin/gepjarmu-dialog")
	@ManyToOne
	@JoinColumn(name = "gepjarmu")
	private Gepjarmu gepjarmu;
	@EntityFieldInfo(info = "#{msg['azonositoertek']}", weight = 4, required = true, editor = "txt")
	private String ertek;
	@EntityFieldInfo(info = "#{msg['ervenyessegKezdete']}", weight = 5, required = true, editor = "date")
	private Date ervenyessegKezdete;
	@EntityFieldInfo(info = "#{msg['ervenyessegVege']}", weight = 6, required = true, editor = "date")
	private Date ervenyessegVege;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AzonositoTipus getAzonositoTipus() {
		return azonositoTipus;
	}
	public void setAzonositoTipus(AzonositoTipus azonositoTipus) {
		this.azonositoTipus = azonositoTipus;
	}
	public Gepjarmu getGepjarmu() {
		return gepjarmu;
	}
	public void setGepjarmu(Gepjarmu gepjarmu) {
		this.gepjarmu = gepjarmu;
	}
	public String getErtek() {
		return ertek;
	}
	public void setErtek(String ertek) {
		this.ertek = ertek;
	}
	public Date getErvenyessegKezdete() {
		return ervenyessegKezdete;
	}
	public void setErvenyessegKezdete(Date ervenyessegKezdete) {
		this.ervenyessegKezdete = ervenyessegKezdete;
	}
	public Date getErvenyessegVege() {
		return ervenyessegVege;
	}
	public void setErvenyessegVege(Date ervenyessegVege) {
		this.ervenyessegVege = ervenyessegVege;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((azonositoTipus == null) ? 0 : azonositoTipus.hashCode());
		result = prime * result + ((ertek == null) ? 0 : ertek.hashCode());
		result = prime * result + ((ervenyessegKezdete == null) ? 0 : ervenyessegKezdete.hashCode());
		result = prime * result + ((ervenyessegVege == null) ? 0 : ervenyessegVege.hashCode());
		result = prime * result + ((gepjarmu == null) ? 0 : gepjarmu.hashCode());
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
		GepjarmuAzonosito other = (GepjarmuAzonosito) obj;
		if (azonositoTipus == null) {
			if (other.azonositoTipus != null)
				return false;
		} else if (!azonositoTipus.equals(other.azonositoTipus))
			return false;
		if (ertek == null) {
			if (other.ertek != null)
				return false;
		} else if (!ertek.equals(other.ertek))
			return false;
		if (ervenyessegKezdete == null) {
			if (other.ervenyessegKezdete != null)
				return false;
		} else if (!ervenyessegKezdete.equals(other.ervenyessegKezdete))
			return false;
		if (ervenyessegVege == null) {
			if (other.ervenyessegVege != null)
				return false;
		} else if (!ervenyessegVege.equals(other.ervenyessegVege))
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
		return true;
	}

}
