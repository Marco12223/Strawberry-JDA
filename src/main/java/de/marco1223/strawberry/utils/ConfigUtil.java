package de.marco1223.strawberry.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ConfigUtil {

    private static Random r = new Random();
    public static final String DISCORD_URL = "https://discord.gg/82y7BRnArr";
    private static final List<String> tipps = new ArrayList<>(Arrays.asList(
            "Tipp 1",
            "Tipp 2",
            "Tipp 3",
            "Tipp 4",
            "Tipp 5",
            "Tipp 6",
            "Tipp 7",
            "Tipp 8",
            "Tipp 9",
            "Tipp 10"
    ));

    public static String DEFAULT_LOCALE = "de";
    public static Color DEFAULT_COLOR = Color.decode("0xeb5762");
    public static Color DEFAULT_ERROR_COLOR = Color.decode("0xC0392B");
    public static String tipp = tipps.get(r.nextInt(tipps.size()));

}
