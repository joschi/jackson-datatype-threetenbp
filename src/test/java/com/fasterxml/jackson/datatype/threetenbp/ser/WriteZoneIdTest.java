package com.fasterxml.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.datatype.threetenbp.ThreeTenTimeModule;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import com.fasterxml.jackson.datatype.threetenbp.MockObjectConfiguration;
import com.fasterxml.jackson.datatype.threetenbp.ModuleTestBase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WriteZoneIdTest extends ModuleTestBase
{
    static class DummyClassWithDate {
        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss Z",
                with = JsonFormat.Feature.WRITE_DATES_WITH_ZONE_ID)
        public ZonedDateTime date;

        DummyClassWithDate() { }

        public DummyClassWithDate(ZonedDateTime date) {
            this.date = date;
        }
    }

    private static ObjectMapper MAPPER = newMapper();

    @Test
    public void testSerialization01() throws Exception
    {
        ZoneId id = ZoneId.of("America/Chicago");
        String value = MAPPER.writeValueAsString(id);
        assertEquals("The value is not correct.", "\"America/Chicago\"", value);
    }

    @Test
    public void testSerialization02() throws Exception
    {
        ZoneId id = ZoneId.of("America/Anchorage");
        String value = MAPPER.writeValueAsString(id);
        assertEquals("The value is not correct.", "\"America/Anchorage\"", value);
    }

    @Test
    public void testSerializationWithTypeInfo01() throws Exception
    {
        ZoneId id = ZoneId.of("America/Denver");
        ObjectMapper mapper = mapperBuilder()
                .addMixIn(ZoneId.class, MockObjectConfiguration.class)
                .addModule(new ThreeTenTimeModule())
                .build();
        String value = mapper.writeValueAsString(id);
        assertEquals("The value is not correct.", "[\"org.threeten.bp.ZoneId\",\"America/Denver\"]", value);
    }

    @Test
    public void testJacksonAnnotatedPOJOWithDateWithTimezoneToJson() throws Exception
    {
        String ZONE_ID_STR = "Asia/Krasnoyarsk";
        final ZoneId ZONE_ID = ZoneId.of(ZONE_ID_STR);

        DummyClassWithDate input = new DummyClassWithDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(0L), ZONE_ID));

        // 30-Jun-2016, tatu: Exact time seems to vary a bit based on DST, so let's actually
        //    just verify appending of timezone id itself:
        String json = MAPPER.writeValueAsString(input);
        if (!json.contains("\"1970-01-01T")) {
            Assert.fail("Should contain time prefix, did not: "+json);
        }
        String match = String.format("[%s]", ZONE_ID_STR);
        if (!json.contains(match)) {
            Assert.fail("Should contain zone id "+match+", does not: "+json);
        }
    }

    @Test
    public void testMapSerialization() throws Exception {
        final ZonedDateTime datetime = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Warsaw]");
        final HashMap<ZonedDateTime, String> map = new HashMap<>();
        map.put(datetime, "");

        String json = MAPPER.writer()
                .with(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                .writeValueAsString(map);
        Assert.assertEquals("{\"2007-12-03T10:15:30+01:00[Europe/Warsaw]\":\"\"}", json);
    }
}
