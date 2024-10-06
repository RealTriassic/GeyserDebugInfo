package com.triassic.geyserdebuginfo.placeholder.placeholders;

import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class ServerPlaceholderProvider extends PlaceholderProvider {

    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public String getIdentifier() {
        return "server";
    }

    @Override
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        final GeyserImpl geyser = session.getGeyser();
        final int i = 1024 * 1024; // Mebibytes conversion factor (1 MiB = 1024 * 1024 bytes)

        return switch (params) {
            case "name" -> geyser.bedrockListener().serverName();
            case "address" -> geyser.bedrockListener().address();
            case "port" -> String.valueOf(geyser.bedrockListener().port());
            case "version" -> GeyserImpl.VERSION;
            case "build" -> GeyserImpl.BUILD_NUMBER;
            case "platform" -> geyser.getPlatformType().platformName();
            case "online" -> String.valueOf(geyser.onlineConnectionsCount());
            case "view_distance" -> String.valueOf(session.getServerRenderDistance());

            case "remote_address" -> geyser.defaultRemoteServer().address();
            case "remote_port" -> String.valueOf(geyser.defaultRemoteServer().port());
            case "remote_version" -> geyser.defaultRemoteServer().minecraftVersion();
            case "remote_protocol" -> String.valueOf(geyser.defaultRemoteServer().protocolVersion());
            case "remote_auth_type" -> geyser.defaultRemoteServer().authType().toString();

            case "ram_used" -> String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / i);
            case "ram_free" -> String.valueOf(runtime.freeMemory() / i);
            case "ram_total" -> String.valueOf(runtime.totalMemory() / i);
            case "ram_max" -> String.valueOf(runtime.maxMemory() / i);
            default -> null;
        };
    }
}
