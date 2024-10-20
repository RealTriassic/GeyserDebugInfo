package com.triassic.geyserdebuginfo.display.displays;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.display.Display;
import com.triassic.geyserdebuginfo.display.DisplayType;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderManager;
import net.kyori.adventure.text.Component;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BossBarDisplay extends Display {

    private final GeyserDebugInfo instance;
    private final BossBar bossBar;

    public BossBarDisplay(GeyserDebugInfo instance, @NotNull GeyserSession session) {
        super(session, DisplayType.BOSSBAR, 50);
        long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
        this.bossBar = new BossBar(session, entityId, Component.empty(), 1.0f, 1, 0, 0);
        session.getEntityCache().addBossBar(session.playerUuid(), bossBar);

        this.instance = instance;
    }

    @Override
    public void updateDisplay() {
        List<String> displayFormat = instance.getConfig().get().getDisplay().getBossBar().getText();
        String displayText = displayFormat.stream()
                .map(line -> PlaceholderManager.setPlaceholders(session, line))
                .collect(Collectors.joining("\n"));

        bossBar.updateTitle(Component.text(displayText));
    }

    @Override
    public void removeDisplay() {
        bossBar.removeBossBar();
    }
}
