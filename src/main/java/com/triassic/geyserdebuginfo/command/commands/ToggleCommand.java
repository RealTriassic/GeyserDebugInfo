package com.triassic.geyserdebuginfo.command.commands;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.Command;
import com.triassic.geyserdebuginfo.manager.BossBarManager;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;

public class ToggleCommand implements Command {

    private final GeyserDebugInfo instance;
    private final BossBarManager bossBarManager;
    private final PlayerDataManager playerDataManager;

    public ToggleCommand(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
        this.bossBarManager = instance.getBossBarManager();
        this.playerDataManager = instance.getPlayerDataManager();
    }

    @Override
    public org.geysermc.geyser.api.command.Command createCommand() {
        return org.geysermc.geyser.api.command.Command.builder(instance)
                .name("toggle")
                .playerOnly(true)
                .bedrockOnly(true)
                .source(CommandSource.class)
                .description("Toggle the display of the F3 debug menu.")
                .permission("f3.command.toggle")
                .executor(this::execute)
                .build();
    }

    private void execute(@NonNull CommandSource commandSource, org.geysermc.geyser.api.command.Command command, @NonNull String[] strings) {
        final GeyserSession session = (GeyserSession) commandSource.connection();
        final SessionPlayerEntity player = session.getPlayerEntity();

        if (player != null && !playerDataManager.isF3Enabled(player.getUuid())) {
            bossBarManager.createBossBar(player);
        } else {
            bossBarManager.removeBossBar(player);
            playerDataManager.setF3Enabled(player.getUuid(), false);
        }
    }
}
