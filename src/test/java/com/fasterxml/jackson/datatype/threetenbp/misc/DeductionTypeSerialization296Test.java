package com.fasterxml.jackson.datatype.threetenbp.misc;


import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.threetenbp.ModuleTestBase;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.MonthDay;
import org.threeten.bp.OffsetTime;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

// for [modules-java8#296]: problem with `JsonTypeInfo.Id.DEDUCTION`
public class DeductionTypeSerialization296Test extends ModuleTestBase
{
    static class Wrapper {
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
        public Object value;

        public Wrapper(Object value) {
            this.value = value;
        }
    }

    private final ObjectMapper MAPPER = mapperBuilder()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    @Test
    public void testLocalDate() throws Exception
    {
        LocalDate date = LocalDate.of(1986, Month.JANUARY, 17);
        Assert.assertEquals(a2q("{'value':'1986-01-17'}"),
                MAPPER.writeValueAsString(new Wrapper(date)));
    }

    @Test
    public void testLocalDateTime() throws Exception
    {
        LocalDateTime datetime = LocalDateTime.of(2013, Month.AUGUST, 21, 9, 22, 0, 57);
        Assert.assertEquals(a2q("{'value':'2013-08-21T09:22:00.000000057'}"),
                MAPPER.writeValueAsString(new Wrapper(datetime)));
    }

    @Test
    public void testLocalTime() throws Exception
    {
        LocalTime time = LocalTime.of(9, 22, 57);
        Assert.assertEquals(a2q("{'value':'09:22:57'}"),
                MAPPER.writeValueAsString(new Wrapper(time)));
    }

    @Test
    public void testMonthDate() throws Exception
    {
        MonthDay date = MonthDay.of(Month.JANUARY, 17);
        Assert.assertEquals(a2q("{'value':'--01-17'}"),
                MAPPER.writeValueAsString(new Wrapper(date)));
    }

    @Test
    public void testOffsetTime() throws Exception
    {
        OffsetTime time = OffsetTime.of(15, 43, 0, 0, ZoneOffset.of("+0300"));
        Assert.assertEquals(a2q("{'value':'15:43+03:00'}"),
                MAPPER.writeValueAsString(new Wrapper(time)));
    }

    @Test
    public void testYearMonth() throws Exception
    {
        YearMonth date = YearMonth.of(1986, Month.JANUARY);
        Assert.assertEquals(a2q("{'value':'1986-01'}"),
                MAPPER.writeValueAsString(new Wrapper(date)));
    }

    @Test
    public void testZoneId() throws Exception
    {
        ZoneId zone = ZoneId.of("America/Denver");
        Assert.assertEquals(a2q("{'value':'America/Denver'}"),
                MAPPER.writeValueAsString(new Wrapper(zone)));
    }
}
