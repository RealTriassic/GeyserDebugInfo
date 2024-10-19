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
    private final PlaceholderManager placeholderManager;

    public BossBarDisplay(GeyserDebugInfo instance, @NotNull GeyserSession session, long entityId) {
        super(session, DisplayType.BOSSBAR, 50);
        this.bossBar = new BossBar(session, entityId, Component.empty(), 1.0f, 1, 0, 0);
        session.getEntityCache().addBossBar(session.playerUuid(), bossBar);

        this.instance = instance;
        this.placeholderManager = instance.getPlaceholderManager();
    }

    @Override
    public void updateDisplay() {
        List<String> displayFormat = instance.getConfig().getDisplay().getBossBar().getText();
        String displayText = displayFormat.stream()
                .map(line -> placeholderManager.setPlaceholders(session, line))
                .collect(Collectors.joining("\n"));

        bossBar.updateTitle(Component.text(displayText));
    }

    @Override
    public void removeDisplay() {
        bossBar.removeBossBar();
    }
}
