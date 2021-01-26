package com.fasterxml.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.datatype.threetenbp.function.ToIntFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.ToLongFunction;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

// TODO deprecate this: SerializationFeature config should be respected,
// default behaviour should be to serialize according to ISO-8601 format
/**
 * @since 2.6
 *
 * @deprecated Since 2.8
 */
@Deprecated
public class ZonedDateTimeWithZoneIdSerializer extends InstantSerializerBase<ZonedDateTime>
{
    private static final long serialVersionUID = 1L;

    public static final ZonedDateTimeWithZoneIdSerializer INSTANCE = new ZonedDateTimeWithZoneIdSerializer();

    protected ZonedDateTimeWithZoneIdSerializer() {
        super(ZonedDateTime.class,
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
                },
                // Serialize in a backwards compatible way: with zone id, using toString method
                null);
    }

    protected ZonedDateTimeWithZoneIdSerializer(ZonedDateTimeWithZoneIdSerializer base,
            Boolean useTimestamp, DateTimeFormatter formatter) {
        this(base, useTimestamp, null, formatter);
    }

    protected ZonedDateTimeWithZoneIdSerializer(ZonedDateTimeWithZoneIdSerializer base,
            Boolean useTimestamp, Boolean useNanoseconds, DateTimeFormatter formatter) {
        super(base, useTimestamp, useNanoseconds, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<?> withFormat(Boolean useTimestamp,
        DateTimeFormatter formatter,
        JsonFormat.Shape shape) {
        return new ZonedDateTimeWithZoneIdSerializer(this, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<?> withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
        return new ZonedDateTimeWithZoneIdSerializer(this, _useTimestamp, writeNanoseconds, _formatter);
    }
}
