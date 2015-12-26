package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class OffsetDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final OffsetDateTimeKeyDeserializer INSTANCE = new OffsetDateTimeKeyDeserializer();

    private OffsetDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected OffsetDateTime deserialize(String key, DeserializationContext ctxt) {
        return OffsetDateTime.parse(key, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
