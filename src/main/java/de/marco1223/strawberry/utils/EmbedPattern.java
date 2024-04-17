package de.marco1223.strawberry.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class EmbedPattern {

    public static MessageEmbed info(@NotNull String title, @Nullable String description, @Nullable String url, @Nullable String iconUrl, @Nullable List<MessageEmbed.Field> fields, @Nullable String thumbnailUrl, @Nullable String imageUrl) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(ConfigUtil.DEFAULT_COLOR);
        embed.setFooter(ConfigUtil.tipp);
        embed.setTimestamp(new Date().toInstant());

        if(url == null) {
            url = ConfigUtil.DISCORD_URL;
        }

        if(description != null) {
            embed.setDescription(description);
        }

        if(iconUrl != null) {
            embed.setAuthor(title, url, iconUrl);
        } else {
            embed.setAuthor(title, url);
        }

        if(fields != null) {
            for(MessageEmbed.Field field : fields) {
                embed.addField(field);
            }
        }

        if(thumbnailUrl != null) {
            embed.setThumbnail(thumbnailUrl);
        }

        if(imageUrl != null) {
            embed.setImage(imageUrl);
        }

        return embed.build();

    }

    public static MessageEmbed error(@NotNull String title, @Nullable String description, @Nullable String url, @Nullable String iconUrl, @Nullable List<MessageEmbed.Field> fields, @Nullable String thumbnailUrl, @Nullable String imageUrl) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(ConfigUtil.DEFAULT_ERROR_COLOR);

        if(url == null) {
            url = ConfigUtil.DISCORD_URL;
        }

        if(description != null) {
            embed.setDescription(description);
        }

        if(iconUrl != null) {
            embed.setAuthor(title, url, iconUrl);
        } else {
            embed.setAuthor(title, url);
        }

        if(fields != null) {
            for(MessageEmbed.Field field : fields) {
                embed.addField(field);
            }
        }

        if(thumbnailUrl != null) {
            embed.setThumbnail(thumbnailUrl);
        }

        if(imageUrl != null) {
            embed.setImage(imageUrl);
        }

        return embed.build();

    }

}
