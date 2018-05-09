package hu.seacon.back.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;

@Entity
public class JaratIdo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	@EntityFieldInfo(info="#{msg['idopont']}", weight=1, required=true, editor="txt")
	private String idopont;

	@Basic
	@EntityFieldInfo(info="#{msg['megjegyzes']}", weight=3, required=false, editor="txt")
	private String description;
	
	@Version
	private Long version;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdopont() {
		return idopont;
	}

	public void setIdopont(String idopont) {
		this.idopont = idopont;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idopont == null) ? 0 : idopont.hashCode());
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
		JaratIdo other = (JaratIdo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idopont == null) {
			if (other.idopont != null)
				return false;
		} else if (!idopont.equals(other.idopont))
			return false;
		return true;
	}

	
}
