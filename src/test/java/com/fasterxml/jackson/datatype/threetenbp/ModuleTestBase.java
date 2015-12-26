package com.fasterxml.jackson.datatype.threetenbp;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleTestBase
{
    protected ObjectMapper newMapper() {
        return new ObjectMapper()
                .registerModule(new ThreeTenModule());
    }

    protected String aposToQuotes(String json) {
        return json.replace("'", "\"");
    }
}
