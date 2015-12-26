package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.MonthDay;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import static org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;

public class MonthDayKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final MonthDayKeyDeserializer INSTANCE = new MonthDayKeyDeserializer();

    // formatter copied from MonthDay
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendLiteral("--")
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .toFormatter();

    private MonthDayKeyDeserializer() {
        // singleton
    }

    @Override
    protected MonthDay deserialize(String key, DeserializationContext ctxt) {
        return MonthDay.parse(key, PARSER);
    }

}
