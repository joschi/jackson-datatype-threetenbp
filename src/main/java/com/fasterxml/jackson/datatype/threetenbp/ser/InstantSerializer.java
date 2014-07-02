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

package com.fasterxml.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.threetenbp.DecimalUtils;
import com.fasterxml.jackson.datatype.threetenbp.function.ToIntFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.ToLongFunction;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.Temporal;

import java.io.IOException;

/**
 * Serializer for ThreeTen temporal {@link Instant}s, {@link OffsetDateTime},
 * and {@link ZonedDateTime}s.
 *
 * @author Nick Williams
 * @since 2.4.1
 */
public final class InstantSerializer<T extends Temporal> extends ThreeTenSerializerBase<T>
{
    public static final InstantSerializer<Instant> INSTANT = new InstantSerializer<Instant>(
            Instant.class,
            new ToLongFunction<Instant>() {
                @Override
                public long applyAsLong(Instant instant) {
                    return instant.toEpochMilli();
                }
            },
            new ToLongFunction<Instant>() {
                @Override
                public long applyAsLong(Instant instant) {
                    return instant.getEpochSecond();
                }
            },
            new ToIntFunction<Instant>() {
                @Override
                public int applyAsInt(Instant instant) {
                    return instant.getNano();
                }
            });

    public static final InstantSerializer<OffsetDateTime> OFFSET_DATE_TIME = new InstantSerializer<OffsetDateTime>(
            OffsetDateTime.class,
            new ToLongFunction<OffsetDateTime>() {
                @Override
                public long applyAsLong(OffsetDateTime dt) {
                    return dt.toInstant().toEpochMilli();
                }
            },
            new ToLongFunction<OffsetDateTime>() {
                @Override
                public long applyAsLong(OffsetDateTime dt) {
                    return dt.toEpochSecond();
                }
            },
            new ToIntFunction<OffsetDateTime>() {
                @Override
                public int applyAsInt(OffsetDateTime dt) {
                    return dt.getNano();
                }
            });

    public static final InstantSerializer<ZonedDateTime> ZONED_DATE_TIME = new InstantSerializer<ZonedDateTime>(
            ZonedDateTime.class,
            new ToLongFunction<ZonedDateTime>() {
                @Override
                public long applyAsLong(ZonedDateTime dt) {
                    return dt.toInstant().toEpochMilli();
                }
            },
            new ToLongFunction<ZonedDateTime>() {
                @Override
                public long applyAsLong(ZonedDateTime dt) {
                    return dt.toEpochSecond();
                }
            },
            new ToIntFunction<ZonedDateTime>() {
                @Override
                public int applyAsInt(ZonedDateTime dt) {
                    return dt.getNano();
                }
            });

    private final ToLongFunction<T> getEpochMillis;

    private final ToLongFunction<T> getEpochSeconds;

    private final ToIntFunction<T> getNanoseconds;

    private InstantSerializer(Class<T> supportedType, ToLongFunction<T> getEpochMillis,
                              ToLongFunction<T> getEpochSeconds, ToIntFunction<T> getNanoseconds)
    {
        super(supportedType);
        this.getEpochMillis = getEpochMillis;
        this.getEpochSeconds = getEpochSeconds;
        this.getNanoseconds = getNanoseconds;
    }

    @Override
    public void serialize(T instant, JsonGenerator generator, SerializerProvider provider) throws IOException
    {
        if(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
        {
            if(provider.isEnabled(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS))
            {
                generator.writeNumber(DecimalUtils.toDecimal(
                        this.getEpochSeconds.applyAsLong(instant), this.getNanoseconds.applyAsInt(instant)
                ));
            }
            else
            {
                generator.writeNumber(this.getEpochMillis.applyAsLong(instant));
            }
        }
        else
        {
            generator.writeString(instant.toString());
        }
    }
}
