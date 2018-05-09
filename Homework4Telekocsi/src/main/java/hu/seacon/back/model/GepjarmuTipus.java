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
public class GepjarmuTipus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@EntityFieldInfo(info = "#{msg['gepjarmu-tipus']}", weight = 1, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "markaMegnevezes", service="GepjarmuMarkaService", detailDialogFile = "/admin/gepjarmumarka-dialog")
	@ManyToOne
	@JoinColumn(name = "marka")
	private GepjarmuMarka marka;
	
	@EntityFieldInfo(info = "#{msg['tipus-megnevezes']}", weight = 2, required = true, editor = "txt")
	private String tipusMegnevezes;

	@EntityFieldInfo(info = "#{msg['terhelhetoseg']}", weight = 3, required = true, editor = "integer")
	private Short terhelhetoseg;

	@EntityFieldInfo(info = "#{msg['urtartalom']}", weight = 4, required = true, editor = "integer")
	private Short urtartalom;

	@EntityFieldInfo(info = "#{msg['utasok-szama']}", weight = 5, required = true, editor = "integer")
	private Byte utasok;
	
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GepjarmuMarka getMarka() {
		return marka;
	}

	public void setMarka(GepjarmuMarka marka) {
		this.marka = marka;
	}

	public String getTipusMegnevezes() {
		return tipusMegnevezes;
	}

	public void setTipusMegnevezes(String tipusMegnevezes) {
		this.tipusMegnevezes = tipusMegnevezes;
	}

	public Short getTerhelhetoseg() {
		return terhelhetoseg;
	}

	public void setTerhelhetoseg(Short terhelhetoseg) {
		this.terhelhetoseg = terhelhetoseg;
	}

	public Short getUrtartalom() {
		return urtartalom;
	}

	public void setUrtartalom(Short urtartalom) {
		this.urtartalom = urtartalom;
	}

	public Byte getUtasok() {
		return utasok;
	}

	public void setUtasok(Byte utasok) {
		this.utasok = utasok;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((marka == null) ? 0 : marka.hashCode());
		result = prime * result + ((terhelhetoseg == null) ? 0 : terhelhetoseg.hashCode());
		result = prime * result + ((tipusMegnevezes == null) ? 0 : tipusMegnevezes.hashCode());
		result = prime * result + ((urtartalom == null) ? 0 : urtartalom.hashCode());
		result = prime * result + ((utasok == null) ? 0 : utasok.hashCode());
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
		GepjarmuTipus other = (GepjarmuTipus) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (marka == null) {
			if (other.marka != null)
				return false;
		} else if (!marka.equals(other.marka))
			return false;
		if (terhelhetoseg == null) {
			if (other.terhelhetoseg != null)
				return false;
		} else if (!terhelhetoseg.equals(other.terhelhetoseg))
			return false;
		if (tipusMegnevezes == null) {
			if (other.tipusMegnevezes != null)
				return false;
		} else if (!tipusMegnevezes.equals(other.tipusMegnevezes))
			return false;
		if (urtartalom == null) {
			if (other.urtartalom != null)
				return false;
		} else if (!urtartalom.equals(other.urtartalom))
			return false;
		if (utasok == null) {
			if (other.utasok != null)
				return false;
		} else if (!utasok.equals(other.utasok))
			return false;
		return true;
	}
}
