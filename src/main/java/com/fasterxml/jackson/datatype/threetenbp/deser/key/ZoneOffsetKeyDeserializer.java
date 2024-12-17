package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.ZoneOffset;

import com.fasterxml.jackson.databind.DeserializationContext;

public class ZoneOffsetKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final ZoneOffsetKeyDeserializer INSTANCE = new ZoneOffsetKeyDeserializer();

    private ZoneOffsetKeyDeserializer() {
        // singleton
    }

    @Override
    protected ZoneOffset deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return ZoneOffset.of(key);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, ZoneOffset.class, e, key);
        }
    }
}
