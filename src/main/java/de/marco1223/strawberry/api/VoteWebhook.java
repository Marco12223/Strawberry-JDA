package de.marco1223.strawberry.api;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.utils.ConfigUtil;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class VoteWebhook {

    @PostMapping("/voting")
    public ResponseEntity<String> handlePostRequest(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body) {

        if(Strawberry.getShardManager() == null) {
            return ResponseEntity.status(500).body("ShardManager is not initialized");
        }

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        if(authorizationHeader.equals(dotenv.get("TOPGG_TOKEN"))) {

            Strawberry.getShardManager().retrieveUserById(body.get("user")).submit().thenAccept(user -> {
                Guild guild = Strawberry.getShardManager().getGuildById("733493048750768210");
                NewsChannel channel = guild.getNewsChannelById("1040964560389161010");

                System.out.println("User " + user.getName() + " has voted for Strawberry on TOP.GG");

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(ConfigUtil.DEFAULT_COLOR);
                embed.setTimestamp(new Date().toInstant());
                embed.setThumbnail(user.getAvatarUrl());
                embed.setUrl("https://top.gg/bot/733483316002422804/vote");
                embed.setDescription("`" + user.getName() + "` has voted for **Strawberry** on TOP.GG.\nThank you for your support!");
                MessageEmbed messageEmbed = embed.build();

                channel.sendMessageEmbeds(messageEmbed).queue();

            });

            return ResponseEntity.ok("OK");

        } else {
            return ResponseEntity.badRequest().body("Invalid Token");
        }

    }

}
