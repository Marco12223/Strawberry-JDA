package de.marco1223.strawberry.commands.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.handlers.music.QueueHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.music.skipCoammdLocalizations;
import de.marco1223.strawberry.utils.EmbedPattern;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

public class skipCommand implements SlashCommandInterface {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

        event.deferReply(true).queue();
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());

        if(event.getGuild().getSelfMember().getVoiceState().getChannel() != null) {

            if(event.getMember().getVoiceState().getChannel() != null && event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() == event.getMember().getVoiceState().getChannel().getIdLong()) {

                LavalinkPlayer player = Strawberry.getLavalinkClient().getOrCreateLink(event.getGuild().getIdLong()).getPlayer().block();
                QueueHandler queueHandler = new QueueHandler(event.getGuild().getIdLong());

                if(queueHandler.isQueueEmpty()) {
                    if(player.getTrack() != null) {

                        player.stopTrack().subscribe();
                        player.setTrack(null).subscribe();

                        event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.skipCommand.embed.success.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.success.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();

                    } else {
                        event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noTrack.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noTrack.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();
                    }

                } else {
                    event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noMoreSongs.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noMoreSongs.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();
                }

            } else {
                event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameVoiceChannel.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();
            }

        } else {
            event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noVoiceChannel.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();
        }

    }

    @NotNull
    @Override
    public CommandData getCommandData() {

        LocalizationFunction localizations = new skipCoammdLocalizations();

        return Commands.slash("skip", LanguageHandler.Language("en-US", "values.skipCommand.description"))
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);

    }
}
