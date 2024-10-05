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
                "%session_world%",
                "",
                "XYZ: %position_x% / %position_y% / %position_z%",
                "Block: %position_x:floor% %position_y:floor% %position_z:floor% [%chunk_relative_x% %chunk_relative_y% %chunk_relative_z%]",
                "Chunk: %chunk_x% %chunk_y% %chunk_z% [%chunk_global_x% %chunk_global_z% in %chunk_region_file%]",
                "Facing: %position_facing% (%position_yaw% / %position_pitch%)"
        );

        public long getRefreshInterval() {
            return this.refreshInterval;
        }

        public List<String> getDisplayFormat() {
            return this.displayFormat;
        }
    }
}
