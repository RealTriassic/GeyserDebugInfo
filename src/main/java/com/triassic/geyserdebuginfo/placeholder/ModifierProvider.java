package com.triassic.geyserdebuginfo.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class for defining modifier providers.
 * Subclasses can provide specific modifier values
 * to be used within text by the Geyser session.
 */
public abstract class ModifierProvider {

    /**
     * Retrieves a list of all modifier keys supported by this provider.
     *
     * @return A list of modifier keys.
     */
    public List<String> getModifiers() {
        return Collections.emptyList();
    }

    /**
     * Resolves modifiers in the given text based on the provided Geyser session.
     *
     * @param params The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    @Nullable
    public String onRequest(final String modifier, @NotNull final String params) {
        return null;
    }
}
