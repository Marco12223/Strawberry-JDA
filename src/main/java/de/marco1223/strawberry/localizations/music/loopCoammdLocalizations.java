package de.marco1223.strawberry.localizations.music;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class loopCoammdLocalizations implements LocalizationFunction {
    @Override
    public Map<DiscordLocale, String> apply(String localizationKey) {
        Map<DiscordLocale, String> map = new HashMap<>();

        switch (localizationKey) {
            case "loop.description":
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.loopCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.loopCommand.description"));
                break;
        }
        return map;
    }
}
