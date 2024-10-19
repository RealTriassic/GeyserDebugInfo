package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.AbstractCommand;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand(final GeyserDebugInfo instance) {
        super(instance, "reload", "Reloads the configuration file", false, false);
    }

    @Override
    protected void execute(@NotNull CommandSource source, @NotNull Command command, @NotNull String[] args) {
        if (instance.reloadConfig()) {
            source.sendMessage("§aGeyserDebugInfo configuration has been reloaded.");
        } else {
            source.sendMessage("§cFailed to reload GeyserDebugInfo configuration, check console for details.");
        }
    }
}
