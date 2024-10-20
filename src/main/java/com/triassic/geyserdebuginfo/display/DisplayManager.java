package com.triassic.geyserdebuginfo.display;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.display.displays.ActionBarDisplay;
import com.triassic.geyserdebuginfo.display.displays.BossBarDisplay;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import org.geysermc.geyser.session.GeyserSession;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DisplayManager {

    private final GeyserDebugInfo instance;
    private final Map<GeyserSession, Set<Display>> activeDisplays;
    private final ScheduledExecutorService executor;
    private final ExecutorService asyncExecutor;
    private final PlayerDataManager playerDataManager;

    public DisplayManager(GeyserDebugInfo instance) {
        this.instance = instance;
        this.activeDisplays = new ConcurrentHashMap<>();
        this.executor = Executors.newScheduledThreadPool(10);
        this.asyncExecutor = Executors.newCachedThreadPool();
        this.playerDataManager = instance.getPlayerDataManager();
    }

    public void subscribePlayer(final GeyserSession session, final DisplayType displayType) {
        asyncExecutor.submit(() -> {
            Display display = createDisplay(session, displayType);
            Set<Display> displays = activeDisplays.computeIfAbsent(session, k -> ConcurrentHashMap.newKeySet());
            displays.add(display);
            display.startUpdating(executor);

            playerDataManager.setDisplayEnabled(session.playerUuid(), displayType, true);
        });
    }

    public void unsubscribePlayer(final GeyserSession session, final DisplayType displayType) {
        asyncExecutor.submit(() -> Optional.ofNullable(activeDisplays.get(session)).ifPresent(displays -> {
            for (Display display : displays) {
                if (display.getDisplayType() == displayType) {
                    displays.remove(display);
                    display.stopUpdating();
                    if (displays.isEmpty()) {
                        activeDisplays.remove(session);
                    }
                    playerDataManager.setDisplayEnabled(session.playerUuid(), displayType, false);
                    break;
                }
            }
        }));
    }

    private Display createDisplay(final GeyserSession session, final DisplayType displayType) {
        return switch (displayType) {
            case ACTIONBAR -> new ActionBarDisplay(instance, session);
            case BOSSBAR -> new BossBarDisplay(instance, session);
        };
    }

    public void shutdown() {
        executor.shutdown();
        asyncExecutor.shutdown();
    }
}
