package com.fasterxml.jackson.datatype.threetenbp.misc;


import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ModuleTestBase;
import org.threeten.bp.DateTimeException;

import static org.junit.Assert.assertEquals;

public class DateTimeExceptionTest extends ModuleTestBase
{
    private final ObjectMapper MAPPER = newMapper();

    // [modules-java#319]: should not fail to ser/deser DateTimeException
    @Test
    public void testDateTimeExceptionRoundtrip() throws Exception
    {
        String json = MAPPER.writeValueAsString(new DateTimeException("Test!"));
        DateTimeException result = MAPPER.readValue(json, DateTimeException.class);
        assertEquals("Test!", result.getMessage());
    }
}
