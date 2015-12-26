package com.fasterxml.jackson.datatype.threetenbp.ser;

import com.fasterxml.jackson.datatype.threetenbp.function.ToIntFunction;
import com.fasterxml.jackson.datatype.threetenbp.function.ToLongFunction;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class OffsetDateTimeSerializer extends InstantSerializerBase<OffsetDateTime> {
    private static final long serialVersionUID = 1L;

    public static final OffsetDateTimeSerializer INSTANCE = new OffsetDateTimeSerializer();

    protected OffsetDateTimeSerializer() {
        super(OffsetDateTime.class,
                new ToLongFunction<OffsetDateTime>() {
                    @Override
                    public long applyAsLong(OffsetDateTime value) {
                        return value.toInstant().toEpochMilli();
                    }
                },
                new ToLongFunction<OffsetDateTime>() {
                    @Override
                    public long applyAsLong(OffsetDateTime value) {
                        return value.toEpochSecond();
                    }
                },
                new ToIntFunction<OffsetDateTime>() {
                    @Override
                    public int applyAsInt(OffsetDateTime value) {
                        return value.getNano();
                    }
                },
                DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    protected OffsetDateTimeSerializer(OffsetDateTimeSerializer base,
                                       Boolean useTimestamp, DateTimeFormatter formatter) {
        super(base, useTimestamp, formatter);
    }

    @Override
    protected ThreeTenFormattedSerializerBase<?> withFormat(Boolean useTimestamp, DateTimeFormatter formatter) {
        return new OffsetDateTimeSerializer(this, useTimestamp, formatter);
    }
}
