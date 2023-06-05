package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.location.door.config.Predicate;

public class AttributeFilterCmd {
	private boolean currentDate;
	private FormEntry attribute;

	private String value;

	private Predicate predicateType;

	public boolean isCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(final boolean currentDate) {
		this.currentDate = currentDate;
	}

	public FormEntry getAttribute() {
		return attribute;
	}

	public void setAttribute(final FormEntry attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public Predicate getPredicateType() {
		return predicateType;
	}

	public void setPredicateType(final Predicate predicateType) {
		this.predicateType = predicateType;
	}
}
