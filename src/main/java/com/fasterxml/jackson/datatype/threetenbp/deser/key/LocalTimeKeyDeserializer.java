package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class LocalTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalTimeKeyDeserializer INSTANCE = new LocalTimeKeyDeserializer();

    private LocalTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalTime deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return LocalTime.parse(key, DateTimeFormatter.ISO_LOCAL_TIME);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, LocalTime.class, e, key);
        }
    }

}
