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
import com.fasterxml.jackson.datatype.threetenbp.function.Function;
import org.threeten.bp.MonthDay;
import org.threeten.bp.Period;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import java.io.IOException;

/**
 * Deserializer for all Java temporal {@code org.threeten.bp} types that cannot be represented with numbers
 * and that have parse functions that can take {@link String}s.
 *
 * @author Nick Williams
 * @since 2.4.1
 */
public final class ThreeTenStringParsableDeserializer<T> extends ThreeTenDeserializerBase<T>
{
    private static final long serialVersionUID = 1L;

    public static final ThreeTenStringParsableDeserializer<MonthDay> MONTH_DAY = new ThreeTenStringParsableDeserializer<MonthDay>(
            MonthDay.class,
            new Function<String, MonthDay>() {
                @Override
                public MonthDay apply(final String text) {
                    return MonthDay.parse(text);
                }
            });

    public static final ThreeTenStringParsableDeserializer<Period> PERIOD = new ThreeTenStringParsableDeserializer<Period>(
            Period.class,
            new Function<String, Period>() {
                @Override
                public Period apply(final String text) {
                    return Period.parse(text);
                }
            });

    public static final ThreeTenStringParsableDeserializer<YearMonth> YEAR_MONTH = new ThreeTenStringParsableDeserializer<YearMonth>(
            YearMonth.class,
            new Function<String, YearMonth>() {
                @Override
                public YearMonth apply(final String text) {
                    return YearMonth.parse(text);
                }
            });

    public static final ThreeTenStringParsableDeserializer<ZoneId> ZONE_ID = new ThreeTenStringParsableDeserializer<ZoneId>(
            ZoneId.class,
            new Function<String, ZoneId>() {
                @Override
                public ZoneId apply(final String zoneId) {
                    return ZoneId.of(zoneId);
                }
            });

    public static final ThreeTenStringParsableDeserializer<ZoneOffset> ZONE_OFFSET = new ThreeTenStringParsableDeserializer<ZoneOffset>(
            ZoneOffset.class,
            new Function<String, ZoneOffset>() {
                @Override
                public ZoneOffset apply(final String offsetId) {
                    return ZoneOffset.of(offsetId);
                }
            });

    private transient final Function<String, T> parse;

    private ThreeTenStringParsableDeserializer(Class<T> supportedType, Function<String, T> parse)
    {
        super(supportedType);
        this.parse = parse;
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        String string = parser.getText().trim();
        if(string.length() == 0)
            return null;
        return this.parse.apply(string);
    }
}
