package com.triassic.geyserdebuginfo.placeholder.placeholders;

import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;

import java.util.Arrays;
import java.util.List;

public class SessionPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "session";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getAuthor() {
        return "Triassic";
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList("world", "gamemode", "difficulty", "time");
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
