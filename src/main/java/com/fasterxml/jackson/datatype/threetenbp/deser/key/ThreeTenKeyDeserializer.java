package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import java.io.IOException;
import org.threeten.bp.DateTimeException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer;

abstract class ThreeTenKeyDeserializer extends KeyDeserializer {

    @Override
    public final Object deserializeKey(String key, DeserializationContext ctxt)
        throws IOException
    {
        if (ThreeTenNullKeySerializer.NULL_KEY.equals(key)) {
            // potential null key in HashMap
            return null;
        }
        return deserialize(key, ctxt);
    }

    protected abstract Object deserialize(String key, DeserializationContext ctxt)
        throws IOException;

    @SuppressWarnings("unchecked")
    protected <T> T _handleDateTimeException(DeserializationContext ctxt,
              Class<?> type, DateTimeException e0, String value) throws IOException
    {
        try {
            return (T) ctxt.handleWeirdKey(type, value,
                    "Failed to deserialize %s: (%s) %s",
                    type.getName(), e0.getClass().getName(), e0.getMessage());

        } catch (JsonMappingException e) {
            e.initCause(e0);
            throw e;
        } catch (IOException e) {
            if (null == e.getCause()) {
                e.initCause(e0);
            }
            throw JsonMappingException.fromUnexpectedIOE(e);
        }
    }
}
