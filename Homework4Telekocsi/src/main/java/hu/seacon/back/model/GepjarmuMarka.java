package hu.seacon.back.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import hu.exprog.honeyweb.front.annotations.EntityFieldInfo;

@Entity
public class GepjarmuMarka {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@EntityFieldInfo(info="#{msg['gepjarmumarka-megnevezes']}",weight=1,required=true,editor="txt")
	private String markaMegnevezes;

	@Version
	private Long version;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMarkaMegnevezes() {
		return markaMegnevezes;
	}
	public void setMarkaMegnevezes(String megnevezes) {
		this.markaMegnevezes = megnevezes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((markaMegnevezes == null) ? 0 : markaMegnevezes.hashCode());
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
		GepjarmuMarka other = (GepjarmuMarka) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (markaMegnevezes == null) {
			if (other.markaMegnevezes != null)
				return false;
		} else if (!markaMegnevezes.equals(other.markaMegnevezes))
			return false;
		return true;
	}
}
