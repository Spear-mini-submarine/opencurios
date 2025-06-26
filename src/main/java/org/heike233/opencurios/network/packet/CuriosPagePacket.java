package org.heike233.opencurios.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.heike233.opencurios.gui.menu.CuriosMenu;

import java.util.function.Supplier;

public class CuriosPagePacket {
    public final int containerId;
    public final int newPage;

    public CuriosPagePacket(int containerId, int newPage) {
        this.containerId = containerId;
        this.newPage = newPage;
    }

    public CuriosPagePacket(FriendlyByteBuf buf) {
        this.containerId = buf.readVarInt();
        this.newPage = buf.readVarInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(containerId);
        buf.writeVarInt(newPage);
    }

    public static void handle(CuriosPagePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof CuriosMenu menu && menu.containerId == msg.containerId) {
                menu.changePage(msg.newPage, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
