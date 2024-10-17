package com.triassic.geyserdebuginfo.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

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
     * Handles a request based on the given modifier and parameters.
     *
     * @param modifier the modifier used for the request, which can be {@code null}
     * @param params   the parameters associated with the request, must not be {@code null}
     * @return a response as a {@code String}, or {@code null} if no response is available
     */
    @Nullable
    public String onRequest(final String modifier, @NotNull final String params) {
        return null;
    }
}
