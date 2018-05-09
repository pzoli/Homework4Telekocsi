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
public class JaratFoglaltsag {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@EntityFieldInfo(info="#{msg['igenyfogadas-datuma']}", weight=5, required=true, editor="date")
	private Date igenyfogadasDatuma;

	@EntityFieldInfo(info="#{msg['jaratigeny']}", weight=2, required=true, editor="select")
	@LookupFieldInfo(keyField="id",labelField="igenyloNeveIdopont",detailDialogFile="/admin/jaratigeny-dialog")
	@ManyToOne
	@JoinColumn(name = "jaratigeny")
	private JaratIgeny jaratIgeny;
	
	@Version
	private Long version;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getIgenyfogadasDatuma() {
		return igenyfogadasDatuma;
	}

	public void setIgenyfogadasDatuma(Date igenyfogadasDatuma) {
		this.igenyfogadasDatuma = igenyfogadasDatuma;
	}

	public JaratIgeny getJaratIgeny() {
		return jaratIgeny;
	}

	public void setJaratIgeny(JaratIgeny jaratIgeny) {
		this.jaratIgeny = jaratIgeny;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((igenyfogadasDatuma == null) ? 0 : igenyfogadasDatuma.hashCode());
		result = prime * result + ((jaratIgeny == null) ? 0 : jaratIgeny.hashCode());
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
		JaratFoglaltsag other = (JaratFoglaltsag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (igenyfogadasDatuma == null) {
			if (other.igenyfogadasDatuma != null)
				return false;
		} else if (!igenyfogadasDatuma.equals(other.igenyfogadasDatuma))
			return false;
		if (jaratIgeny == null) {
			if (other.jaratIgeny != null)
				return false;
		} else if (!jaratIgeny.equals(other.jaratIgeny))
			return false;
		return true;
	}
	
}
