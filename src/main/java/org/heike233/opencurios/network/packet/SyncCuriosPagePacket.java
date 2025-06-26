package org.heike233.opencurios.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.heike233.opencurios.gui.screen.CuriosScreen;

import java.util.function.Supplier;

public class SyncCuriosPagePacket {
    private final int windowId;
    private final int page;//图标列表
    private final int entityId;//是否显示列表

    public SyncCuriosPagePacket(int windowId, int page,int entityId) {
        this.windowId = windowId;
        this.page = page;
        this.entityId = entityId;
    }

    public SyncCuriosPagePacket(FriendlyByteBuf buf) {
        this.windowId = buf.readVarInt();
        this.page = buf.readVarInt();
        this.entityId = buf.readVarInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(windowId);
        buf.writeVarInt(page);
        buf.writeVarInt(entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 客户端侧：找到当前 CuriosMenu，并刷新
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof CuriosScreen screen) {
                if (screen.getMenu().containerId == windowId) {
                    if (screen.getMenu().getEntity().getId()!=entityId){
                        mc.screen.onClose();
                        return;
                    }
                    screen.getMenu().setPage(page);
                    screen.refresh();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}