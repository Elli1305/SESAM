package com.gpse.sesam.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
public final class FileStorageConfiguration {
	private String baseDir;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
}