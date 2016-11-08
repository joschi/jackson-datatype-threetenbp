package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Duration;

import com.fasterxml.jackson.databind.DeserializationContext;

public class DurationKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final DurationKeyDeserializer INSTANCE = new DurationKeyDeserializer();

    private DurationKeyDeserializer() {
        // singleton
    }

    @Override
    protected Duration deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return Duration.parse(key);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, Duration.class, e, key);
        }
    }
}
