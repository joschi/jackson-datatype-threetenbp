package com.fasterxml.jackson.datatype.threetenbp.util;

import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalUnit;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.datatype.threetenbp.function.Function;

import static com.fasterxml.jackson.datatype.threetenbp.util.DurationUnitConverter.DurationSerialization.deserializer;

/**
 * Handles the conversion of the duration based on the API of {@link Duration} for a restricted set of {@link ChronoUnit}.
 * Only the units considered as accurate are supported in this converter since are the only ones capable of handling
 * deserialization in a precise manner (see {@link ChronoUnit#isDurationEstimated}).
 *
 * @since 2.12
 */
public class DurationUnitConverter {

    protected static class DurationSerialization {
        final Function<Duration, Long> serializer;
        final Function<Long, Duration> deserializer;

        DurationSerialization(
                Function<Duration, Long> serializer,
                Function<Long, Duration> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        static Function<Long, Duration> deserializer(final TemporalUnit unit) {
            return new Function<Long, Duration>() {
                @Override
                public Duration apply(Long v) {
                    return Duration.of(v, unit);
                }
            };
        }
    }

    private final static Map<String, DurationSerialization> UNITS;

    static {
        Map<String, DurationSerialization> units = new LinkedHashMap<>();
        units.put(ChronoUnit.NANOS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toNanos();
            }
        }, deserializer(ChronoUnit.NANOS)));
        units.put(ChronoUnit.MICROS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toNanos() / 1000;
            }
        }, deserializer(ChronoUnit.MICROS)));
        units.put(ChronoUnit.MILLIS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toMillis();
            }
        }, deserializer(ChronoUnit.MILLIS)));
        units.put(ChronoUnit.SECONDS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.getSeconds();
            }
        }, deserializer(ChronoUnit.SECONDS)));
        units.put(ChronoUnit.MINUTES.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toMinutes();
            }
        }, deserializer(ChronoUnit.MINUTES)));
        units.put(ChronoUnit.HOURS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toHours();
            }
        }, deserializer(ChronoUnit.HOURS)));
        units.put(ChronoUnit.HALF_DAYS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toHours() / 12;
            }
        }, deserializer(ChronoUnit.HALF_DAYS)));
        units.put(ChronoUnit.DAYS.name(), new DurationSerialization(new Function<Duration, Long>() {
            @Override
            public Long apply(Duration duration) {
                return duration.toDays();
            }
        }, deserializer(ChronoUnit.DAYS)));
        UNITS = units;
    }


    final DurationSerialization serialization;

    DurationUnitConverter(DurationSerialization serialization) {
        this.serialization = serialization;
    }

    public Duration convert(long value) {
        return serialization.deserializer.apply(value);
    }

    public long convert(Duration duration) {
        return serialization.serializer.apply(duration);
    }

    /**
     * @return Description of all allowed valued as a sequence of
     * double-quoted values separated by comma
     */
    public static String descForAllowed() {
        StringBuilder sb = new StringBuilder("\"");
        Iterator<String> iterator = UNITS.keySet().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append("\", \"");
            }
        }

        return sb.append("\"").toString();
    }

    public static DurationUnitConverter from(String unit) {
        DurationSerialization def = UNITS.get(unit);
        return (def == null) ? null : new DurationUnitConverter(def);
    }
}
