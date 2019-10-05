package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalQuery;

public class InstantKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final InstantKeyDeserializer INSTANCE = new InstantKeyDeserializer();

    private InstantKeyDeserializer() {
        // singleton
    }

    @Override
    protected Instant deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return DateTimeFormatter.ISO_INSTANT.parse(key, new TemporalQuery<Instant>() {
                @Override
                public Instant queryFrom(TemporalAccessor temporal) {
                    return Instant.from(temporal);
                }
            });
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, Instant.class, e, key);
        }
    }
}
