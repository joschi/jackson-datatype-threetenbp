package com.fasterxml.jackson.datatype.threetenbp.deser;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.MonthDay;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Deserializer for ThreeTen temporal {@link MonthDay}s.
 */
public class MonthDayDeserializer extends ThreeTenDateTimeDeserializerBase<MonthDay>
{
    private static final long serialVersionUID = 1L;

    public static final MonthDayDeserializer INSTANCE = new MonthDayDeserializer();

    /**
     * NOTE: only {@code public} so that use via annotations (see [modules-java8#202])
     * is possible
     *
     * @since 2.12
     */
    public MonthDayDeserializer() {
        this(null);
    }

    public MonthDayDeserializer(DateTimeFormatter formatter) {
        super(MonthDay.class, formatter);
    }

    /**
     * Since 2.12
     */
    protected MonthDayDeserializer(MonthDayDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    /**
     * Since 2.16
     */
    protected MonthDayDeserializer(MonthDayDeserializer base,
            Boolean leniency,
            DateTimeFormatter formatter,
            JsonFormat.Shape shape) {
        super(base, leniency, formatter, shape);
    }

    @Override
    protected MonthDayDeserializer withLeniency(Boolean leniency) {
        return new MonthDayDeserializer(this, leniency);
    }

    @Override
    protected MonthDayDeserializer withDateFormat(DateTimeFormatter dtf) {
        return new MonthDayDeserializer(this, _isLenient, dtf, _shape);
    }

    @Override
    public MonthDay deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            return _fromString(parser, context, parser.getText());
        }
        // 30-Sep-2020, tatu: New! "Scalar from Object" (mostly for XML)
        if (parser.isExpectedStartObjectToken()) {
            return _fromString(parser, context,
                    context.extractScalarFromObject(parser, this, handledType()));
        }
        if (parser.isExpectedStartArrayToken()) {
            JsonToken t = parser.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return null;
            }
            if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT)
                    && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                final MonthDay parsed = deserialize(parser, context);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(parser, context);
                }
                return parsed;
            }
            if (t != JsonToken.VALUE_NUMBER_INT) {
                _reportWrongToken(context, JsonToken.VALUE_NUMBER_INT, "month");
            }
            int month = parser.getIntValue();
            int day = parser.nextIntValue(-1);
            if (day == -1) {
                if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    _reportWrongToken(context, JsonToken.VALUE_NUMBER_INT, "day");
                }
                day = parser.getIntValue();
            }
            if (parser.nextToken() != JsonToken.END_ARRAY) {
                throw context.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY,
                        "Expected array to end");
            }
            return MonthDay.of(month, day);
        }
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (MonthDay) parser.getEmbeddedObject();
        }
        return _handleUnexpectedToken(context, parser,
                JsonToken.VALUE_STRING, JsonToken.START_ARRAY);
    }

    protected MonthDay _fromString(JsonParser p, DeserializationContext ctxt,
            String string0)  throws IOException
    {
        String string = string0.trim();
        if (string.length() == 0) {
            // 22-Oct-2020, tatu: not sure if we should pass original (to distinguish
            //   b/w empty and blank); for now don't which will allow blanks to be
            //   handled like "regular" empty (same as pre-2.12)
            return _fromEmptyString(p, ctxt, string);
        }
        try {
            if (_formatter == null) {
                return MonthDay.parse(string);
            }
            return MonthDay.parse(string, _formatter);
        } catch (DateTimeException e) {
            return _handleDateTimeException(ctxt, e, string);
        }
    }
}
