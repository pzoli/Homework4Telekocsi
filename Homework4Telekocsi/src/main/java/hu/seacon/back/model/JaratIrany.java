package hu.seacon.back.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;

@Entity
public class JaratIrany {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	@EntityFieldInfo(info="#{msg['honnan']}", weight=1, required=true, editor="txt")
	private String honnan;

	@Basic
	@EntityFieldInfo(info="#{msg['hova']}", weight=2, required=true, editor="txt")
	private String hova;

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

	public String getHonnan() {
		return honnan;
	}

	public void setHonnan(String honnan) {
		this.honnan = honnan;
	}

	public String getHova() {
		return hova;
	}

	public void setHova(String hova) {
		this.hova = hova;
	}
	
	public String getHonnanhova() {
		return honnan+" - "+hova;
	}

	public void setHonnanhova(String honnanhova) {
		
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
		result = prime * result + ((honnan == null) ? 0 : honnan.hashCode());
		result = prime * result + ((hova == null) ? 0 : hova.hashCode());
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
		JaratIrany other = (JaratIrany) obj;
		if (honnan == null) {
			if (other.honnan != null)
				return false;
		} else if (!honnan.equals(other.honnan))
			return false;
		if (hova == null) {
			if (other.hova != null)
				return false;
		} else if (!hova.equals(other.hova))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
