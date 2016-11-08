package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class LocalDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalDateTimeKeyDeserializer INSTANCE = new LocalDateTimeKeyDeserializer();

    private LocalDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalDateTime deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return LocalDateTime.parse(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, LocalDateTime.class, e, key);
        }
    }

}
