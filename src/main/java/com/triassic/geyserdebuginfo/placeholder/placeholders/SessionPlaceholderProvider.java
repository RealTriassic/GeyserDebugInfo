package com.triassic.geyserdebuginfo.placeholder.placeholders;

import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class SessionPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "session";
    }

    @Override
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        return switch (params) {
            case "world" -> String.valueOf(session.getWorldName());
            case "gamemode" -> String.valueOf(session.getGameMode());
            case "difficulty" -> String.valueOf(session.getWorldCache().getDifficulty());
            case "time" -> String.valueOf(session.getTicks());
            default -> null;
        };
    }
}
