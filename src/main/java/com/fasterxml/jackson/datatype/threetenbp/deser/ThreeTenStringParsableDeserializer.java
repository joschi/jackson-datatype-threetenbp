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

import java.io.IOException;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Deserializer for all ThreeTen temporal {@link org.threeten.bp} types that cannot be represented
 * with numbers and that have parse functions that can take {@link String}s,
 * and where format is not configurable.
 *
 * @author Nick Williams
 * @author Tatu Saloranta
 *
 * @since 2.2
 */
public class ThreeTenStringParsableDeserializer
    extends ThreeTenDeserializerBase<Object>
{
    private static final long serialVersionUID = 1L;

    protected final static int TYPE_PERIOD = 1;
    protected final static int TYPE_ZONE_ID = 2;
    protected final static int TYPE_ZONE_OFFSET = 3;

    public static final JsonDeserializer<Period> PERIOD =
        createDeserializer(Period.class, TYPE_PERIOD);

    public static final JsonDeserializer<ZoneId> ZONE_ID =
        createDeserializer(ZoneId.class, TYPE_ZONE_ID);

    public static final JsonDeserializer<ZoneOffset> ZONE_OFFSET =
        createDeserializer(ZoneOffset.class, TYPE_ZONE_OFFSET);

    protected final int _valueType;

    @SuppressWarnings("unchecked")
    protected ThreeTenStringParsableDeserializer(Class<?> supportedType, int valueId)
    {
        super((Class<Object>)supportedType);
        _valueType = valueId;
    }

    @SuppressWarnings("unchecked")
    protected static <T> JsonDeserializer<T> createDeserializer(Class<T> type, int typeId) {
        return (JsonDeserializer<T>) new ThreeTenStringParsableDeserializer(type, typeId);
    }

    @Override
    public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }
            try {
                switch (_valueType) {
                case TYPE_PERIOD:
                    return Period.parse(string);
                case TYPE_ZONE_ID:
                    return ZoneId.of(string);
                case TYPE_ZONE_OFFSET:
                    return ZoneOffset.of(string);
                }
            } catch (DateTimeException e) {
                _rethrowDateTimeException(parser, context, e, string);
            }
        }
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            // 20-Apr-2016, tatu: Related to [databind#1208], can try supporting embedded
            //    values quite easily
            return parser.getEmbeddedObject();
        }
        throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, null);
    }

    @Override
    public Object deserializeWithType(JsonParser parser, DeserializationContext context,
            TypeDeserializer deserializer)
        throws IOException
    {
        /* This is a nasty kludge right here, working around issues like
         * [datatype-jsr310#24]. But should work better than not having the work-around.
         */
        JsonToken t = parser.getCurrentToken();
        if ((t != null) && t.isScalarValue()) {
            return deserialize(parser, context);
        }
        return deserializer.deserializeTypedFromAny(parser, context);
    }
}
