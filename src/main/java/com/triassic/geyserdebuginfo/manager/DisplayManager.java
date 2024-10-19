package com.triassic.geyserdebuginfo.manager;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.display.Display;
import com.triassic.geyserdebuginfo.display.DisplayType;
import com.triassic.geyserdebuginfo.display.displays.ActionBarDisplay;
import com.triassic.geyserdebuginfo.display.displays.BossBarDisplay;
import org.geysermc.geyser.session.GeyserSession;

import java.util.Map;
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

    public DisplayManager(GeyserDebugInfo instance) {
        this.instance = instance;
        this.activeDisplays = new ConcurrentHashMap<>();
        this.executor = Executors.newScheduledThreadPool(10);
        this.asyncExecutor = Executors.newCachedThreadPool();
    }

    public void subscribePlayer(final GeyserSession session, final DisplayType displayType) {
        asyncExecutor.submit(() -> {
            Display display = createDisplay(session, displayType);
            activeDisplays.computeIfAbsent(session, k -> ConcurrentHashMap.newKeySet()).add(display);
            display.startUpdating(executor);
        });
    }

    public void unsubscribePlayer(final GeyserSession session, final DisplayType displayType) {
        asyncExecutor.submit(() -> {
            Set<Display> displays = activeDisplays.get(session);
            if (displays != null) {
                displays.stream()
                        .filter(display -> display.getDisplayType() == displayType)
                        .findFirst()
                        .ifPresent(display -> {
                            displays.remove(display);
                            display.stopUpdating();
                            if (displays.isEmpty()) {
                                activeDisplays.remove(session);
                            }
                        });
            }
        });
    }

    private Display createDisplay(final GeyserSession session, final DisplayType displayType) {
        return switch (displayType) {
            case ACTIONBAR -> new ActionBarDisplay(instance, session);
            case BOSSBAR -> {
                long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
                yield new BossBarDisplay(instance, session, entityId);
            }
        };
    }

    public void shutdown() {
        executor.shutdown();
        asyncExecutor.shutdown();
    }
}
