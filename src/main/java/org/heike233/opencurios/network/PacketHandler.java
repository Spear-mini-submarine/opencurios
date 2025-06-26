package org.heike233.opencurios.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.heike233.opencurios.Opencurios;
import org.heike233.opencurios.network.packet.CuriosPagePacket;
import org.heike233.opencurios.network.packet.SyncCuriosPagePacket;
import org.heike233.opencurios.network.packet.SyncLivingEntityCuriosPacket;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Opencurios.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int idx = 0;
        INSTANCE.registerMessage(idx++, CuriosPagePacket.class, CuriosPagePacket::toBytes, CuriosPagePacket::new, CuriosPagePacket::handle);
        INSTANCE.registerMessage(idx++, SyncCuriosPagePacket.class, SyncCuriosPagePacket::toBytes, SyncCuriosPagePacket::new, SyncCuriosPagePacket::handle);
        INSTANCE.registerMessage(idx++, SyncLivingEntityCuriosPacket.class, SyncLivingEntityCuriosPacket::toBytes, SyncLivingEntityCuriosPacket::new, SyncLivingEntityCuriosPacket::handle);
    }
}
