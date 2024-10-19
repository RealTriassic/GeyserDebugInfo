package com.triassic.geyserdebuginfo.display.displays;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.display.Display;
import com.triassic.geyserdebuginfo.display.DisplayType;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderManager;
import org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class ActionBarDisplay extends Display {

    private final GeyserDebugInfo instance;
    private final SetTitlePacket titlePacket;

    public ActionBarDisplay(GeyserDebugInfo instance, @NotNull GeyserSession session) {
        super(session, DisplayType.ACTIONBAR, 50);

        this.titlePacket = new SetTitlePacket();
        titlePacket.setType(SetTitlePacket.Type.ACTIONBAR);
        titlePacket.setFadeOutTime(1);
        titlePacket.setStayTime(2);
        titlePacket.setXuid("");
        titlePacket.setPlatformOnlineId("");

        this.instance = instance;
    }

    @Override
    public void updateDisplay() {
        titlePacket.setText(PlaceholderManager.setPlaceholders(session, instance.getConfig().getDisplay().getActionBar().getText()));
        session.sendUpstreamPacket(titlePacket);
    }

    @Override
    public void removeDisplay() {
        titlePacket.setText("");
        session.sendUpstreamPacket(titlePacket);
    }
}
