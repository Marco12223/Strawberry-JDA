package de.marco1223.strawberry.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String formatMillisToHMS(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

}
