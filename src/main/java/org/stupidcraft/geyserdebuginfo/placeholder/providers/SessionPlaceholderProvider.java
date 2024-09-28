package org.stupidcraft.geyserdebuginfo.placeholder.providers;

import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.placeholder.PlaceholderProvider;

import java.util.HashMap;
import java.util.Map;

public class SessionPlaceholderProvider implements PlaceholderProvider {

    @Override
    public Map<String, String> getPlaceholders(String line, GeyserSession session) {
        return new HashMap<>(Map.ofEntries(
                Map.entry("world", String.valueOf(session.getWorldName())),
                Map.entry("gamemode", String.valueOf(session.getGameMode())),
                Map.entry("difficulty", String.valueOf(session.getWorldCache().getDifficulty())),
                Map.entry("time", String.valueOf(session.getWorldTicks()))
        ));
    }
}
