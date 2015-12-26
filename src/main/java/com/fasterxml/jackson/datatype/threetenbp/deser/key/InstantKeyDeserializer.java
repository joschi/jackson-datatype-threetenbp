package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.Instant;
import org.threeten.bp.format.DateTimeFormatter;

public class InstantKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final InstantKeyDeserializer INSTANCE = new InstantKeyDeserializer();

    private InstantKeyDeserializer() {
        // singleton
    }

    @Override
    protected Instant deserialize(String key, DeserializationContext ctxt) {
        return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(key));
    }

}
