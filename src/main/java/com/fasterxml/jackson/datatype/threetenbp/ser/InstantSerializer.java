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

import com.fasterxml.jackson.datatype.threetenbp.function.ToIntFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.ToLongFunction;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Serializer for ThreeTen temporal {@link Instant}s, {@link OffsetDateTime}, and {@link ZonedDateTime}s.
 *
 * @author Nick Williams
 * @since 2.2
 */
public final class InstantSerializer extends InstantSerializerBase<Instant>
{
    private static final long serialVersionUID = 1L;

    public static final InstantSerializer INSTANCE = new InstantSerializer();

    protected InstantSerializer() {
        super(Instant.class,
                new ToLongFunction<Instant>() {
                    @Override
                    public long applyAsLong(Instant value) {
                        return value.toEpochMilli();
                    }
                },
                new ToLongFunction<Instant>() {
                    @Override
                    public long applyAsLong(Instant value) {
                        return value.getEpochSecond();
                    }
                },
                new ToIntFunction<Instant>() {
                    @Override
                    public int applyAsInt(Instant value) {
                        return value.getNano();
                    }
                },
                // null -> use 'value.toString()', default format
                null);
    }

    protected InstantSerializer(InstantSerializer base,
            Boolean useTimestamp, DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<Instant> withFormat(Boolean useTimestamp, DateTimeFormatter formatter) {
        return new InstantSerializer(this, useTimestamp, formatter);
    }
}
