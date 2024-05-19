package de.marco1223.strawberry.listeners.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.handlers.music.AudioHandler;
import de.marco1223.strawberry.handlers.music.QueueHandler;
import de.marco1223.strawberry.utils.EmbedPattern;
import de.marco1223.strawberry.utils.TimeUtils;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.event.EmittedEvent;
import dev.arbjerg.lavalink.client.event.ReadyEvent;
import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import dev.arbjerg.lavalink.protocol.v4.TrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.internal.utils.JDALogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AudioListeners {

    public static void registerLavalinkListeners() {
        LavalinkClient client = Strawberry.getLavalinkClient();
        ShardManager jda = Strawberry.getShardManager();

        client.on(ReadyEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            JDALogger.getLog(Strawberry.class).info("Node '{}' is ready", node.getName());

        });

        client.on(EmittedEvent.class).subscribe((event) -> {

            if(event instanceof TrackEndEvent) {

                try {
                    QueueHandler queueHandler = new QueueHandler(event.getGuildId());
                    LavalinkPlayer player = client.getOrCreateLink(event.getGuildId()).getPlayer().block();

                    if((Strawberry.repeat.containsKey(event.getGuildId()) && Strawberry.repeat.get(event.getGuildId()))) {
                        Link link = client.getOrCreateLink(event.getGuildId());
                        link.loadItem(Objects.requireNonNull(Strawberry.currentTrack.get(event.getGuildId()).getUri())).subscribe(new AudioHandler(link, null));
                    } else if ((Strawberry.queue.containsKey(event.getGuildId()) && !queueHandler.isQueueEmpty())) {

                        Link link = client.getOrCreateLink(event.getGuildId());
                        String lang = LanguageHandler.getGuildLocale(String.valueOf(event.getGuildId()));
                        TrackInfo trackInfo = null;

                        if((Strawberry.shuffle.containsKey(event.getGuildId()) && Strawberry.shuffle.get(event.getGuildId()))) {
                            Integer random = new Random().nextInt(queueHandler.getQueue().size());
                            link.loadItem(queueHandler.getTrackInfoByIndex(random).getInfo().getUri()).subscribe(new AudioHandler(link, null));
                            trackInfo = queueHandler.getTrackInfoByIndex(random).getInfo();
                            queueHandler.getQueue().remove(random);
                        } else {
                            link.loadItem(queueHandler.getNextTrack().getInfo().getUri()).subscribe(new AudioHandler(link, null));
                            trackInfo = queueHandler.getNextTrack().getInfo();
                            queueHandler.getQueue().remove(0);
                        }

                        Strawberry.currentTrack.put(event.getGuildId(), trackInfo);

                        List<MessageEmbed.Field> fields = new ArrayList<>();

                        fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.title"), "`" +  trackInfo.getTitle() + "`", true));
                        fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.artist"), "`" + trackInfo.getAuthor() + "`", true));
                        fields.add(new MessageEmbed.Field(LanguageHandler.Language(lang, "values.playCommand.embed.success.fields.duration"), "`" + TimeUtils.formatMillisToHMS(trackInfo.getLength()) + "`", true));

                        MessageEmbed embed = EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.success.title"), null, null, null, fields, null, trackInfo.getArtworkUrl());

                        if(Strawberry.panelMessage.containsKey(event.getGuildId())) {
                            Long channelId = Strawberry.panelMessage.get(event.getGuildId()).get("channel");
                            Long messageId = Strawberry.panelMessage.get(event.getGuildId()).get("message");
                            Guild guild = jda.getGuildById(event.getGuildId());

                            if(guild != null) {
                                try {
                                    guild.getTextChannelById(channelId).editMessageEmbedsById(messageId, embed).queue();
                                } catch (Exception e) {
                                    guild.getVoiceChannelById(channelId).editMessageEmbedsById(messageId, embed).queue();
                                }
                            }

                        }

                    } else {
                        queueHandler.clearQueue();
                        Guild guild = jda.getGuildById(event.getGuildId());
                        guild.getJDA().getDirectAudioController().disconnect(guild);

                        if(Strawberry.panelMessage.containsKey(event.getGuildId())) {
                            Long channelId = Strawberry.panelMessage.get(event.getGuildId()).get("channel");
                            Long messageId = Strawberry.panelMessage.get(event.getGuildId()).get("message");

                            if(guild != null) {
                                guild.getJDA().getDirectAudioController().disconnect(guild);
                                try {
                                    guild.getTextChannelById(channelId).deleteMessageById(messageId).queue();
                                } catch (Exception e) {
                                    guild.getVoiceChannelById(channelId).deleteMessageById(messageId).queue();
                                }

                            }

                            Strawberry.panelMessage.remove(event.getGuildId());

                        }


                        Link link = client.getOrCreateLink(event.getGuildId());
                        link.destroy().subscribe();
                        jda.getGuildById(event.getGuildId()).getJDA().getDirectAudioController().disconnect(jda.getGuildById(event.getGuildId()));
                    }
                } catch (Exception e2) {
                    QueueHandler queueHandler = new QueueHandler(event.getGuildId());
                    queueHandler.clearQueue();
                    Guild guild = jda.getGuildById(event.getGuildId());
                    guild.getJDA().getDirectAudioController().disconnect(guild);

                    if(Strawberry.panelMessage.containsKey(event.getGuildId())) {
                        Long channelId = Strawberry.panelMessage.get(event.getGuildId()).get("channel");
                        Long messageId = Strawberry.panelMessage.get(event.getGuildId()).get("message");

                        if(guild != null) {
                            guild.getJDA().getDirectAudioController().disconnect(guild);
                            try {
                                guild.getTextChannelById(channelId).deleteMessageById(messageId).queue();
                            } catch (Exception e) {
                                guild.getVoiceChannelById(channelId).deleteMessageById(messageId).queue();
                            }

                        }

                        Strawberry.panelMessage.remove(event.getGuildId());

                    }


                    Link link = client.getOrCreateLink(event.getGuildId());
                    link.destroy().subscribe();
                    jda.getGuildById(event.getGuildId()).getJDA().getDirectAudioController().disconnect(jda.getGuildById(event.getGuildId()));
                    JDALogger.getLog(AudioListeners.class).error("An error occurred while playing the next track", e2);
                }

            }

        });

    }

}
