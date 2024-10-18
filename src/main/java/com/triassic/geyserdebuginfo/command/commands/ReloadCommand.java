package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.AbstractCommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand(final GeyserDebugInfo instance) {
        super(instance, "reload", "Reloads the configuration file", false, false);
    }

    @Override
    protected void execute(@NonNull CommandSource source, @NonNull Command command, @NonNull String[] args) {
        if (instance.reloadConfig()) {
            source.sendMessage("§aGeyserDebugInfo configuration has been reloaded.");
        } else {
            source.sendMessage("§cFailed to reload GeyserDebugInfo configuration, check console for details.");
        }
    }
}
