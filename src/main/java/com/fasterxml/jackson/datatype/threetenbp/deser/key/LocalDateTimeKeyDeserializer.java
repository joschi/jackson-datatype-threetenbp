package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalDateTimeKeyDeserializer INSTANCE = new LocalDateTimeKeyDeserializer();

    private LocalDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalDateTime deserialize(String key, DeserializationContext ctxt) {
        return LocalDateTime.parse(key, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
