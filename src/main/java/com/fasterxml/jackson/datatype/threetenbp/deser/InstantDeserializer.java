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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.threetenbp.function.BiFunction;
import com.fasterxml.jackson.datatype.threetenbp.DecimalUtils;
import com.fasterxml.jackson.datatype.threetenbp.function.Function;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.Temporal;

import java.io.IOException;
import java.math.BigDecimal;

import static com.fasterxml.jackson.datatype.threetenbp.DateTimeUtils.timeZoneToZoneId;

/**
 * Deserializer for Java 8 temporal {@link Instant}s, {@link OffsetDateTime}, and {@link ZonedDateTime}s.
 *
 * @author Nick Williams
 * @since 2.2.0
 */
public final class InstantDeserializer<T extends Temporal> extends ThreeTenDeserializerBase<T>
{
    private static final long serialVersionUID = 1L;

    public static final InstantDeserializer<Instant> INSTANT = new InstantDeserializer<>(
            Instant.class,
            new Function<CharSequence, Instant>() {
                @Override
                public Instant apply(final CharSequence text) {
                    return Instant.parse(text);
                }
            },
            new Function<FromIntegerArguments, Instant>() {
                @Override
                public Instant apply(final FromIntegerArguments a) {
                    return Instant.ofEpochMilli(a.value);
                }
            },
            new Function<FromDecimalArguments, Instant>() {
                @Override
                public Instant apply(final FromDecimalArguments a) {
                    return Instant.ofEpochSecond(a.integer, a.fraction);
                }
            },
            null
    );

    public static final InstantDeserializer<OffsetDateTime> OFFSET_DATE_TIME = new InstantDeserializer<>(
            OffsetDateTime.class,
            new Function<CharSequence, OffsetDateTime>() {
                @Override
                public OffsetDateTime apply(CharSequence text) {
                    return OffsetDateTime.parse(text);
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
            }
    );

    public static final InstantDeserializer<ZonedDateTime> ZONED_DATE_TIME = new InstantDeserializer<>(
            ZonedDateTime.class,
            new Function<CharSequence, ZonedDateTime>() {
                @Override
                public ZonedDateTime apply(CharSequence text) {
                    return ZonedDateTime.parse(text);
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
            }
    );

    private final Function<FromIntegerArguments, T> fromMilliseconds;

    private final Function<FromDecimalArguments, T> fromNanoseconds;

    private final Function<CharSequence, T> parse;

    private final BiFunction<T, ZoneId, T> adjust;

    private InstantDeserializer(Class<T> supportedType, Function<CharSequence, T> parse,
                                Function<FromIntegerArguments, T> fromMilliseconds,
                                Function<FromDecimalArguments, T> fromNanoseconds,
                                BiFunction<T, ZoneId, T> adjust)
    {
        super(supportedType);
        this.parse = parse;
        this.fromMilliseconds = fromMilliseconds;
        this.fromNanoseconds = fromNanoseconds;
        this.adjust = adjust == null ? new BiFunction<T, ZoneId, T>() {
            @Override
            public T apply(final T t, final ZoneId zoneId) {
                return t;
            }
        } : adjust;
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        //NOTE: Timestamps contain no timezone info, and are always in configured TZ. Only
        //string values have to be adjusted to the configured TZ.
        switch(parser.getCurrentToken())
        {
            case VALUE_NUMBER_FLOAT:
                BigDecimal value = parser.getDecimalValue();
                long seconds = value.longValue();
                int nanoseconds = DecimalUtils.extractNanosecondDecimal(value, seconds);
                return this.fromNanoseconds.apply(new FromDecimalArguments(
                        seconds, nanoseconds, this.getZone(context)
                ));

            case VALUE_NUMBER_INT:
                if(context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS))
                {
                    return this.fromNanoseconds.apply(new FromDecimalArguments(
                            parser.getLongValue(), 0, this.getZone(context)
                    ));
                }
                else
                {
                    return this.fromMilliseconds.apply(new FromIntegerArguments(
                            parser.getLongValue(), this.getZone(context)
                    ));
                }

            case VALUE_STRING:
                String string = parser.getText().trim();
                if(string.length() == 0)
                    return null;
                if(context.isEnabled(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE))
                    return this.adjust.apply(this.parse.apply(string), this.getZone(context));
                return this.parse.apply(string);
        }
        throw context.mappingException("Expected type float, integer, or string.");
    }

    private ZoneId getZone(DeserializationContext context)
    {
        // Instants are always in UTC, so don't waste compute cycles
        return this._valueClass == Instant.class ? null : timeZoneToZoneId(context.getTimeZone());
    }

    private static class FromIntegerArguments
    {
        public final long value;
        public final ZoneId zoneId;

        private FromIntegerArguments(long value, ZoneId zoneId)
        {
            this.value = value;
            this.zoneId = zoneId;
        }
    }

    private static class FromDecimalArguments
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
