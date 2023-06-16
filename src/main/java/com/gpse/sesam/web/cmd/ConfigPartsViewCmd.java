package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.InternalCredential;

import java.util.List;

public class ConfigPartsViewCmd {
	private List<Credential> credentials;
	private List<AttributeFilterCmd> attributeFilter;

	public List<AttributeFilterCmd> getAttributeFilter() {
		return attributeFilter;
	}

	public void setAttributeFilter(final List<AttributeFilterCmd> attributeFilter) {
		this.attributeFilter = attributeFilter;
	}

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(final List<Credential> credentials) {
		this.credentials = credentials;
	}
}
