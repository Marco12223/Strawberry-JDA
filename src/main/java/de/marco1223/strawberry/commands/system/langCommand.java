package de.marco1223.strawberry.commands.system;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.system.langCoammdLocalizations;
import de.marco1223.strawberry.utils.ConfigUtil;
import io.swagger.client.ApiException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class langCommand implements SlashCommandInterface {

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

        event.deferReply(true).queue();

        Member member = event.getMember();
        if (member.hasPermission(Permission.ADMINISTRATOR) || member.hasPermission(Permission.MANAGE_SERVER)) {
            try {

                String lang = Objects.requireNonNull(event.getOption("language")).getAsString();
                if (LanguageHandler.isLocaleAvailable(lang)) {

                    LanguageHandler.updateGuildLanguage(Objects.requireNonNull(event.getGuild()).getId(), lang);

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor(LanguageHandler.Language(lang, "values.langCommand.embed.success.title"), ConfigUtil.DISCORD_URL, event.getUser().getAvatarUrl());
                    embed.setDescription(LanguageHandler.Language(lang, "values.langCommand.embed.success.description").replace("{locale}", LanguageHandler.Language(lang, "name")));
                    embed.setColor(ConfigUtil.DEFAULT_COLOR);
                    embed.setTimestamp(Objects.requireNonNull(event.getTimeCreated()));
                    embed.setFooter(ConfigUtil.tipp);
                    event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();

                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setAuthor(LanguageHandler.Language(lang, "values.langCommand.embed.errors.invalidLang.title"), ConfigUtil.DISCORD_URL, event.getUser().getAvatarUrl());
                    embed.setDescription(LanguageHandler.Language(lang, "values.langCommand.embed.errors.invalidLang.description"));
                    embed.setColor(ConfigUtil.DEFAULT_COLOR);
                    embed.setTimestamp(Objects.requireNonNull(event.getTimeCreated()));
                    embed.setFooter(ConfigUtil.tipp);
                    event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
                }

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            String lang = LanguageHandler.getGuildLocale(Objects.requireNonNull(event.getGuild()).getId());
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(LanguageHandler.Language(lang, "values.langCommand.embed.errors.invalidLang.title"), ConfigUtil.DISCORD_URL, event.getUser().getAvatarUrl());
            embed.setDescription(LanguageHandler.Language(lang, "values.langCommand.embed.errors.invalidLang.description"));
            embed.setColor(ConfigUtil.DEFAULT_COLOR);
            embed.setTimestamp(Objects.requireNonNull(event.getTimeCreated()));
            embed.setFooter(ConfigUtil.tipp);
            event.getHook().sendMessageEmbeds(embed.build()).setEphemeral(true).queue();
        }

    }

    @NotNull
    @Override
    public CommandData getCommandData() {
        LocalizationFunction localizations = new langCoammdLocalizations();

        return Commands.slash("lang", LanguageHandler.Language("en-US", "values.langCommand.description"))
                .addOptions(
                        new OptionData(OptionType.STRING, "language", LanguageHandler.Language("en-US", "values.langCommand.options.language.description"), true)
                                .addChoice(LanguageHandler.Language("en-US", "values.langCommand.options.choices.german.name"), "de")
                                .addChoice(LanguageHandler.Language("en-US", "values.langCommand.options.choices.english_us.name"), "en-US"))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);

    }

}
