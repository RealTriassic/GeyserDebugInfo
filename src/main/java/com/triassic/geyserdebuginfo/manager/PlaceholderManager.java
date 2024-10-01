package com.triassic.geyserdebuginfo.manager;

import org.geysermc.geyser.api.extension.ExtensionLogger;
import org.geysermc.geyser.session.GeyserSession;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager {

    private final ExtensionLogger logger;
    private final Map<String, PlaceholderProvider> providers = new ConcurrentHashMap<>();

    public PlaceholderManager(
            final ExtensionLogger logger
    ) {
        this.logger = logger;
    }

    public void registerProvider(PlaceholderProvider provider) {
        providers.put(provider.getIdentifier(), provider);
        logger.info("Registered provider " + provider.getIdentifier() + " " + "v" + provider.getVersion() + " by " + provider.getAuthor());
    }

    public void unregisterProvider(PlaceholderProvider provider) {
        providers.remove(provider.getIdentifier());
        logger.info("Unregistered provider " + provider.getIdentifier() + " " + "v" + provider.getVersion() + " by " + provider.getAuthor());
    }

    public Map<String, PlaceholderProvider> getProviders() {
        return providers;
    }

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     * Placeholders should be formatted as %provider_placeholder% (e.g., %session_world%).
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text    The text containing placeholders to resolve.
     * @return The text with placeholders resolved to their respective values.
     */
    public String setPlaceholders(GeyserSession session, String text) {
        StringBuilder finalResult = new StringBuilder();
        Pattern placeholderPattern = Pattern.compile("%(\\w+)_?(\\w+)%");
        Matcher matcher = placeholderPattern.matcher(text);

        int lastEnd = 0;

        while (matcher.find()) {
            finalResult.append(text, lastEnd, matcher.start());

            String providerIdentifier = matcher.group(1);
            String placeholderKey = matcher.group(2);
            String value = null;

            // Retrieve the provider from the map
            PlaceholderProvider provider = providers.get(providerIdentifier);
            if (provider != null) {
                value = provider.onRequest(session, placeholderKey);
            }

            // Append resolved value or original placeholder
            finalResult.append(value != null ? value : matcher.group(0));
            lastEnd = matcher.end();
        }

        // Append any remaining text
        finalResult.append(text, lastEnd, text.length());
        return finalResult.toString();
    }
}
