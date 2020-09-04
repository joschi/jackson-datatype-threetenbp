package com.fasterxml.jackson.datatype.threetenbp.deser;

import java.io.IOException;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.ResolverStyle;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

@SuppressWarnings("serial")
public abstract class ThreeTenDateTimeDeserializerBase<T>
    extends ThreeTenDeserializerBase<T>
    implements ContextualDeserializer
{
    protected final DateTimeFormatter _formatter;

    /**
     * Setting that indicates the {@link JsonFormat.Shape} specified for this deserializer
     * as a {@link JsonFormat.Shape} annotation on property or class, or due to per-type
     * "config override", or from global settings:
     * If Shape is NUMBER_INT, the input value is considered to be epoch days. If not a
     * NUMBER_INT, and the deserializer was not specified with the leniency setting of true,
     * then an exception will be thrown.
     * @see [jackson-modules-java8#58] for more info
     *
     * @since 2.11
     */
    protected final Shape _shape;

    protected ThreeTenDateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter f) {
        super(supportedType);
        _formatter = f;
        _shape = null;
    }

    /**
     * @since 2.11
     */
    public ThreeTenDateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter f, Boolean leniency) {
        super(supportedType, leniency);
        _formatter = f;
        _shape = null;
    }

    /**
     * @since 2.10
     */
    protected ThreeTenDateTimeDeserializerBase(ThreeTenDateTimeDeserializerBase<T> base,
            DateTimeFormatter f) {
        super(base);
        _formatter = f;
        _shape = base._shape;
    }
    
    /**
     * @since 2.10
     */
    protected ThreeTenDateTimeDeserializerBase(ThreeTenDateTimeDeserializerBase<T> base,
            Boolean leniency) {
        super(base, leniency);
        _formatter = base._formatter;
        _shape = base._shape;
    }

    /**
     * @since 2.11
     */
    protected ThreeTenDateTimeDeserializerBase(ThreeTenDateTimeDeserializerBase<T> base,
            Shape shape) {
        super(base);
        _formatter = base._formatter;
        _shape = shape;
    }

    protected abstract ThreeTenDateTimeDeserializerBase<T> withDateFormat(DateTimeFormatter dtf);

    /**
     * @since 2.10
     */
    @Override
    protected abstract ThreeTenDateTimeDeserializerBase<T> withLeniency(Boolean leniency);

    /**
     * @since 2.11
     */
    protected abstract ThreeTenDateTimeDeserializerBase<T> withShape(Shape shape);

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException
    {
        JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
        ThreeTenDateTimeDeserializerBase<?> deser = this;
        if (format != null) {
            // 17-Aug-2019, tatu: For 2.10 let's start considering leniency/strictness too
            if (format.hasLenient()) {
                Boolean leniency = format.getLenient();
                if (leniency != null) {
                    deser = deser.withLeniency(leniency);
                }
            }
            if (format.hasPattern()) {
                final String pattern = format.getPattern();
                final Locale locale = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
                DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                if (acceptCaseInsensitiveValues(ctxt, format)) {
                    builder.parseCaseInsensitive();
                }
                builder.appendPattern(pattern);
                DateTimeFormatter df;
                if (locale == null) {
                    df = builder.toFormatter();
                } else {
                    df = builder.toFormatter(locale);
                }

                // [#148]: allow strict parsing
                if (!deser.isLenient()) {
                    df = df.withResolverStyle(ResolverStyle.STRICT);
                }

                // [#69]: For instant serializers/deserializers we need to configure the formatter with
                //a time zone picked up from JsonFormat annotation, otherwise serialization might not work
                if (format.hasTimeZone()) {
                    df = df.withZone(DateTimeUtils.toZoneId(format.getTimeZone()));
                }
                deser = deser.withDateFormat(df);
            }
            // [#58]: For LocalDate deserializers we need to configure the formatter with
            //a shape picked up from JsonFormat annotation, to decide if the value is EpochSeconds
            JsonFormat.Shape shape = format.getShape();
            if (shape != null && shape != _shape) {
                deser = deser.withShape(shape);
            }
            // any use for TimeZone?
        }
        return deser;
    }

    private boolean acceptCaseInsensitiveValues(DeserializationContext ctxt, JsonFormat.Value format) 
    {
        Boolean enabled = format.getFeature( Feature.ACCEPT_CASE_INSENSITIVE_VALUES);
        if( enabled == null) {
            enabled = ctxt.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES);
        }
        return enabled;
    }
    
    protected void _throwNoNumericTimestampNeedTimeZone(JsonParser p, DeserializationContext ctxt)
        throws IOException
    {
        ctxt.reportInputMismatch(handledType(),
"raw timestamp (%d) not allowed for `%s`: need additional information such as an offset or time-zone (see class Javadocs)",
p.getNumberValue(), handledType().getName());
    }
}
