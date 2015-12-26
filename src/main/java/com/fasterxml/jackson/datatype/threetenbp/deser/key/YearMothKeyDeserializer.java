package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.SignStyle;

import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;
import static org.threeten.bp.temporal.ChronoField.YEAR;

public class YearMothKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final YearMothKeyDeserializer INSTANCE = new YearMothKeyDeserializer();

    // parser copied from YearMonth
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .toFormatter();

    private YearMothKeyDeserializer() {
        // singleton
    }

    @Override
    protected YearMonth deserialize(String key, DeserializationContext ctxt) {
        return YearMonth.parse(key, FORMATTER);
    }

}
