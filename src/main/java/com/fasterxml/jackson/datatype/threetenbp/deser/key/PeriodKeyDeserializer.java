package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.Period;

public class PeriodKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final PeriodKeyDeserializer INSTANCE = new PeriodKeyDeserializer();

    private PeriodKeyDeserializer() {
        // singleton
    }

    @Override
    protected Period deserialize(String key, DeserializationContext ctxt) {
        return Period.parse(key);
    }

}
