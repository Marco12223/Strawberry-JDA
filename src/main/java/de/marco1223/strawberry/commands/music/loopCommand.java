package de.marco1223.strawberry.commands.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.music.loopCommandLocalizations;
import de.marco1223.strawberry.utils.EmbedPattern;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

public class loopCommand implements SlashCommandInterface {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());

        if(event.getGuild().getSelfMember().getVoiceState().getChannel() != null) {

            if(event.getMember().getVoiceState().getChannel() != null && event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() == event.getMember().getVoiceState().getChannel().getIdLong()) {

                LavalinkPlayer player = Strawberry.getLavalinkClient().getOrCreateLink(event.getGuild().getIdLong()).getPlayer().block();

                if(player.getTrack() != null) {

                    if(!Strawberry.repeat.containsKey(event.getGuild().getIdLong())) {
                        Strawberry.repeat.put(event.getGuild().getIdLong(), true);
                    } else {
                        Strawberry.repeat.put(event.getGuild().getIdLong(), !Strawberry.repeat.get(event.getGuild().getIdLong()));
                    }

                    event.replyEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.loopCommand.embed.success.title"), LanguageHandler.Language(lang, "values.loopCommand.embed.success.description").replace("{state}", Strawberry.repeat.get(event.getGuild().getIdLong()).toString()), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();

                } else {
                    event.replyEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.loopCommand.embed.errors.noTrack.title"), LanguageHandler.Language(lang, "values.loopCommand.embed.errors.noTrack.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
                }

            } else {
                event.replyEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.loopCommand.embed.errors.notInSameVoiceChannel.title"), LanguageHandler.Language(lang, "values.loopCommand.embed.errors.notInSameVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
            }
        } else {
            event.replyEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.loopCommand.embed.errors.noVoiceChannel.title"), LanguageHandler.Language(lang, "values.loopCommand.embed.errors.noVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
        }

    }

    @NotNull
    @Override
    public CommandData getCommandData() {
        LocalizationFunction localizations = new loopCommandLocalizations();

        return Commands.slash("loop", LanguageHandler.Language("en-US", "values.shuffleCommand.description"))
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);
    }
}
