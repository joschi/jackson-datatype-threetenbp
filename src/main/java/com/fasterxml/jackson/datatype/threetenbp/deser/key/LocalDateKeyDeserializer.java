package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class LocalDateKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalDateKeyDeserializer INSTANCE = new LocalDateKeyDeserializer();

    private LocalDateKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalDate deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return LocalDate.parse(key, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, LocalDate.class, e, key);
        }
    }
}
