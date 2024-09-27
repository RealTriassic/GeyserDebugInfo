package org.stupidcraft.geyserdebuginfo.command.commands;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.GeyserDebugInfo;
import org.stupidcraft.geyserdebuginfo.command.BaseCommand;
import org.stupidcraft.geyserdebuginfo.manager.BossBarManager;
import org.stupidcraft.geyserdebuginfo.manager.PlayerDataManager;

public class F3Command implements BaseCommand {

    private final GeyserDebugInfo instance;
    private final BossBarManager bossBarManager;
    private final PlayerDataManager playerDataManager;

    public F3Command(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
        this.bossBarManager = instance.bossBarManager();
        this.playerDataManager = instance.playerDataManager();
    }

    @Override
    public Command createCommand() {
        return Command.builder(instance)
                .name("f3")
                .playerOnly(true)
                .bedrockOnly(true)
                .source(CommandSource.class)
                .description("Toggle the display of the F3 debug menu.")
                .executor(this::execute)
                .build();
    }

    private void execute(@NonNull CommandSource commandSource, @NonNull Command command, @NonNull String[] strings) {
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
