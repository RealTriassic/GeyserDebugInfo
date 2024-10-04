package com.triassic.geyserdebuginfo.command.commands;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.command.BaseCommand;
import com.triassic.geyserdebuginfo.manager.PlaceholderManager;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;

import java.util.Map;

public class ProvidersCommand implements BaseCommand {

    private final GeyserDebugInfo instance;
    private final PlaceholderManager placeholderManager;

    public ProvidersCommand(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
        this.placeholderManager = instance.placeholderManager();
    }

    @Override
    public Command createCommand() {
        return Command.builder(instance)
                .name("providers")
                .playerOnly(false)
                .bedrockOnly(false)
                .source(CommandSource.class)
                .description("Lists all loaded placeholder providers.")
                .permission("f3.command.providers")
                .executor(this::execute)
                .build();
    }

    private void execute(@NonNull CommandSource commandSource, @NonNull Command command, @NonNull String[] strings) {
        if (strings.length >= 1) {
            String providerName = strings[0];

            PlaceholderProvider provider = placeholderManager.getProviders().get(providerName);

            if (provider != null) {
                String identifier = provider.getIdentifier();
                String author = provider.getAuthor();
                String version = provider.getVersion();
                String placeholders = provider.getPlaceholders() != null
                        ? String.join(", ", provider.getPlaceholders())
                        : "No placeholders available";

                String[] messages = {
                        "§6Provider Details:",
                        "§8- Identifier: §r" + identifier,
                        "§8- Author: §r" + author,
                        "§8- Version: §r" + version,
                        "§8- Placeholders: §r" + placeholders
                };

                commandSource.sendMessage(messages);
            } else {
                commandSource.sendMessage(new String[]{
                        "§cProvider '" + providerName + "' not found."
                });
            }
        } else {
            Map<String, PlaceholderProvider> providers = placeholderManager.getProviders();
            commandSource.sendMessage("§6Placeholder Providers: " + "(" + providers.size() + ")");
            commandSource.sendMessage(new String[]{
                    "§8- §r" + String.join(", ", providers.keySet())
            });
        }
    }
}