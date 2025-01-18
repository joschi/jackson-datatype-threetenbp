package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class ZonedDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final ZonedDateTimeKeyDeserializer INSTANCE = new ZonedDateTimeKeyDeserializer();

    private ZonedDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected ZonedDateTime deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            // Not supplying a formatter allows the use of all supported formats
            return ZonedDateTime.parse(key);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, ZonedDateTime.class, e, key);
        }
    }
}
