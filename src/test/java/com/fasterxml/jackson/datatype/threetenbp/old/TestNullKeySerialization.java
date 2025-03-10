package com.fasterxml.jackson.datatype.threetenbp.old;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import  org.threeten.bp.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestNullKeySerialization extends ModuleTestBase {
    @SuppressWarnings("deprecation")
    final String NULL_KEY = com.fasterxml.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer.NULL_KEY;

    private static final TypeReference<Map<LocalDate, String>> TYPE_REF = new TypeReference<Map<LocalDate, String>>() {
    };

    private ObjectMapper om;
    private Map<LocalDate, String> map;

    @Before
    public void setUp() {
        this.om = newMapper();
        map = new HashMap<>();
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSerialization() throws Exception {
        om.getSerializerProvider().setNullKeySerializer(new com.fasterxml.jackson.datatype.threetenbp.ser.key.ThreeTenNullKeySerializer());

        map.put(null, "test");
        String value = om.writeValueAsString(map);

        Assert.assertEquals(map(NULL_KEY, "test"), value);
    }

    @Test
    public void testDeserialization() throws Exception {
        Map<LocalDate, String> value = om.readValue(map(NULL_KEY, "test"), TYPE_REF);

        map.put(null, "test");
        Assert.assertEquals(map, value);
    }

    private String map(String key, String value) {
        return String.format("{\"%s\":\"%s\"}", key, value);
    }
}
