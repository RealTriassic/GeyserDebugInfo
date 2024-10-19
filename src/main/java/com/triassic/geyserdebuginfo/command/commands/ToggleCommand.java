package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.AbstractCommand;
import com.triassic.geyserdebuginfo.display.DisplayType;
import com.triassic.geyserdebuginfo.display.DisplayManager;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends AbstractCommand {

    private final GeyserDebugInfo instance;
    private final PlayerDataManager playerDataManager;
    private final DisplayManager displayManager;

    public ToggleCommand(final GeyserDebugInfo instance) {
        super(instance, "toggle", "Toggle the display of the F3 debug menu", true, true);
        this.instance = instance;
        this.playerDataManager = instance.getPlayerDataManager();
        this.displayManager = new DisplayManager(instance);
    }

    @Override
    protected void execute(@NotNull CommandSource source, @NotNull Command command, @NotNull String[] args) {
        final GeyserSession session = (GeyserSession) source.connection();

        switch (args[0]) {
            case "actionbar" -> displayManager.subscribePlayer(session, DisplayType.ACTIONBAR);
            case "bossbar" -> displayManager.subscribePlayer(session, DisplayType.BOSSBAR);
        }
    }
}
