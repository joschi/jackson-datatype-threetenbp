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
import org.threeten.bp.LocalDate;

import java.io.IOException;

/**
 * Serializer for ThreeTen temporal {@link LocalDate}s.
 *
 * @author Nick Williams
 * @since 2.4.1
 */
public class LocalDateSerializer extends ThreeTenArraySerializerBase<LocalDate>
{
    public static final LocalDateSerializer INSTANCE = new LocalDateSerializer();

    private LocalDateSerializer()
    {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider provider) throws IOException
    {
        if(provider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
        {
            generator.writeStartArray();
            generator.writeNumber(date.getYear());
            generator.writeNumber(date.getMonthValue());
            generator.writeNumber(date.getDayOfMonth());
            generator.writeEndArray();
        }
        else
        {
            generator.writeString(date.toString());
        }
    }
}
