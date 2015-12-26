package com.fasterxml.jackson.datatype.threetenbp;

import org.threeten.bp.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestNullKeySerialization {

    private static final TypeReference<Map<LocalDate, String>> TYPE_REF = new TypeReference<Map<LocalDate, String>>() {
    };

    private ObjectMapper om;
    private Map<LocalDate, String> map;

    @Before
    public void setUp() {
        this.om = new ObjectMapper();
        om.registerModule(new ThreeTenModule());
        map = new HashMap<LocalDate, String>();
    }

    @Test
    public void testSerialization() throws Exception {
        om.getSerializerProvider().setNullKeySerializer(new ThreeTenNullKeySerializer());

        map.put(null, "test");
        String value = om.writeValueAsString(map);

        Assert.assertEquals(map(ThreeTenNullKeySerializer.NULL_KEY, "test"), value);
    }

    @Test
    public void testDeserialization() throws Exception {
        Map<LocalDate, String> value = om.readValue(map(ThreeTenNullKeySerializer.NULL_KEY, "test"), TYPE_REF);

        map.put(null, "test");
        Assert.assertEquals(map, value);
    }

    private String map(String key, String value) {
        return String.format("{\"%s\":\"%s\"}", key, value);
    }
}
