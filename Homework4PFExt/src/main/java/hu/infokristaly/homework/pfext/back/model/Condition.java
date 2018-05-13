package hu.infokristaly.homework.pfext.back.model;

import java.io.Serializable;

public class Condition implements Serializable {
	private static final long serialVersionUID = -310176779538151366L;

	private String name;
	private String mailAddress;
	private int index;

	public Condition() {
	}

	public Condition(String name, String mailAddress, int index) {
		this.name = name;
		this.mailAddress = mailAddress;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String inputValue) {
		this.mailAddress = inputValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Condition condition = (Condition) o;

		if (index != condition.index) {
			return false;
		}

		if (mailAddress != null ? !mailAddress.equals(condition.mailAddress) : condition.mailAddress != null) {
			return false;
		}

		if (name != null ? !name.equals(condition.name) : condition.name != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = (name != null ? name.hashCode() : 0);
		result = 31 * result + (mailAddress != null ? mailAddress.hashCode() : 0);
		result = 31 * result + index;
		return result;
	}

	@Override
	public String toString() {
		return "Condition {" + "name='" + name + '\'' + "mailAddress='" + mailAddress + '\'' + '}';
	}
}
