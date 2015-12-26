package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.ZoneOffset;

public class ZoneOffsetKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final ZoneOffsetKeyDeserializer INSTANCE = new ZoneOffsetKeyDeserializer();

    private ZoneOffsetKeyDeserializer() {
        // singleton
    }

    @Override
    protected ZoneOffset deserialize(String key, DeserializationContext ctxt) {
        return ZoneOffset.of(key);
    }

}
