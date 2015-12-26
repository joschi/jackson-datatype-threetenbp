package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.OffsetTime;
import org.threeten.bp.format.DateTimeFormatter;

public class OffsetTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final OffsetTimeKeyDeserializer INSTANCE = new OffsetTimeKeyDeserializer();

    private OffsetTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected OffsetTime deserialize(String key, DeserializationContext ctxt) {
        return OffsetTime.parse(key, DateTimeFormatter.ISO_OFFSET_TIME);
    }

}
