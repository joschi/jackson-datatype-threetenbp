package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.OffsetTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;

public class OffsetTimeKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final OffsetTimeKeyDeserializer INSTANCE = new OffsetTimeKeyDeserializer();

    private OffsetTimeKeyDeserializer() {
        // singleton
    }

    @Override
    protected OffsetTime deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return OffsetTime.parse(key, DateTimeFormatter.ISO_OFFSET_TIME);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, OffsetTime.class, e, key);
        }
    }

}
