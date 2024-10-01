package com.triassic.geyserdebuginfo.placeholder;

import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class for defining placeholder providers.
 * Subclasses can provide specific placeholder values
 * to be used within text by the Geyser session.
 */
public abstract class PlaceholderProvider {

    /**
     * Gets the unique identifier for this placeholder provider.
     *
     * @return The identifier of the provider.
     */
    public abstract String getIdentifier();

    /**
     * Gets the version of this placeholder provider.
     *
     * @return The version string of the provider.
     */
    public abstract String getVersion();

    /**
     * Gets the author of this placeholder provider.
     *
     * @return The author name of the provider.
     */
    public abstract String getAuthor();

    /**
     * Retrieves a list of all placeholder keys supported by this provider.
     *
     * @return A list of placeholder keys.
     */
    public List<String> getPlaceholders() {
        return Collections.emptyList();
    }

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text    The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    @Nullable
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        return null;
    }
}
