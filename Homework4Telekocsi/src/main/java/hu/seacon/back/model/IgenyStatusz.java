package hu.seacon.back.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;

@Entity
public class IgenyStatusz {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Basic
	@EntityFieldInfo(info="#{msg['igenykod']}",weight=1,required=true,editor="txt")
	private String igenyKod;
	
	@Basic
	@EntityFieldInfo(info="#{msg['igenymegnevezes']}",weight=2,required=true,editor="txt")
	private String igenyMegnevezes;
	
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

	public String getIgenyKod() {
		return igenyKod;
	}

	public void setIgenyKod(String igenyKod) {
		this.igenyKod = igenyKod;
	}

	public String getIgenyMegnevezes() {
		return igenyMegnevezes;
	}

	public void setIgenyMegnevezes(String igenyMegnevezes) {
		this.igenyMegnevezes = igenyMegnevezes;
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
		result = prime * result + ((igenyKod == null) ? 0 : igenyKod.hashCode());
		result = prime * result + ((igenyMegnevezes == null) ? 0 : igenyMegnevezes.hashCode());
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
		IgenyStatusz other = (IgenyStatusz) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (igenyKod == null) {
			if (other.igenyKod != null)
				return false;
		} else if (!igenyKod.equals(other.igenyKod))
			return false;
		if (igenyMegnevezes == null) {
			if (other.igenyMegnevezes != null)
				return false;
		} else if (!igenyMegnevezes.equals(other.igenyMegnevezes))
			return false;
		return true;
	}
	
}
