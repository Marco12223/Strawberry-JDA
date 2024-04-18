package de.marco1223.strawberry.commands.system;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.system.pingCommandLocalizations;
import de.marco1223.strawberry.utils.EmbedPattern;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class pingCommand implements SlashCommandInterface {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());

        List<MessageEmbed.Field> fields = List.of(
                new MessageEmbed.Field(LanguageHandler.Language(lang, "values.pingCommand.embed.fields.ws"), event.getJDA().getGatewayPing() + "ms", true),
                new MessageEmbed.Field(LanguageHandler.Language(lang, "values.pingCommand.embed.fields.rest"), event.getJDA().getRestPing().complete() + "ms", true)
        );

        event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.pingCommand.embed.title").replace("{shardId}", event.getJDA().getShardInfo().getShardId()+1 + "/" + event.getJDA().getShardInfo().getShardTotal()), LanguageHandler.Language(lang, "values.pingCommand.embed.description"), null, event.getUser().getAvatarUrl(), fields, null, null)).queue();

    }

    @NotNull
    @Override
    public CommandData getCommandData() {
        LocalizationFunction localizations = new pingCommandLocalizations();

        return Commands.slash("ping", LanguageHandler.Language("en-US", "values.pingCommand.description"))
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);
    }
}
