package de.marco1223.strawberry.localizations.system;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class langCoammdLocalizations implements LocalizationFunction {
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
            case "lang.options.language.choices.german.name":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.langCommand.options.choices.german.name"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.langCommand.options.choices.german.name"));
                break;
            case "lang.options.language.choices.english_(us).name":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.langCommand.options.choices.english_us.name"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.langCommand.options.choices.english_us.name"));
                break;
        }
        return map;
    }
}
