package org.stupidcraft.geyserdebuginfo.command.commands;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.stupidcraft.geyserdebuginfo.GeyserDebugInfo;
import org.stupidcraft.geyserdebuginfo.command.BaseCommand;

public class ReloadCommand implements BaseCommand {

    private final GeyserDebugInfo instance;

    public ReloadCommand(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
    }

    @Override
    public Command createCommand() {
        return Command.builder(instance)
                .name("reload")
                .playerOnly(true)
                .bedrockOnly(true)
                .source(CommandSource.class)
                .description("Reloads the configuration file.")
                .executor(this::execute)
                .build();
    }

    private void execute(@NonNull CommandSource commandSource, @NonNull Command command, @NonNull String[] strings) {
        instance.reload();
        commandSource.sendMessage("Configuration reloaded successfully.");
    }
}
