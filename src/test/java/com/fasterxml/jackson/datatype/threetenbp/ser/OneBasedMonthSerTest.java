package com.fasterxml.jackson.datatype.threetenbp.ser;

import static org.junit.Assert.assertEquals;


import com.fasterxml.jackson.datatype.threetenbp.ThreeTenTimeModule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenTimeFeature;
import com.fasterxml.jackson.datatype.threetenbp.ModuleTestBase;
import org.threeten.bp.Month;

public class OneBasedMonthSerTest extends ModuleTestBase
{
    static class Wrapper {
        public Month month;

        public Wrapper(Month m) { month = m; }
        public Wrapper() { }
    }

    @Test
    public void testSerializationFromEnum() throws Exception
    {
        assertEquals( "\"JANUARY\"" , mapperForOneBased()
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .writeValueAsString(Month.JANUARY));
        assertEquals( "\"JANUARY\"" , mapperForZeroBased()
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .writeValueAsString(Month.JANUARY));
    }

    @Test
    public void testSerializationFromEnumWithPattern_oneBased() throws Exception
    {
        ObjectMapper mapper = mapperForOneBased().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        assertEquals( "{\"month\":1}" , mapper.writeValueAsString(new Wrapper(Month.JANUARY)));
    }

    @Test
    public void testSerializationFromEnumWithPattern_zeroBased() throws Exception
    {
        ObjectMapper mapper = mapperForZeroBased().enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        assertEquals( "{\"month\":0}" , mapper.writeValueAsString(new Wrapper(Month.JANUARY)));
    }


    private JsonMapper mapperForZeroBased() {
        return JsonMapper.builder()
                .addModule(new ThreeTenTimeModule().disable(ThreeTenTimeFeature.ONE_BASED_MONTHS))
                .build();
    }

    private JsonMapper mapperForOneBased() {
        return JsonMapper.builder()
                .addModule(new ThreeTenTimeModule().enable(ThreeTenTimeFeature.ONE_BASED_MONTHS))
                .build();
    }

}
