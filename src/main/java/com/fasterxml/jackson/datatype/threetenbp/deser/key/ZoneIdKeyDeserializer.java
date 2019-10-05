package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.ZoneId;

import com.fasterxml.jackson.databind.DeserializationContext;

public class ZoneIdKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final ZoneIdKeyDeserializer INSTANCE = new ZoneIdKeyDeserializer();

    private ZoneIdKeyDeserializer() {
        // singleton
    }

    @Override
    protected Object deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return ZoneId.of(key);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, ZoneId.class, e, key);
        }
    }
}
