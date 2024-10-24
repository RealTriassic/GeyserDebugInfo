package com.triassic.geyserdebuginfo.placeholder;

import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlaceholderManager {

    private static final Map<String, PlaceholderProvider> providers = new ConcurrentHashMap<>();
    private static final Map<String, ModifierProvider> modifiers = new ConcurrentHashMap<>();

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     * Placeholders should be formatted as %provider_placeholder%.
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text    The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    @NotNull
    public static String setPlaceholders(
            final GeyserSession session,
            final @NotNull String text
    ) {
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

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     * Placeholders should be formatted as %provider_placeholder%.
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text    The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    @NotNull
    public static List<String> setPlaceholders(
            final GeyserSession session,
            final @NotNull List<String> text
    ) {
        return text.stream().map(line -> setPlaceholders(session, line)).collect(Collectors.toList());
    }

    public void register(PlaceholderProvider provider) {
        providers.put(provider.getIdentifier(), provider);
    }

    public void register(ModifierProvider provider) {
        for (String modifier : provider.getModifiers()) {
            modifiers.put(modifier, provider);
        }
    }
}
