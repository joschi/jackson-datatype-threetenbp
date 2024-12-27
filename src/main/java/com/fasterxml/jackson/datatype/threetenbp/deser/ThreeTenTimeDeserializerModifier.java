package com.fasterxml.jackson.datatype.threetenbp.deser;


import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import org.threeten.bp.Month;

/**
 * @since 2.17
 */
public class ThreeTenTimeDeserializerModifier extends BeanDeserializerModifier {
    private static final long serialVersionUID = 1L;

    private final boolean _oneBaseMonths;

    public ThreeTenTimeDeserializerModifier(boolean oneBaseMonths) {
        _oneBaseMonths = oneBaseMonths;
    }

    @Override
    public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> defaultDeserializer) {
        if (_oneBaseMonths && type.hasRawClass(Month.class)) {
            return new OneBasedMonthDeserializer(defaultDeserializer);
        }
        return defaultDeserializer;
    }
}
