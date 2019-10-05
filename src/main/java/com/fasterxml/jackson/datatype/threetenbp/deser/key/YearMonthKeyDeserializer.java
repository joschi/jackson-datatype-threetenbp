package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;
import static org.threeten.bp.temporal.ChronoField.YEAR;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.SignStyle;

import com.fasterxml.jackson.databind.DeserializationContext;

/**
 * @since 2.10 (had a typo previously)
 */
public class YearMonthKeyDeserializer extends ThreeTenKeyDeserializer {
    public static final YearMonthKeyDeserializer INSTANCE = new YearMonthKeyDeserializer();

    // parser copied from YearMonth
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .toFormatter();

    protected YearMonthKeyDeserializer() { } // only protected for 2.10, to allow sub-class

    @Override
    protected YearMonth deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return YearMonth.parse(key, FORMATTER);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, YearMonth.class, e, key);
        }
    }
}
