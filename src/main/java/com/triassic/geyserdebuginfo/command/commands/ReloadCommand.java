package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.Command;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.CommandSource;

public class ReloadCommand implements Command {

    private final GeyserDebugInfo instance;

    public ReloadCommand(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
    }

    @Override
    public org.geysermc.geyser.api.command.Command createCommand() {
        return org.geysermc.geyser.api.command.Command.builder(instance)
                .name("reload")
                .playerOnly(false)
                .bedrockOnly(false)
                .source(CommandSource.class)
                .description("Reloads the configuration file.")
                .permission("f3.command.reload")
                .executor(this::execute)
                .build();
    }

    private void execute(@NonNull CommandSource commandSource, org.geysermc.geyser.api.command.Command command, @NonNull String[] strings) {
        if (instance.reloadConfig()) {
            commandSource.sendMessage("§aGeyserDebugInfo configuration has been reloaded.");
        } else {
            commandSource.sendMessage("§cFailed to reload GeyserDebugInfo configuration, check console for details.");
        }
    }
}
