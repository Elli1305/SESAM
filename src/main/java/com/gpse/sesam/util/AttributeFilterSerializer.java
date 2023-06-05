package com.gpse.sesam.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;

public class AttributeFilterSerializer extends StdSerializer<AttributeFilter> {
	private static final long serialVersionUID = 1234567L;

	public AttributeFilterSerializer() {
		this(null);
	}

	protected AttributeFilterSerializer(final Class<AttributeFilter> t) {
		super(t);
	}

	@Override
	public void serialize(final AttributeFilter attributeFilter, final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();
		if (Strings.isNotBlank(attributeFilter.getCredentialDefinitionId())) {
			jsonGenerator.writeStringField("credentialDefinitionId", attributeFilter.getCredentialDefinitionId());
		}
		if (attributeFilter.getAttributeValue() != null) {
			jsonGenerator.writeFieldName("attributeValue");
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("name", attributeFilter.getAttributeValue().getName());
			jsonGenerator.writeStringField("value", attributeFilter.getAttributeValue().getValue());
			jsonGenerator.writeEndObject();
		}
		jsonGenerator.writeEndObject();
	}
}
