package de.marco1223.strawberry;

import de.marco1223.strawberry.commands.music.playCommand;
import de.marco1223.strawberry.handlers.SlashCommandHandler;
import de.marco1223.strawberry.handlers.api.AuthHandler;
import de.marco1223.strawberry.tasks.LanguageLocaleUpdateTask;
import de.marco1223.strawberry.tasks.LanguageUpdateTask;
import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.client.ApiException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Timer;

public class Strawberry {

    public static ShardManager shardManager;
    public static LavalinkClient lavalinkClient;
    private static final Timer timer = new Timer();

    public static void main(String[] args) throws ApiException {

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        AuthHandler.init(dotenv.get("API_USER"), dotenv.get("API_PASS"), dotenv.get("API_URL"));
        startTasks();
        lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(dotenv.get("TOKEN")));
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dotenv.get("TOKEN"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavalinkClient));
        builder.addEventListeners(registerCommands());
        shardManager = builder.build();

    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

    public static LavalinkClient getLavalinkClient() {
        return lavalinkClient;
    }

    private static SlashCommandHandler registerCommands() {

        final var slashCommandHandler = new SlashCommandHandler();
        slashCommandHandler.addCommands(
                new playCommand()
        );

        return slashCommandHandler;

    }

    private static void startTasks() {
        timer.schedule(new LanguageLocaleUpdateTask(), 0, 1000 * 60 * 15);
        timer.schedule(new LanguageUpdateTask(), 0, 1000 * 60 * 30);
    }

}