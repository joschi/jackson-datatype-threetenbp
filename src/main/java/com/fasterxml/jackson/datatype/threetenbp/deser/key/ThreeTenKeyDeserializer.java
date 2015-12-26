package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer;

abstract class ThreeTenKeyDeserializer extends KeyDeserializer {

    @Override
    public final Object deserializeKey(String key, DeserializationContext ctxt) {
        if (ThreeTenNullKeySerializer.NULL_KEY.equals(key)) {
            // potential null key in HashMap
            return null;
        }
        return deserialize(key, ctxt);
    }

    protected abstract Object deserialize(String key, DeserializationContext ctxt);
}
