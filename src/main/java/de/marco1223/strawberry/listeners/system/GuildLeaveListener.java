package de.marco1223.strawberry.listeners.system;

import de.marco1223.strawberry.handlers.api.GuildHandler;
import io.swagger.client.ApiException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.util.Date;

public class GuildLeaveListener extends ListenerAdapter {

    public void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        Date date = new Date();
        User ownerUser = event.getJDA().getUserById(event.getGuild().getOwnerId());
        TextChannel channel = event.getJDA().getShardManager().getTextChannelById("1013333660981788683");

        if (channel != null) {

            try {
                if (GuildHandler.guildExists(guild.getId())) {
                    GuildHandler.deleteGuild(guild.getId());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Server Left");
            embedBuilder.addField("Server Name", guild.getName(), false);
            embedBuilder.addField("Server ID", guild.getId(), false);
            if (ownerUser != null) {
                embedBuilder.addField("Server Owner", ownerUser.getName(), false);
            }
            embedBuilder.addField("Server Member Count", String.valueOf(guild.getMemberCount()), false);
            embedBuilder.addField("Server Language (Community function)", guild.getLocale().getLanguageName(), false);
            embedBuilder.addField("Server Creation Date", TimeFormat.DATE_TIME_LONG.format(guild.getTimeCreated()), false);
            embedBuilder.addField("Server Join Date", TimeFormat.DATE_TIME_LONG.format(guild.getSelfMember().getTimeJoined()), false);
            embedBuilder.addField("Server Boost Count", String.valueOf(guild.getBoostCount()), false);
            if (guild.getIconUrl() != null) {
                embedBuilder.setThumbnail(guild.getIconUrl());
            }
            embedBuilder.setColor(0xC0392B);
            embedBuilder.setTimestamp(date.toInstant());

            channel.sendMessageEmbeds(embedBuilder.build()).queue();

        }

    }

}
