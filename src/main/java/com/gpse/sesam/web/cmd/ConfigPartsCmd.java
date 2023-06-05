package com.gpse.sesam.web.cmd;

import java.util.List;

public class ConfigPartsCmd {
	private List<String> credentials;
	private List<AttributeFilterCmd> attributeFilter;

	public List<String> getCredentials() {
		return credentials;
	}

	public void setCredentials(final List<String> credentialDefinitionIds) {
		credentials = credentialDefinitionIds;
	}

	public List<AttributeFilterCmd> getAttributeFilter() {
		return attributeFilter;
	}

	public void setAttributeFilter(final List<AttributeFilterCmd> attributeFilter) {
		this.attributeFilter = attributeFilter;
	}
}
