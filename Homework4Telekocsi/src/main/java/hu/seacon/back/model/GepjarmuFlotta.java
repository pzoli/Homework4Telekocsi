package hu.seacon.back.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import hu.exprog.beecomposit.back.model.Company;
import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;
import hu.exprog.honeyweb.front.annotations.EntityInfo;
import hu.exprog.honeyweb.front.annotations.LookupFieldInfo;

@Entity
@EntityInfo(info="#{msg['gepjarmuFlotta']}")
public class GepjarmuFlotta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@EntityFieldInfo(info = "#{msg['gepjarmu']}", weight = 2, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "belsoAzonosito", service="GepjarmuService", detailDialogFile = "/admin/gepjarmu-dialog", filterField="gepjarmu.belsoAzonosito", sortField="gepjarmu.belsoAzonosito")
	@ManyToOne
	@JoinColumn(name = "gepjarmu")
	private Gepjarmu gepjarmu;
	@EntityFieldInfo(info = "#{msg['company-table']}", weight = 3, required = true, editor = "select")
	@LookupFieldInfo(keyField = "id", labelField = "companyName", service="CompanyService", detailDialogFile = "/admin/company-dialog", filterField="company.companyName", sortField="company.companyName")
	@ManyToOne
	@JoinColumn(name = "company")
	private Company company;
	@EntityFieldInfo(info = "#{msg['ervenyessegKezdete']}", weight = 5, required = true, editor = "date")
	private Date ervenyessegKezdete;
	@EntityFieldInfo(info = "#{msg['ervenyessegVege']}", weight = 6, required = false, editor = "date")
	private Date ervenyessegVege;
	@Version
	private Long version;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Gepjarmu getGepjarmu() {
		return gepjarmu;
	}
	public void setGepjarmu(Gepjarmu gepjarmu) {
		this.gepjarmu = gepjarmu;
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
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
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
		GepjarmuFlotta other = (GepjarmuFlotta) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
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
