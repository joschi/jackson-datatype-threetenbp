package com.fasterxml.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.datatype.threetenbp.function.ToIntFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.ToLongFunction;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

// TODO deprecate this: SerializationFeature config should be respected,
// default behaviour should be to serialize according to ISO-8601 format

/**
 * @since 2.6
 */
public class ZonedDateTimeWithZoneIdSerializer extends InstantSerializerBase<ZonedDateTime> {
    private static final long serialVersionUID = 1L;

    public static final ZonedDateTimeWithZoneIdSerializer INSTANCE = new ZonedDateTimeWithZoneIdSerializer();

    protected ZonedDateTimeWithZoneIdSerializer() {
        super(ZonedDateTime.class,
                new ToLongFunction<ZonedDateTime>() {
                    @Override
                    public long applyAsLong(ZonedDateTime value) {
                        return value.toInstant().toEpochMilli();
                    }
                },
                new ToLongFunction<ZonedDateTime>() {
                    @Override
                    public long applyAsLong(ZonedDateTime value) {
                        return value.toEpochSecond();
                    }
                },
                new ToIntFunction<ZonedDateTime>() {
                    @Override
                    public int applyAsInt(ZonedDateTime value) {
                        return value.getNano();
                    }
                },
                // Serialize in a backwards compatible way: with zone id, using toString method
                null);
    }

    protected ZonedDateTimeWithZoneIdSerializer(ZonedDateTimeWithZoneIdSerializer base,
                                                Boolean useTimestamp, DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<?> withFormat(Boolean useTimestamp, DateTimeFormatter formatter) {
        return new ZonedDateTimeWithZoneIdSerializer(this, useTimestamp, formatter);
    }

}
