package org.stupidcraft.geyserdebuginfo.config;

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
        private int color = 1;
        private int overlay = 1;
        private int darkenSky = 1;
        private float healthPercentage = 1.0f;
        private long refreshInterval = 50;
        private List<String> displayFormat = Arrays.asList(
                "Geyser Debug Information",
                "",
                "XYZ: %x% / %y% / %z%",
                "Block: %floor_x% %floor_y% %floor_z% [%relative_chunk_x% %relative_chunk_y% %relative_chunk_z%]",
                "Chunk: %chunk_x% %chunk_y% %chunk_z%",
                "Facing: %facing% (%yaw% / %pitch%)"
        );

        public int getColor() {
            return this.color;
        }

        public int getOverlay() {
            return this.overlay;
        }

        public int getDarkenSky() {
            return this.darkenSky;
        }

        public float getHealthPercentage() {
            return this.healthPercentage;
        }

        public long getRefreshInterval() {
            return this.refreshInterval;
        }

        public List<String> getDisplayFormat() {
            return this.displayFormat;
        }
    }
}
