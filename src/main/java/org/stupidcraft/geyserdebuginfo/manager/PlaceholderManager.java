package org.stupidcraft.geyserdebuginfo.manager;

import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.stupidcraft.geyserdebuginfo.util.PlaceholderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager {

    private final List<PlaceholderProvider> providers = new ArrayList<>();

    public void registerProvider(PlaceholderProvider provider) {
        providers.add(provider);
    }

    public List<PlaceholderProvider> getProviders() {
        return providers;
    }

    /**
     * Resolves placeholders in the given text based on the provided Geyser session.
     * Placeholders should be formatted as %placeholder% and may include an optional
     * modifier separated by a colon (e.g., %world:uppercase%).
     *
     * @param session The Geyser session to retrieve context-specific placeholder values.
     * @param text The text containing placeholders to be resolved.
     * @return The text with placeholders resolved to their respective values.
     */
    public String setPlaceholders(GeyserSession session, String text) {
        StringBuilder finalResult = new StringBuilder();

        Pattern placeholderPattern = Pattern.compile("%(\\w+)(?::(\\w+))?%");
        Matcher matcher = placeholderPattern.matcher(text);

        int lastEnd = 0;

        while (matcher.find()) {
            finalResult.append(text, lastEnd, matcher.start());

            String placeholderKey = matcher.group(1);
            String modifier = matcher.group(2);

            String value = null;
            for (PlaceholderProvider provider : providers) {
                Map<String, String> placeholders = provider.getPlaceholders(placeholderKey, session);
                value = placeholders.get(placeholderKey);
                if (value != null) {
                    break;
                }
            }

            if (value != null && modifier != null) {
                value = PlaceholderUtil.setModifiers(modifier, value);
            }

            finalResult.append(value != null ? value : matcher.group(0));

            lastEnd = matcher.end();
        }

        finalResult.append(text, lastEnd, text.length());

        return finalResult.toString();
    }
}
