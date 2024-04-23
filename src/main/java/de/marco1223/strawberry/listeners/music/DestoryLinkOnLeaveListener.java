package de.marco1223.strawberry.listeners.music;

import de.marco1223.strawberry.Strawberry;
import dev.arbjerg.lavalink.client.Link;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DestoryLinkOnLeaveListener extends ListenerAdapter implements EventListener {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if(event.getMember().getIdLong() == event.getGuild().getSelfMember().getIdLong() && event.getChannelLeft() != null) {
            Link link = Strawberry.getLavalinkClient().getOrCreateLink(event.getGuild().getIdLong());
            link.getNode().destroyPlayerAndLink(link.getGuildId());
        }
    }
}
