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

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class InstantSerializer extends InstantSerializerBase<Instant>
{
    private static final long serialVersionUID = 1L;

    public static final InstantSerializer INSTANCE = new InstantSerializer();

    protected InstantSerializer() {
        super(Instant.class, Instant::toEpochMilli, Instant::getEpochSecond, Instant::getNano,
                // null -> use 'value.toString()', default format
                null);
    }

    @Deprecated // since 2.14
    protected InstantSerializer(InstantSerializer base,
            Boolean useTimestamp, DateTimeFormatter formatter) {
        this(base, useTimestamp, base._useNanoseconds, formatter);
    }

    /**
     * @since 2.14
     */
    protected InstantSerializer(InstantSerializer base, Boolean useTimestamp,
            DateTimeFormatter formatter, JsonFormat.Shape shape) {
        super(base, useTimestamp, base._useNanoseconds, formatter, shape);
    }

    protected InstantSerializer(InstantSerializer base,
            Boolean useTimestamp, Boolean useNanoseconds, DateTimeFormatter formatter) {
        super(base, useTimestamp, useNanoseconds, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<Instant> withFormat(Boolean useTimestamp,
                                                                  DateTimeFormatter formatter, JsonFormat.Shape shape) {
        return new InstantSerializer(this, useTimestamp, formatter, shape);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<?> withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new InstantSerializer(this, _useTimestamp, writeNanoseconds, _formatter);
    }
}
