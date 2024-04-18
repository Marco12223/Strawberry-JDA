package de.marco1223.strawberry;

import de.marco1223.strawberry.commands.music.*;
import de.marco1223.strawberry.commands.system.langCommand;
import de.marco1223.strawberry.commands.system.pingCommand;
import de.marco1223.strawberry.handlers.SlashCommandHandler;
import de.marco1223.strawberry.handlers.api.AuthHandler;
import de.marco1223.strawberry.handlers.api.GuildHandler;
import de.marco1223.strawberry.handlers.music.ButtonHandler;
import de.marco1223.strawberry.listeners.system.GuildJoinListener;
import de.marco1223.strawberry.listeners.system.GuildLeaveListener;
import de.marco1223.strawberry.tasks.LanguageLocaleUpdateTask;
import de.marco1223.strawberry.tasks.LanguageUpdateTask;
import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.loadbalancing.RegionGroup;
import dev.arbjerg.lavalink.client.player.Track;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import dev.arbjerg.lavalink.protocol.v4.TrackInfo;
import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.client.ApiException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;

import java.util.*;

import static de.marco1223.strawberry.listeners.music.AudioListeners.registerLavalinkListeners;

public class Strawberry {

    public static ShardManager shardManager;
    public static LavalinkClient lavalinkClient;
    private static final Timer timer = new Timer();
    public static final Map<Long, ArrayList<Track>> queue = new HashMap<>();
    public static final Map<Long, TrackInfo> currentTrack = new HashMap<>();
    public static final Map<Long, Boolean> repeat = new HashMap<>();
    public static final Map<Long, Map<String, Long>> panelMessage = new HashMap<>();
    public static final Map<Long, Integer> volume = new HashMap<>();
    public static final Map<Long, Boolean> shuffle = new HashMap<>();

    public static void main(String[] args) throws ApiException, InterruptedException {

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        AuthHandler.init(dotenv.get("API_USER"), dotenv.get("API_PASS"), dotenv.get("API_URL"));
        startTasks();

        while (!LanguageUpdateTask.isRunning) {
            Thread.sleep(1000);
            JDALogger.getLog(Strawberry.class).info("Waiting for LanguageUpdateTask to finish...");
        }

        lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(dotenv.get("TOKEN")));
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dotenv.get("TOKEN"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavalinkClient));
        builder.addEventListeners(registerCommands());
        builder.addEventListeners(registerEvents());
        shardManager = builder.build();

        while (!(shardManager.getStatus(shardManager.getShardsTotal()-1) == JDA.Status.CONNECTED)) {
            Thread.sleep(3000);
        }

        JDALogger.getLog(Strawberry.class).info("-".repeat(20) + "Bot Information" + "-".repeat(20));
        JDALogger.getLog(Strawberry.class).info("Java version: " + System.getProperty("java.version"));
        JDALogger.getLog(Strawberry.class).info("JDA version: " + JDAInfo.VERSION);
        JDALogger.getLog(Strawberry.class).info("Shard latency: " + shardManager.getAverageGatewayPing() + "ms");
        JDALogger.getLog(Strawberry.class).info("Shard count: " + shardManager.getShardsTotal());
        JDALogger.getLog(Strawberry.class).info("Guild count: " + shardManager.getGuilds().size());
        JDALogger.getLog(Strawberry.class).info("User count: " + shardManager.getGuilds().stream().mapToInt(Guild::getMemberCount).sum());
        JDALogger.getLog(Strawberry.class).info("-".repeat(54));

        for(Guild guild : shardManager.getGuilds()) {
            if (!GuildHandler.guildExists(guild.getId())) {
                GuildHandler.createGuild(guild);
            }
        }

        registerLavalinkNodes(lavalinkClient);
        registerLavalinkListeners();

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
                new langCommand(),
                new playCommand(),
                new stopCommand(),
                new skipCommand(),
                new loopCommand(),
                new shuffleCommand(),
                new pingCommand()
        );

        return slashCommandHandler;

    }

    private static List<Object> registerEvents() {
        List<Object> listeners = new ArrayList<>();
        listeners.add(new ButtonHandler());
        listeners.add(new GuildLeaveListener());
        listeners.add(new GuildJoinListener());

        return listeners;
    }

    private static void startTasks() {
        timer.schedule(new LanguageLocaleUpdateTask(), 0, 1000 * 60 * 15);
        timer.schedule(new LanguageUpdateTask(), 0, 1000 * 60 * 30);
    }

    private static void registerLavalinkNodes(LavalinkClient client) {
        JDALogger.getLog("Lavalink Nodes").info("Registering Lavalink nodes...");
        List.of(
                client.addNode(
                        new NodeOptions.Builder()
                                .setName("Germany #1")
                                .setServerUri("ws://45.81.233.126:2333")
                                .setPassword("4KM5oWQT")
                                .setRegionFilter(RegionGroup.EUROPE)
                                .build()
                )
        );
    }

}