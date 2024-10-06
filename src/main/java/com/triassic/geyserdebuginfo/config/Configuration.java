package com.triassic.geyserdebuginfo.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

@ConfigSerializable
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class Configuration {

    private BossBarSettings bossBarSettings = new BossBarSettings();

    public BossBarSettings getBossBarSettings() {
        return this.bossBarSettings;
    }

    @ConfigSerializable
    public static class BossBarSettings {
        private long refreshInterval = 50;
        private List<String> displayFormat = Arrays.asList(
                "Geyser Debug Information",
                "",
                "%player_world%",
                "",
                "XYZ: %player_x% / %player_y% / %player_z%",
                "Block: %player_x:floor% %player_y:floor% %player_z:floor% [%player_relative_x% %player_relative_y% %player_relative_z%]",
                "Chunk: %player_chunk_x% %player_chunk_y% %player_chunk_z% [%player_global_x% %player_global_z% in %player__region_file%]",
                "Facing: %player_facing% (%player_yaw% / %player_pitch%)"
        );

        public long getRefreshInterval() {
            return this.refreshInterval;
        }

        public List<String> getDisplayFormat() {
            return this.displayFormat;
        }
    }
}
