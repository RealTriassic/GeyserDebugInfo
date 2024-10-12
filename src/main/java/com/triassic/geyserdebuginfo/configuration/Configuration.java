package com.triassic.geyserdebuginfo.configuration;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.List;

@Getter
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class Configuration {

    private long refreshInterval = 50;
    private List<String> displayFormat = Arrays.asList(
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
