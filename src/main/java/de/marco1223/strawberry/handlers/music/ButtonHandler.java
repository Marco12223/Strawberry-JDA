package de.marco1223.strawberry.handlers.music;

import de.marco1223.strawberry.Strawberry;
import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.utils.ConfigUtil;
import de.marco1223.strawberry.utils.EmbedPattern;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.Objects;

public class ButtonHandler extends ListenerAdapter implements EventListener {

    private final LavalinkClient client;

    public ButtonHandler() {
        this.client = Strawberry.getLavalinkClient();
    }

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        event.deferReply(true).queue();
        if (event.getComponentId().equals("volumeSelectMenu")) {
            String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());

            if(event.getGuild().getSelfMember().getVoiceState().getChannel() != null && event.getMember().getVoiceState().getChannel() != null && event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() == event.getMember().getVoiceState().getChannel().getIdLong()) {
                Link link = client.getOrCreateLink(event.getGuild().getIdLong());
                LavalinkPlayer player = link.getPlayer().block();

                if(player != null) {
                    player.setVolume(Integer.parseInt(event.getSelectedOptions().get(0).getValue())).subscribe();
                    Strawberry.volume.put(event.getGuild().getIdLong(), Integer.parseInt(event.getSelectedOptions().get(0).getValue()));

                    event.getHook().sendMessageEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.volumeChange.title"), LanguageHandler.Language(lang, "values.playCommand.embed.volumeChange.description").replace("{volume}", event.getSelectedOptions().get(0).getValue()), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();

                }

            } else {

                event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();

            }

        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {

        Link link = client.getOrCreateLink(event.getGuild().getIdLong());
        LavalinkPlayer player = link.getPlayer().block();
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());

        switch (event.getComponentId()) {
            case "pauseButton" -> {
                if(event.getGuild().getSelfMember().getVoiceState().getChannel() != null && event.getMember().getVoiceState().getChannel() != null && event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() == event.getMember().getVoiceState().getChannel().getIdLong()) {
                    player.setPaused(!player.getPaused()).subscribe();
                    if (player.getPaused()) {
                        event.editButton(event.getButton().withLabel("PAUSE").withStyle(ButtonStyle.DANGER)).queue();
                    } else {
                        event.editButton(event.getButton().withLabel("RESUME").withStyle(ButtonStyle.SUCCESS)).queue();
                    }
                } else {
                    event.getHook().sendMessageEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
                }
            }
            case "skipButton" -> {

                if (event.getGuild().getSelfMember().getVoiceState().getChannel() != null && event.getMember().getVoiceState().getChannel() != null && event.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() == event.getMember().getVoiceState().getChannel().getIdLong()) {
                    QueueHandler queueHandler = new QueueHandler(event.getGuild().getIdLong());

                    if (queueHandler.isQueueEmpty()) {

                        event.replyEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noMoreSongs.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.noMoreSongs.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();

                    } else {

                        if(player != null) {

                            link.updatePlayer(
                                    (update) -> update.setTrack(null).setPaused(false)
                            ).subscribe();

                        }

                        event.editButton(event.getButton().withLabel("SKIP").withStyle(ButtonStyle.PRIMARY)).queue();

                    }

                } else {
                    event.replyEmbeds(EmbedPattern.error(LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.title"), LanguageHandler.Language(lang, "values.skipCommand.embed.errors.notInSameChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).setEphemeral(true).queue();
                }

            }
            case "loop" -> {

                try {
                    if(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel() != null && Objects.requireNonNull(event.getMember().getVoiceState().getChannel()).getIdLong() == Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState().getChannel()).getIdLong()) {
                        try {
                            try {
                                if (Strawberry.repeat.get(event.getGuild().getIdLong())) {
                                    Strawberry.repeat.put(event.getGuild().getIdLong(), false);
                                } else {
                                    Strawberry.repeat.put(event.getGuild().getIdLong(), true);
                                }
                            } catch (Exception e) {
                                Strawberry.repeat.put(event.getGuild().getIdLong(), true);
                            }

                            if (Strawberry.repeat.get(event.getGuild().getIdLong())) {
                                event.editButton(event.getButton().withLabel("LOOP").withStyle(ButtonStyle.DANGER)).queue();
                            } else {
                                event.editButton(event.getButton().withLabel("LOOP").withStyle(ButtonStyle.PRIMARY)).queue();
                            }

                            Strawberry.currentTrack.put(event.getGuild().getIdLong(), player.getTrack().getInfo());

                        } catch (Exception e) {
                            Strawberry.repeat.put(event.getGuild().getIdLong(), false);
                        }

                    } else {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setAuthor(LanguageHandler.Language(lang, "values.loopCommand.embed.errors.notInSameChannel.title"), ConfigUtil.DISCORD_URL, event.getUser().getAvatarUrl());
                        embed.setDescription(LanguageHandler.Language(lang, "values.loopCommand.embed.errors.notInSameChannel.description"));
                        embed.setColor(ConfigUtil.DEFAULT_ERROR_COLOR);
                        embed.setFooter(ConfigUtil.tipp);
                        embed.setTimestamp(Objects.requireNonNull(event.getTimeCreated()));
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                    }
                } catch (Exception e) {
                    // Do nothing
                }

            }
        }

    }

}
