package org.cbir.retrieval.domain.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

/**
 * Custom Jackson deserializer for displaying Joda DateTime objects.
 */
public class CustomDateTimeDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return ISODateTimeFormat.dateTimeParser().parseDateTime(str);
        }
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new DateTime(jp.getLongValue());
        }
        throw ctxt.mappingException(handledType());
    }
}
