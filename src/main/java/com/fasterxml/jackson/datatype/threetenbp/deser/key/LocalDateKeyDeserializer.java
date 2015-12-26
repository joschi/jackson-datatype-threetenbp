package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalDateKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalDateKeyDeserializer INSTANCE = new LocalDateKeyDeserializer();

    private LocalDateKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalDate deserialize(String key, DeserializationContext ctxt) {
        return LocalDate.parse(key, DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
