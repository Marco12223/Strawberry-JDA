package de.marco1223.strawberry.listeners.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.music.QueueHandler;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DestoryLinkOnLeaveListener extends ListenerAdapter implements EventListener {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if(event.getMember().getIdLong() == event.getGuild().getSelfMember().getIdLong() && event.getChannelLeft() != null) {
            Link link = Strawberry.getLavalinkClient().getOrCreateLink(event.getGuild().getIdLong());
            LavalinkPlayer player = link.getPlayer().block();
            if(player != null) {
                QueueHandler queueHandler = new QueueHandler(event.getGuild().getIdLong());
                queueHandler.clearQueue();
                player.setTrack(null).setPaused(false).subscribe();
                link.getNode().destroyPlayerAndLink(event.getGuild().getIdLong()).subscribe();
            }
        }
    }
}
