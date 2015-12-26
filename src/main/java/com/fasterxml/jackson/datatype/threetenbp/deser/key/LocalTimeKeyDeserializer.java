package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

public class LocalTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final LocalTimeKeyDeserializer INSTANCE = new LocalTimeKeyDeserializer();

    private LocalTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected LocalTime deserialize(String key, DeserializationContext ctxt) {
        return LocalTime.parse(key, DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
