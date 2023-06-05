package com.gpse.sesam.web.cmd;

import java.util.List;

public class ConfigCmd {


	private String doorId;
	private String description;
	private List<ConfigPartsCmd> configParts;

	public List<ConfigPartsCmd> getConfigParts() {
		return configParts;
	}

	public void setConfigParts(final List<ConfigPartsCmd> configParts) {
		this.configParts = configParts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDoorId() {
		return doorId;
	}

	public void setDoorId(final String doorId) {
		this.doorId = doorId;
	}
}
