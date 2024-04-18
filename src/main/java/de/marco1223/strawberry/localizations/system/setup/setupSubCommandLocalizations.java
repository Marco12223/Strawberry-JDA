package de.marco1223.strawberry.localizations.system.setup;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;

import java.util.HashMap;
import java.util.Map;

public class setupSubCommandLocalizations implements LocalizationFunction {
    @Override
    public Map<DiscordLocale, String> apply(String localizationKey) {
        Map<DiscordLocale, String> map = new HashMap<>();

        switch (localizationKey) {

            // setup groupd
            case "setup.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.description"));
            }
            // logs
            case "setup.logs.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.description"));
            }
            // logs add
            case "setup.logs.add.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.commands.add.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.commands.add.description"));
            }
            case "setup.logs.add.options.channel.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.commands.add.options.channel.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.commands.add.options.channel.description"));
            }
            case "setup.logs.add.options.type.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.commands.add.options.type.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.commands.add.options.type.description"));
            }
            // logs remove
            case "setup.logs.remove.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.commands.remove.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.commands.remove.description"));
            }
            case "setup.logs.remove.options.type.description" -> {
                map.put(DiscordLocale.GERMAN, LanguageHandler.Language("de", "values.setupSubCommand.groups.logsCommand.commands.remove.options.type.description"));
                map.put(DiscordLocale.ENGLISH_US, LanguageHandler.Language("en-US", "values.setupSubCommand.groups.logsCommand.commands.remove.options.type.description"));
            }
        }
        return map;
    }
}
