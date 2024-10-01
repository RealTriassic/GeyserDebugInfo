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
                "%world%",
                "",
                "XYZ: %x% / %y% / %z%",
                "Block: %x:floor% %y:floor% %z:floor% [%relative_chunk_x% %relative_chunk_y% %relative_chunk_z%]",
                "Chunk: %chunk_x% %chunk_y% %chunk_z% [%global_chunk_x% %global_chunk_z% in %region_file%]",
                "Facing: %facing% (%yaw% / %pitch%)"
        );

        public long getRefreshInterval() {
            return this.refreshInterval;
        }

        public List<String> getDisplayFormat() {
            return this.displayFormat;
        }
    }
}
