package de.marco1223.strawberry.interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public interface SlashCommandInterface {

    void execute(@NotNull SlashCommandInteractionEvent event);

    @NotNull
    CommandData getCommandData();
}