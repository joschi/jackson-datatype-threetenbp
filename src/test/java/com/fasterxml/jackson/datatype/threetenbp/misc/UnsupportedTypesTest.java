package com.fasterxml.jackson.datatype.threetenbp.misc;

import org.junit.Ignore;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAdjusters;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ModuleTestBase;

public class UnsupportedTypesTest extends ModuleTestBase
{
    // [modules-java8#207]
    static class TAWrapper {
        public TemporalAdjuster a;

        public TAWrapper(TemporalAdjuster a) {
            this.a = a;
        }
    }

    // [modules-java#207]: should not fail on `TemporalAdjuster`
    // https://github.com/FasterXML/jackson-databind/commit/e237bca2a35b6b41505b1ce2a7793b9224f31877
    @Test
    @Ignore("Special case is handled in jackson-databind")
    public void testTemporalAdjusterSerialization() throws Exception
    {
        ObjectMapper mapper = newMapper();

        // Not 100% sure how this happens, actually; should fail on empty "POJO"?
        Assert.assertEquals(a2q("{'a':{}}"),
                mapper.writeValueAsString(new TAWrapper(TemporalAdjusters.firstDayOfMonth())));
    }
}
