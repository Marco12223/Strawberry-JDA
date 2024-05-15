package de.marco1223.strawberry.commands.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.handlers.music.AudioHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.music.playCoammdLocalizations;
import de.marco1223.strawberry.utils.EmbedPattern;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class playCommand implements SlashCommandInterface {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());
        String identifier = event.getOption("query").getAsString();
        event.deferReply(false).queue();

        if (!event.getMember().getVoiceState().inAudioChannel()) {
            event.replyEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.errors.noVoiceChannel.title"), LanguageHandler.Language(lang, "values.playCommand.embed.errors.noVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();

        } else {
            event.getJDA().getDirectAudioController().connect(event.getMember().getVoiceState().getChannel());

            LavalinkClient client = Strawberry.getLavalinkClient();
            Link link = client.getOrCreateLink(event.getGuild().getIdLong());
            LavalinkPlayer player = link.getPlayer().block();

            if(player.getTrack() == null) {

                if(Strawberry.panelMessage.containsKey(event.getGuild().getIdLong())) {
                    Long channelId = Strawberry.panelMessage.get(event.getGuild().getIdLong()).get("channel");
                    Long messageId = Strawberry.panelMessage.get(event.getGuild().getIdLong()).get("message");

                    try {
                        event.getJDA().getShardManager().getGuildById(event.getGuild().getId()).getTextChannelById(channelId).deleteMessageById(messageId).queue();
                    } catch (Exception e) {
                        event.getJDA().getShardManager().getGuildById(event.getGuild().getId()).getVoiceChannelById(channelId).deleteMessageById(messageId).queue();
                    }

                    Strawberry.panelMessage.remove(event.getGuild().getIdLong());

                }

            }

            if (isValidURL(identifier)) {
                link.loadItem(identifier).subscribe(new AudioHandler(link, event));
            } else {
                link.loadItem("dzsearch:" + identifier).subscribe(new AudioHandler(link, event));
            }

        }

    }

    @NotNull
    @Override
    public CommandData getCommandData() {

        LocalizationFunction localizations = new playCoammdLocalizations();

        return Commands.slash("play", LanguageHandler.Language("en-US", "values.playCommand.description"))
                .addOption(OptionType.STRING,"query", LanguageHandler.Language("en-US", "values.playCommand.options.query.description"),true)
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

}
