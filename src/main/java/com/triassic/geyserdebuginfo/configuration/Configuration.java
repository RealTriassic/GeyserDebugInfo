package com.triassic.geyserdebuginfo.configuration;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@Getter
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class Configuration {

    @Setting("display")
    private Display display = new Display();

    @Setting("config-version")
    @Comment("Used internally, do not change.")
    private int configVersion = 1;

    @Getter
    @ConfigSerializable
    public static class Display {

        @Setting("actionbar")
        private ActionBar actionBar = new ActionBar();

        @Setting("bossbar")
        private BossBar bossBar = new BossBar();

        @Getter
        @ConfigSerializable
        public static class ActionBar {
            @Setting("enabled")
            private boolean enabled = true;

            @Setting("visible-by-default")
            private boolean visibleByDefault = false;

            @Setting("refresh-interval")
            private int refreshInterval = 50;

            @Setting("text")
            private String text = "%player_x% %player_y% %player_z%";
        }

        @Getter
        @ConfigSerializable
        public static class BossBar {
            @Setting("enabled")
            private boolean enabled = true;

            @Setting("visible-by-default")
            private boolean visibleByDefault = false;

            @Setting("refresh-interval")
            private int refreshInterval = 50;

            @Setting("text")
            private List<String> text = List.of(
                    "Geyser Debug Information",
                    "",
                    "%player_world%",
                    "",
                    "XYZ: %player_x% / %player_y% / %player_z%",
                    "Block: %player_x:floor% %player_y:floor% %player_z:floor% [%player_relative_x% %player_relative_y% %player_relative_z%]",
                    "Chunk: %player_chunk_x% %player_chunk_y% %player_chunk_z% [%player_global_x% %player_global_z% in %player_region_file%]",
                    "Facing: %player_facing% (%player_yaw% / %player_pitch%)"
            );
        }
    }
}
