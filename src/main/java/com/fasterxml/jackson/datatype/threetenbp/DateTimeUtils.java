package com.fasterxml.jackson.datatype.threetenbp;

import org.threeten.bp.ZoneId;

import java.util.TimeZone;

public class DateTimeUtils {
    private DateTimeUtils() {
    }

    /**
     * Converts this {@code TimeZone} object to a {@code org.threeten.bp.ZoneId}.
     *
     * @return a {@code org.threeten.bp.ZoneId} representing the same time zone as this {@code TimeZone}
     * @since 2.4.1
     */
    public static ZoneId timeZoneToZoneId(final TimeZone timeZone) {
        final String id = timeZone.getID();
        if (id.length() == 3) {
            if ("EST".equals(id))
                return ZoneId.of("America/New_York");
            if ("MST".equals(id))
                return ZoneId.of("America/Denver");
            if ("HST".equals(id))
                return ZoneId.of("America/Honolulu");
        }
        return ZoneId.of(id, ZoneId.SHORT_IDS);
    }
}
