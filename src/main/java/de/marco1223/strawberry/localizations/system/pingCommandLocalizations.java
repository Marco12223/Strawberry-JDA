package de.marco1223.strawberry.localizations.system;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class pingCommandLocalizations implements LocalizationFunction {
    @Override
    public Map<DiscordLocale, String> apply(String localizationKey) {
        Map<DiscordLocale, String> map = new HashMap<>();

        switch (localizationKey) {
            case "ping.description":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.pingCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.pingCommand.description"));
                break;
        }
        return map;
    }
}
