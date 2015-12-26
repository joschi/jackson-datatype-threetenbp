package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class ZonedDateTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final ZonedDateTimeKeyDeserializer INSTANCE = new ZonedDateTimeKeyDeserializer();

    private ZonedDateTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected ZonedDateTime deserialize(String key, DeserializationContext ctxt) {
        // not serializing timezone data yet
        return ZonedDateTime.parse(key, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
