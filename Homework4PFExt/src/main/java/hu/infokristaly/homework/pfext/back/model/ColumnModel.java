package hu.infokristaly.homework.pfext.back.model;

import java.io.Serializable;

public class ColumnModel implements Serializable {

	private static final long serialVersionUID = -5754120725610121073L;

	private String header;
	private String property;

	public ColumnModel(String header, String property) {
		this.header = header;
		this.property = property;
	}

	public String getHeader() {
		return header;
	}

	public String getProperty() {
		return property;
	}
}