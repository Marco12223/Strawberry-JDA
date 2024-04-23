package de.marco1223.strawberry.handlers.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.utils.EmbedPattern;
import de.marco1223.strawberry.utils.TimeUtils;
import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.*;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AudioHandler extends AbstractAudioLoadResultHandler  {

    private final Link link;
    private final SlashCommandInteractionEvent event;
    private Integer volume = 30;

    public AudioHandler(Link link, @Nullable SlashCommandInteractionEvent event) {
        this.link = link;
        this.event = event;

        try {
            if(Strawberry.volume.containsKey(link.getGuildId())) {
                this.volume = Strawberry.volume.get(link.getGuildId());
            }
        } catch (Exception e) {
            Strawberry.volume.put(link.getGuildId(), this.volume);
        }

    }

    @Override
    public void loadFailed(@NotNull LoadFailed loadFailed) {
        if(event != null) {
            String lang = LanguageHandler.getGuildLocale(String.valueOf(link.getGuildId()));
            event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.playCommand.embed.errors.loadFailed.title"), LanguageHandler.Language(lang, "values.playCommand.embed.errors.loadFailed.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
        }
        JDALogger.getLog("AudioHandler").error("Failed to load track: " + loadFailed.getException());
    }

    @Override
    public void noMatches() {
        if(event != null) {
            String lang = LanguageHandler.getGuildLocale(String.valueOf(link.getGuildId()));
            event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.playCommand.embed.errors.noResults.title"), LanguageHandler.Language(lang, "values.playCommand.embed.errors.noResults.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
        }
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded playlistLoaded) {

        QueueHandler queueHandler = new QueueHandler(link.getGuildId());
        LavalinkPlayer player = link.getPlayer().block();

        playlistLoaded.getTracks().forEach(track -> {
            Boolean isQueueFull = queueHandler.addQueue(track);
        });

        if(player.getTrack() == null) {
            
            playSong(playlistLoaded.getTracks().get(0), playlistLoaded.getTracks().size());
            queueHandler.removeTrackByIndex(0);
            
        } else {
            String lang = LanguageHandler.getGuildLocale(String.valueOf(link.getGuildId()));
            event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.playlist.title"), LanguageHandler.Language(lang, "values.playCommand.embed.playlist.description").replace("{size}", String.valueOf(playlistLoaded.getTracks().size())).replace("{name}", playlistLoaded.getInfo().getName()), null, event.getUser().getAvatarUrl(), null, null, playlistLoaded.getTracks().get(0).getInfo().getArtworkUrl())).queue();
        }

    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult searchResult) {

        playSong(searchResult.getTracks().get(0), null);

    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded trackLoaded) {

        playSong(trackLoaded.getTrack(), null);

    }

    private void playSong(Track track, @Nullable Integer playlistSize) {
        LavalinkPlayer player = link.getPlayer().block();

        if(player.getTrack() == null) {

            link.createOrUpdatePlayer()
                    .setTrack(track)
                    .setVolume(volume)
                    .setPaused(false)
                    .subscribe(updatedPlayer -> {

                        if(event != null) {

                            Track playingTrack = updatedPlayer.getTrack();
                            Strawberry.currentTrack.put(link.getGuildId(), playingTrack.getInfo());
                            String lang = LanguageHandler.getGuildLocale(String.valueOf(link.getGuildId()));

                            StringSelectMenu.Builder builder = StringSelectMenu.create("volumeSelectMenu");
                            builder.addOption("10% Volume", "10");
                            builder.addOption("20% Volume", "20");
                            builder.addOption("30% Volume", "30");
                            builder.addOption("40% Volume", "40");
                            builder.addOption("50% Volume", "50");
                            builder.addOption("60% Volume", "60");
                            builder.addOption("70% Volume", "70");
                            builder.addOption("80% Volume", "80");
                            builder.addOption("90% Volume", "90");
                            builder.addOption("100% Volume", "100");
                            builder.setDefaultValues(this.volume.toString());
                            StringSelectMenu menu = builder.build();

                            ActionRow buttonsRow = ActionRow.of(
                                    Button.secondary("switchBack", "<<").asDisabled(),
                                    Button.primary("skipButton", "SKIP"),
                                    Button.danger("pauseButton", "PAUSE"),
                                    Button.primary("loop", "LOOP"),
                                    Button.secondary("switchForward", ">>").asDisabled()
                            );

                            ActionRow menuRow = ActionRow.of(menu);

                            List<MessageEmbed.Field> fields = new ArrayList<>();

                            fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.title"), "`" +  playingTrack.getInfo().getTitle() + "`", true));
                            fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.artist"), "`" + playingTrack.getInfo().getAuthor() + "`", true));
                            fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.duration"), "`" + TimeUtils.formatMillisToHMS(playingTrack.getInfo().getLength()) + "`", true));

                            if(playlistSize != null) {
                                event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.success.title"), LanguageHandler.Language(lang, "values.playCommand.embed.success.description").replace("{size}", playlistSize.toString()), null, event.getUser().getAvatarUrl(), fields, null, playingTrack.getInfo().getArtworkUrl()))
                                        .addComponents(buttonsRow, menuRow)
                                        .queue(message -> {
                                            Strawberry.panelMessage.put(event.getGuild().getIdLong(), Map.of("channel", message.getChannelIdLong(), "message", message.getIdLong()));
                                        });
                            } else {
                                event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.success.title"), null, null, event.getUser().getAvatarUrl(), fields, null, playingTrack.getInfo().getArtworkUrl()))
                                        .addComponents(buttonsRow, menuRow)
                                        .queue(message -> {
                                            Strawberry.panelMessage.put(event.getGuild().getIdLong(), Map.of("channel", message.getChannelIdLong(), "message", message.getIdLong()));
                                            System.out.println(message.getIdLong());
                                        });
                            }

                        }

                    });

        } else {
            QueueHandler queueHandler = new QueueHandler(link.getGuildId());
            String lang = LanguageHandler.getGuildLocale(String.valueOf(link.getGuildId()));
            queueHandler.addQueue(track);

            if(!queueHandler.isQueueEmpty()) {
                event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.queue.title"), LanguageHandler.Language(lang, "values.playCommand.embed.queue.description").replace("{title}", track.getInfo().getTitle()).replace("{artist}", track.getInfo().getAuthor()), null, event.getUser().getAvatarUrl(), null, null, track.getInfo().getArtworkUrl())).setEphemeral(true).queue();
            } else {
                event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.errors.queueFull.title"), LanguageHandler.Language(lang, "values.playCommand.embed.errors.queueFull.description"), null, event.getUser().getAvatarUrl(), null, null, track.getInfo().getArtworkUrl())).setEphemeral(true).queue();
            }

        }

    }

}
