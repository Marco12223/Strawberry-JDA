package de.marco1223.strawberry.commands.music;

import de.marco1223.strawberry.handlers.api.LanguageHandler;
import de.marco1223.strawberry.interfaces.SlashCommandInterface;
import de.marco1223.strawberry.localizations.music.playCoammdLocalizations;
import de.marco1223.strawberry.utils.EmbedPattern;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import org.jetbrains.annotations.NotNull;

public class playCommand implements SlashCommandInterface {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String lang = LanguageHandler.getGuildLocale(event.getGuild().getId());
        if (event.getMember().getVoiceState().inAudioChannel()) {
            event.getJDA().getDirectAudioController().connect(event.getMember().getVoiceState().getChannel());



        } else {
            event.replyEmbeds(EmbedPattern.info(LanguageHandler.Language(lang, "values.playCommand.embed.errors.noVoiceChannel.title"), LanguageHandler.Language(lang, "values.playCommand.embed.errors.noVoiceChannel.description"), null, event.getUser().getAvatarUrl(), null, null, null)).queue();
        }
    }

    @NotNull
    @Override
    public CommandData getCommandData() {

        LocalizationFunction localizations = new playCoammdLocalizations();

        return Commands.slash("play", LanguageHandler.Language("en-US", "values.playCommand.description"))
                .addOption(OptionType.STRING,"query", LanguageHandler.Language("en-US", "values.playCommand.options.query.description"),true)
                .setGuildOnly(true)
                .setLocalizationFunction(localizations);
    }
}
