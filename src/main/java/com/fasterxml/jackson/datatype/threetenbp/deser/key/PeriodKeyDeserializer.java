package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Period;

import com.fasterxml.jackson.databind.DeserializationContext;

public class PeriodKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final PeriodKeyDeserializer INSTANCE = new PeriodKeyDeserializer();

    private PeriodKeyDeserializer() {
        // singletin
    }

    @Override
    protected Period deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return Period.parse(key);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, Period.class, e, key);
        }
    }
}
