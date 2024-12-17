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
        // not serializing timezone data yet
        try {
            return ZonedDateTime.parse(key, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, ZonedDateTime.class, e, key);
        }
    }
}
