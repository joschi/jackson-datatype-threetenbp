package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import static org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.MonthDay;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.databind.DeserializationContext;

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
    protected MonthDay deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return MonthDay.parse(key, PARSER);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, MonthDay.class, e, key);
        }
    }
}
