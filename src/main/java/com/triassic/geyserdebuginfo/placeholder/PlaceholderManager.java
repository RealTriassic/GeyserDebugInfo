package com.triassic.geyserdebuginfo.placeholder;

import org.geysermc.geyser.session.GeyserSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlaceholderManager {

    private final Map<String, PlaceholderProvider> providers = new ConcurrentHashMap<>();
    private final Map<String, ModifierProvider> modifiers = new ConcurrentHashMap<>();

    public void registerProvider(PlaceholderProvider provider) {
        providers.put(provider.getIdentifier(), provider);
    }

    public void registerProvider(ModifierProvider provider) {
        for (String modifier : provider.getModifiers()) {
            modifiers.put(modifier, provider);
        }
    }

    public void unregisterProvider(PlaceholderProvider provider) {
        providers.remove(provider.getIdentifier());
    }

    public void unregisterProvider(ModifierProvider provider) {
        for (String modifier : provider.getModifiers()) {
            modifiers.remove(modifier);
        }
    }

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     * Placeholders should be formatted as %provider_placeholder% (e.g., %chunk_x% or %position_x%).
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text    The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    public String setPlaceholders(GeyserSession session, String text) {
        StringBuilder finalResult = new StringBuilder();
        int cursor = 0;

        while (cursor < text.length()) {
            int startIndex = text.indexOf('%', cursor);
            if (startIndex == -1) {
                finalResult.append(text, cursor, text.length());
                break;
            }

            finalResult.append(text, cursor, startIndex);
            cursor = startIndex + 1; // Move cursor past the '%'

            int endIndex = text.indexOf('%', cursor);
            if (endIndex == -1) {
                finalResult.append(text, startIndex, text.length());
                break;
            }

            String placeholder = text.substring(startIndex + 1, endIndex);
            String[] parts = placeholder.split(":", 2); // Split on ':' to get the modifier if it exists
            String[] keyParts = parts[0].split("_", 2); // Split on '_' to separate provider identifier and placeholder key

            String value = null;

            if (keyParts.length == 2) {
                PlaceholderProvider placeholderProvider = providers.get(keyParts[0]);
                if (placeholderProvider != null) {
                    value = placeholderProvider.onRequest(session, keyParts[1]);
                }
            }

            if (value != null) {
                if (parts.length > 1) {
                    ModifierProvider modifierProvider = modifiers.get(parts[1]);
                    if (modifierProvider != null) {
                        value = modifierProvider.onRequest(parts[1], value);
                    }
                }
                finalResult.append(value);
            } else {
                finalResult.append('%').append(placeholder).append('%');
            }

            cursor = endIndex + 1; // Move past the closing '%'
        }

        return finalResult.toString();
    }
}
