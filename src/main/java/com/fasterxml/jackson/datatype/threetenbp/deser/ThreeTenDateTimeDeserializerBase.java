package com.fasterxml.jackson.datatype.threetenbp.deser;

import java.io.IOException;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

@SuppressWarnings("serial")
public abstract class ThreeTenDateTimeDeserializerBase<T>
    extends ThreeTenDeserializerBase<T>
    implements ContextualDeserializer
{
    protected final DateTimeFormatter _formatter;

    protected ThreeTenDateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter f)
    {
        super(supportedType);
        _formatter = f;
    }

    protected abstract JsonDeserializer<T> withDateFormat(DateTimeFormatter dtf);

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException
    {
        JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
        if (format != null) {
            if (format.hasPattern()) {
                final String pattern = format.getPattern();
                final Locale locale = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
                DateTimeFormatter df;
                if (locale == null) {
                    df = DateTimeFormatter.ofPattern(pattern);
                } else {
                    df = DateTimeFormatter.ofPattern(pattern, locale);
                }
                //Issue #69: For instant serializers/deserializers we need to configure the formatter with
                //a time zone picked up from JsonFormat annotation, otherwise serialization might not work
                if (format.hasTimeZone()) {
                    df = df.withZone(DateTimeUtils.toZoneId(format.getTimeZone()));
                }
                return withDateFormat(df);
            }
            // any use for TimeZone?
        }
        return this;
   }

    protected void _throwNoNumericTimestampNeedTimeZone(JsonParser p, DeserializationContext ctxt)
        throws IOException
    {
        ctxt.reportInputMismatch(handledType(),
"raw timestamp (%d) not allowed for `%s`: need additional information such as an offset or time-zone (see class Javadocs)",
p.getNumberValue(), handledType().getName());
    }
}
