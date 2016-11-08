/*
 * Copyright 2013 FasterXML.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package com.fasterxml.jackson.datatype.threetenbp.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.datatype.threetenbp.DecimalUtils;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.datatype.threetenbp.function.BiFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.Function;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;

/**
 * Deserializer for ThreeTen temporal {@link Instant}s, {@link OffsetDateTime}, and {@link ZonedDateTime}s.
 *
 * @author Nick Williams
 * @since 2.2
 */
public class InstantDeserializer<T extends Temporal>
    extends ThreeTenDateTimeDeserializerBase<T>
{
    private static final long serialVersionUID = 1L;

    public static final InstantDeserializer<Instant> INSTANT = new InstantDeserializer<>(
            Instant.class, DateTimeFormatter.ISO_INSTANT,
            new Function<TemporalAccessor, Instant>() {
                @Override
                public Instant apply(TemporalAccessor temporalAccessor) {
                    return Instant.from(temporalAccessor);
                }
            },
            new Function<FromIntegerArguments, Instant>() {
                @Override
                public Instant apply(FromIntegerArguments a) {
                    return Instant.ofEpochMilli(a.value);
                }
            },
            new Function<FromDecimalArguments, Instant>() {
                @Override
                public Instant apply(FromDecimalArguments a) {
                    return Instant.ofEpochSecond(a.integer, a.fraction);
                }
            },
            null,
            true // yes, replace +0000 with Z
    );

    public static final InstantDeserializer<OffsetDateTime> OFFSET_DATE_TIME = new InstantDeserializer<>(
            OffsetDateTime.class, DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            new Function<TemporalAccessor, OffsetDateTime>() {
                @Override
                public OffsetDateTime apply(TemporalAccessor temporalAccessor) {
                    return OffsetDateTime.from(temporalAccessor);
                }
            },
            new Function<FromIntegerArguments, OffsetDateTime>() {
                @Override
                public OffsetDateTime apply(FromIntegerArguments a) {
                    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId);
                }
            },
            new Function<FromDecimalArguments, OffsetDateTime>() {
                @Override
                public OffsetDateTime apply(FromDecimalArguments a) {
                    return OffsetDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId);
                }
            },
            new BiFunction<OffsetDateTime, ZoneId, OffsetDateTime>() {
                @Override
                public OffsetDateTime apply(OffsetDateTime d, ZoneId z) {
                    return d.withOffsetSameInstant(z.getRules().getOffset(d.toLocalDateTime()));
                }
            },
            true // yes, replace +0000 with Z
    );

    public static final InstantDeserializer<ZonedDateTime> ZONED_DATE_TIME = new InstantDeserializer<>(
            ZonedDateTime.class, DateTimeFormatter.ISO_ZONED_DATE_TIME,
            new Function<TemporalAccessor, ZonedDateTime>() {
                @Override
                public ZonedDateTime apply(TemporalAccessor temporalAccessor) {
                    return ZonedDateTime.from(temporalAccessor);
                }
            },
            new Function<FromIntegerArguments, ZonedDateTime>() {
                @Override
                public ZonedDateTime apply(FromIntegerArguments a) {
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId);
                }
            },
            new Function<FromDecimalArguments, ZonedDateTime>() {
                @Override
                public ZonedDateTime apply(FromDecimalArguments a) {
                    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId);
                }
            },
            new BiFunction<ZonedDateTime, ZoneId, ZonedDateTime>() {
                @Override
                public ZonedDateTime apply(ZonedDateTime zonedDateTime, ZoneId zoneId) {
                    return zonedDateTime.withZoneSameInstant(zoneId);
                }
            },
            false // keep +0000 and Z separate since zones explicitly supported
    );

    protected final Function<FromIntegerArguments, T> fromMilliseconds;

    protected final Function<FromDecimalArguments, T> fromNanoseconds;

    protected final Function<TemporalAccessor, T> parsedToValue;
    
    protected final BiFunction<T, ZoneId, T> adjust;

    /**
     * In case of vanilla `Instant` we seem to need to translate "+0000"
     * timezone designator into plain "Z" for some reason; see
     * [datatype-jsr310#79] for more info
     *
     * @since 2.7.5
     */
    protected final boolean replace0000AsZ;

    /**
     * Flag for <code>JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE</code>
     *
     * @since 2.8
     */
    protected final Boolean _adjustToContextTZOverride;

    protected InstantDeserializer(Class<T> supportedType,
            DateTimeFormatter formatter,
            Function<TemporalAccessor, T> parsedToValue,
            Function<FromIntegerArguments, T> fromMilliseconds,
            Function<FromDecimalArguments, T> fromNanoseconds,
            BiFunction<T, ZoneId, T> adjust,
            boolean replace0000AsZ)
    {
        super(supportedType, formatter);
        this.parsedToValue = parsedToValue;
        this.fromMilliseconds = fromMilliseconds;
        this.fromNanoseconds = fromNanoseconds;
        this.adjust = adjust == null ? new BiFunction<T, ZoneId, T>() {
            @Override
            public T apply(T t, ZoneId zoneId) {
                return t;
            }
        } : adjust;
        this.replace0000AsZ = replace0000AsZ;
        _adjustToContextTZOverride = null;
    }

    @SuppressWarnings("unchecked")
    protected InstantDeserializer(InstantDeserializer<T> base, DateTimeFormatter f)
    {
        super((Class<T>) base.handledType(), f);
        parsedToValue = base.parsedToValue;
        fromMilliseconds = base.fromMilliseconds;
        fromNanoseconds = base.fromNanoseconds;
        adjust = base.adjust;
        replace0000AsZ = (_formatter == DateTimeFormatter.ISO_INSTANT);
        _adjustToContextTZOverride = base._adjustToContextTZOverride;
    }

    @SuppressWarnings("unchecked")
    protected InstantDeserializer(InstantDeserializer<T> base, Boolean adjustToContextTimezoneOverride)
    {
        super((Class<T>) base.handledType(), base._formatter);
        parsedToValue = base.parsedToValue;
        fromMilliseconds = base.fromMilliseconds;
        fromNanoseconds = base.fromNanoseconds;
        adjust = base.adjust;
        replace0000AsZ = base.replace0000AsZ;
        _adjustToContextTZOverride = adjustToContextTimezoneOverride;
    }
    
    @Override
    protected JsonDeserializer<T> withDateFormat(DateTimeFormatter dtf) {
        if (dtf == _formatter) {
            return this;
        }
        return new InstantDeserializer<T>(this, dtf);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        //NOTE: Timestamps contain no timezone info, and are always in configured TZ. Only
        //string values have to be adjusted to the configured TZ.
        switch (parser.getCurrentTokenId())
        {
            case JsonTokenId.ID_NUMBER_FLOAT:
                return _fromDecimal(context, parser.getDecimalValue());

            case JsonTokenId.ID_NUMBER_INT:
                return _fromLong(context, parser.getLongValue());

            case JsonTokenId.ID_STRING:
            {
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }
                // only check for other parsing modes if we are using default formatter
                if (_formatter == DateTimeFormatter.ISO_INSTANT ||
                    _formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME ||
                    _formatter == DateTimeFormatter.ISO_ZONED_DATE_TIME) {
                    // 22-Jan-2016, [datatype-jsr310#16]: Allow quoted numbers too
                    int dots = _countPeriods(string);
                    if (dots >= 0) { // negative if not simple number
                        try {
                            if (dots == 0) {
                                return _fromLong(context, Long.parseLong(string));
                            }
                            if (dots == 1) {
                                return _fromDecimal(context, new BigDecimal(string));
                            }
                        } catch (NumberFormatException e) {
                            // fall through to default handling, to get error there
                        }
                    }
                    // 24-May-2016, tatu: as per [datatype-jsr310#79] seems like we need
                    //   some massaging in some cases...
                    if (replace0000AsZ) {
                        if (string.endsWith("+0000")) {
                            string = string.substring(0, string.length() - 5) + "Z";
                        }
                    }
                }

                T value;
                try {
                    TemporalAccessor acc = _formatter.parse(string);
                    value = parsedToValue.apply(acc);
                    if (shouldAdjustToContextTimezone(context)) {
                        return adjust.apply(value, this.getZone(context));
                    }
                } catch (DateTimeException e) {
                    value = _rethrowDateTimeException(parser, context, e, string);
                }
                return value;
            }

            case JsonTokenId.ID_EMBEDDED_OBJECT:
                // 20-Apr-2016, tatu: Related to [databind#1208], can try supporting embedded
                //    values quite easily
                return (T) parser.getEmbeddedObject();
        }
        throw context.mappingException("Expected type float, integer, or string.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<T> createContextual(DeserializationContext ctxt,
                                                BeanProperty property) throws JsonMappingException
    {
        InstantDeserializer<T> deserializer =
                (InstantDeserializer<T>)super.createContextual(ctxt, property);
        if (deserializer != this) {
            JsonFormat.Value val = findFormatOverrides(ctxt, property, handledType());
            if (val != null) {
                return  new InstantDeserializer<>(deserializer, val.getFeature(JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE));
            }
        }
        return this;
    }

    protected boolean shouldAdjustToContextTimezone(DeserializationContext context) {
        return (_adjustToContextTZOverride != null) ? _adjustToContextTZOverride :
                context.isEnabled(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

    /**
     * Helper method to find Strings of form "all digits" and "digits-comma-digits"
     */
    protected int _countPeriods(String str)
    {
        int commas = 0;
        for (int i = 0, end = str.length(); i < end; ++i) {
            int ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                if (ch == '.') {
                    ++commas;
                } else {
                    return -1;
                }
            }
        }
        return commas;
    }
    
    protected T _fromLong(DeserializationContext context, long timestamp)
    {
        if(context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)){
            return fromNanoseconds.apply(new FromDecimalArguments(
                    timestamp, 0, this.getZone(context)
            ));
        }
        return fromMilliseconds.apply(new FromIntegerArguments(
                timestamp, this.getZone(context)));
    }
    
    protected T _fromDecimal(DeserializationContext context, BigDecimal value)
    {
        long seconds = value.longValue();
        int nanoseconds = DecimalUtils.extractNanosecondDecimal(value, seconds);
        return fromNanoseconds.apply(new FromDecimalArguments(
                seconds, nanoseconds, getZone(context)));
    }
    
    private ZoneId getZone(DeserializationContext context)
    {
        // Instants are always in UTC, so don't waste compute cycles
        return (_valueClass == Instant.class) ? null : DateTimeUtils.toZoneId(context.getTimeZone());
    }

    public static class FromIntegerArguments // since 2.8.3
    {
        public final long value;
        public final ZoneId zoneId;

        private FromIntegerArguments(long value, ZoneId zoneId)
        {
            this.value = value;
            this.zoneId = zoneId;
        }
    }

    public static class FromDecimalArguments // since 2.8.3
    {
        public final long integer;
        public final int fraction;
        public final ZoneId zoneId;

        private FromDecimalArguments(long integer, int fraction, ZoneId zoneId)
        {
            this.integer = integer;
            this.fraction = fraction;
            this.zoneId = zoneId;
        }
    }
}
