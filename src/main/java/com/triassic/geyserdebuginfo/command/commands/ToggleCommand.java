package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.AbstractCommand;
import com.triassic.geyserdebuginfo.manager.BossBarManager;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends AbstractCommand {

    private final BossBarManager bossBarManager;
    private final PlayerDataManager playerDataManager;

    public ToggleCommand(final GeyserDebugInfo instance) {
        super(instance, "toggle", "Toggle the display of the F3 debug menu", true, true);
        this.bossBarManager = instance.getBossBarManager();
        this.playerDataManager = instance.getPlayerDataManager();
    }

    @Override
    protected void execute(@NotNull CommandSource source, @NotNull Command command, @NotNull String[] args) {
        final GeyserSession session = (GeyserSession) source.connection();
        final SessionPlayerEntity player = session.getPlayerEntity();

        if (player != null && !playerDataManager.isF3Enabled(player.getUuid())) {
            bossBarManager.createBossBar(player);
        } else {
            bossBarManager.removeBossBar(player);
            playerDataManager.setF3Enabled(player.getUuid(), false);
        }
    }
}
