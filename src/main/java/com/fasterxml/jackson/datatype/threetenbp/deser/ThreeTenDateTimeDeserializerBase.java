package com.fasterxml.jackson.datatype.threetenbp.deser;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import org.threeten.bp.format.DateTimeFormatter;

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
        if (property != null) {
            JsonFormat.Value format = ctxt.getAnnotationIntrospector().findFormat((Annotated) property.getMember());
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
                    return withDateFormat(df);
                }
                // any use for TimeZone?
            }
        }
        return this;
    }
}