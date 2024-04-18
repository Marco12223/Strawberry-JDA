package de.marco1223.strawberry.localizations.music;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class playCoammdLocalizations implements LocalizationFunction {
    @Override
    public Map<DiscordLocale, String> apply(String localizationKey) {
        Map<DiscordLocale, String> map = new HashMap<>();

        switch (localizationKey) {
            case "lang.description":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.langCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.langCommand.description"));
                break;
            case "lang.options.language.description":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.langCommand.options.language.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.langCommand.options.language.description"));
                break;
        }
        return map;
    }
}
