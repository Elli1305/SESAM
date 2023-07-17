package com.gpse.sesam.domain.imprint;

public interface ImprintService {
	void createImprintEntry(final String content);

	String getLatestImprintEntry();

	void deleteImprintEntry();
}
