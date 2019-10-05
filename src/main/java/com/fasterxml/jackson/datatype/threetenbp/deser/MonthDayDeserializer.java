package com.fasterxml.jackson.datatype.threetenbp.deser;

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.MonthDay;
import org.threeten.bp.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserializer for ThreeTen temporal {@link MonthDay}s.
 */
public class MonthDayDeserializer extends ThreeTenDateTimeDeserializerBase<MonthDay>
{
    private static final long serialVersionUID = 1L;

    public static final MonthDayDeserializer INSTANCE = new MonthDayDeserializer(null);

    public MonthDayDeserializer(DateTimeFormatter formatter) {
        super(MonthDay.class, formatter);
    }

    @Override
    protected JsonDeserializer<MonthDay> withDateFormat(DateTimeFormatter dtf) {
        return new MonthDayDeserializer(dtf);
    }
    
    @Override
    public MonthDay deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String string = parser.getValueAsString().trim();
            try {
                if (_formatter == null) {
                    return MonthDay.parse(string);
                }
                return MonthDay.parse(string, _formatter);
            } catch (DateTimeException e) {
                return _handleDateTimeException(context, e, string);
            }
        }
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (MonthDay) parser.getEmbeddedObject();
        }
        if (parser.hasToken(JsonToken.START_ARRAY)){
            return _deserializeFromArray(parser, context);
        }
        return _handleUnexpectedToken(context, parser, JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT);
    }
}
