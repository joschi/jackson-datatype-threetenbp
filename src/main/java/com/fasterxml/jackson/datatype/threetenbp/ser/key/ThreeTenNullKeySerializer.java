package com.fasterxml.jackson.datatype.threetenbp.ser.key;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

/**
 * This class is to be used in case {@code null} keys are needed to be serialized in a {@link Map} with ThreeTen temporal keys. By default the
 * {@code null} key is not supported by jackson, the serializer needs to be registered manually.
 *
 * @author Zoltan Kiss
 * @since 2.6
 */
public class ThreeTenNullKeySerializer extends JsonSerializer<Object> {

    public static final String NULL_KEY = "";

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            throw new JsonGenerationException("ThreeTenNullKeySerializer is only for serializing null values.");
        }
        gen.writeFieldName(NULL_KEY);
    }

}
