package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class DoorConfigCmd {
	private String description;

	private String doorId;
	private List<ConfigPartsCmd> configParts = new ArrayList<>();

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

	public void addConfigPart(final ConfigPartsCmd configPartsCmd) {
		configParts.add(configPartsCmd);
	}
}
