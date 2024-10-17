package com.triassic.geyserdebuginfo.placeholder;

import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderProvider {

    /**
     * Gets the unique identifier for this placeholder provider.
     *
     * @return The identifier of the provider.
     */
    public abstract String getIdentifier();

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param params  The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    @Nullable
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        return null;
    }
}
