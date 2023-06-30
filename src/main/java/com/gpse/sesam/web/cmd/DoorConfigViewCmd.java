package com.gpse.sesam.web.cmd;

import java.util.ArrayList;
import java.util.List;

public class DoorConfigViewCmd {
	private String description;
	private List<ConfigPartsViewCmd> configParts = new ArrayList<>();

	public List<ConfigPartsViewCmd> getConfigParts() {
		return configParts;
	}

	public void setConfigParts(final List<ConfigPartsViewCmd> configParts) {
		this.configParts = configParts;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}


	public void addConfigPart(final ConfigPartsViewCmd configPartsCmd) {
		configParts.add(configPartsCmd);
	}
}
