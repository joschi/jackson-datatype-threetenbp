package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class OffsetDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final OffsetDateTimeKeyDeserializer INSTANCE = new OffsetDateTimeKeyDeserializer();

    private OffsetDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected OffsetDateTime deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return OffsetDateTime.parse(key, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, OffsetDateTime.class, e, key);
        }
    }
}
