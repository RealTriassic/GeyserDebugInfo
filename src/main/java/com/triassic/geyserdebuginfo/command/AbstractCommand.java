package com.triassic.geyserdebuginfo.command;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;

public abstract class AbstractCommand {

    protected final GeyserDebugInfo instance;
    @Getter
    private final Command command;

    public AbstractCommand(
            final GeyserDebugInfo instance,
            final String name,
            final String description,
            final boolean playerOnly,
            final boolean bedrockOnly
    ) {
        this.instance = instance;
        this.command = Command.builder(instance)
                .name(name)
                .description(description)
                .permission("f3.command." + name.toLowerCase())
                .playerOnly(playerOnly)
                .bedrockOnly(bedrockOnly)
                .source(CommandSource.class)
                .executor(this::execute)
                .build();
    }

    protected abstract void execute(@NonNull CommandSource source, @NonNull Command command, @NonNull String[] args);
}
