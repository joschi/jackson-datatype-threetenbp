package com.fasterxml.jackson.datatype.threetenbp.deser.key;

import static org.threeten.bp.temporal.ChronoField.YEAR;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Year;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.SignStyle;

import com.fasterxml.jackson.databind.DeserializationContext;

public class YearKeyDeserializer extends ThreeTenKeyDeserializer {

    public static final YearKeyDeserializer INSTANCE = new YearKeyDeserializer();

    /*
     * formatter copied from Year. There is no way of getting a reference to the formatter it uses.
     */
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter();

    private YearKeyDeserializer() {
        // singleton
    }

    @Override
    protected Year deserialize(String key, DeserializationContext ctxt) throws IOException {
        try {
            return Year.parse(key, FORMATTER);
        } catch (DateTimeException e) {
            return _rethrowDateTimeException(ctxt, Year.class, e, key);
        }
    }
}
