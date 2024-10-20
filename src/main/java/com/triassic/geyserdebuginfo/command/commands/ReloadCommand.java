package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.AbstractCommand;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.api.extension.ExtensionLogger;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends AbstractCommand {

    private final ExtensionLogger logger;

    public ReloadCommand(final GeyserDebugInfo instance) {
        super(instance, "reload", "Reloads the configuration file", false, false);
        this.logger = instance.getLogger();
    }

    @Override
    protected void execute(@NotNull CommandSource source, @NotNull Command command, @NotNull String[] args) {
        instance.getConfig().reload().handleAsync((v, ex) -> {
            if (ex == null) {
                source.sendMessage("§aGeyserDebugInfo configuration has been reloaded.");
            } else {
                source.sendMessage("§cFailed to reload GeyserDebugInfo configuration, check console for details.");
                logger.error(ex.getMessage(), ex.getCause());
            }
            return null;
        });
    }
}
