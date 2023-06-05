package com.gpse.sesam.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringToIntSerializer extends JsonSerializer<String> {

	private static final long serialVersionUID = 1234567L;

	private final Set<String> magicKeys = new HashSet<>(Arrays.asList("$T-MEMBER", "$T-TRAINING", "$U-MEMBER", "$U"
			+ "-TRAINING", "$TODAY-YYYYMMDD"));

	@Override
	public void serialize(final String value,
						  final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider)
			throws IOException {
		if (magicKeys.contains(value)) {
			jsonGenerator.writeString(value);
		} else {
			jsonGenerator.writeNumber(Integer.parseInt(value));
		}
	}
}