package de.marco1223.strawberry.localizations.music;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class shuffleCommandLocalizations implements LocalizationFunction {
    @Override
    public Map<DiscordLocale, String> apply(String localizationKey) {
        Map<DiscordLocale, String> map = new HashMap<>();

        switch (localizationKey) {
            case "shuffle.description":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.shuffleCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.shuffleCommand.description"));
                break;
        }
        return map;
    }
}
