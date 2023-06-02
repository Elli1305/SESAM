package com.gpse.sesam.domain.imprint;

public interface ImprintService {
	void createImprintEntry(String content);

	String getLatestImprintEntry();

	void deleteImprintEntry();
}
