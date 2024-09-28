package org.stupidcraft.geyserdebuginfo.placeholder;

import org.geysermc.geyser.session.GeyserSession;

import java.util.Map;

public interface PlaceholderProvider {

    Map<String, String> getPlaceholders(String line, GeyserSession session);
}
