package com.fasterxml.jackson.datatype.threetenbp;

import org.threeten.bp.ZoneId;
import sun.util.calendar.ZoneInfoFile;

import java.util.TimeZone;

public class DateTimeUtils {
    private DateTimeUtils() {
    }

    /**
     * Converts this {@code TimeZone} object to a {@code ZoneId}.
     *
     * @return a {@code ZoneId} representing the same time zone as this
     * {@code TimeZone}
     * @since 1.8
     */
    public static ZoneId timeZoneToZoneId(final TimeZone timeZone) {
        final String id = timeZone.getID();
        if (ZoneInfoFile.useOldMapping() && id.length() == 3) {
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
