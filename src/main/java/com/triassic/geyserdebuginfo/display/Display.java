package com.triassic.geyserdebuginfo.display;

import lombok.Getter;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class Display {

    protected final GeyserSession session;
    @Getter
    protected final DisplayType displayType;
    protected final long refreshInterval;
    private ScheduledFuture<?> updateTask;

    public Display(@NotNull GeyserSession session, @NotNull DisplayType displayType, long refreshInterval) {
        this.session = session;
        this.displayType = displayType;
        this.refreshInterval = refreshInterval;
    }

    /**
     * Updates the display with the given text.
     */
    public abstract void updateDisplay();

    /**
     * Removes the display from the player.
     */
    public abstract void removeDisplay();

    /**
     * Starts the scheduled update task for the display.
     */
    public void startUpdating(ScheduledExecutorService executor) {
        updateTask = executor.scheduleAtFixedRate(this::updateDisplay, 0, refreshInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the scheduled update task for the display.
     */
    public void stopUpdating() {
        if (updateTask != null) {
            updateTask.cancel(false);
            updateTask = null;
        }
        removeDisplay();
    }
}
