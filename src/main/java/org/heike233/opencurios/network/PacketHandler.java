package org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.Touhou_little_maid_irons_spells_n_spellbooks;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network.packet.CuriosPagePacket;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network.packet.SyncCuriosPagePacket;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network.packet.SyncLivingEntityCuriosPacket;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Touhou_little_maid_irons_spells_n_spellbooks.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int idx = 0;
        INSTANCE.registerMessage(idx++, CuriosPagePacket.class, CuriosPagePacket::toBytes, CuriosPagePacket::new, CuriosPagePacket::handle);
        INSTANCE.registerMessage(idx++, SyncCuriosPagePacket.class, SyncCuriosPagePacket::toBytes, SyncCuriosPagePacket::new, SyncCuriosPagePacket::handle);
        INSTANCE.registerMessage(idx++, SyncLivingEntityCuriosPacket.class, SyncLivingEntityCuriosPacket::toBytes, SyncLivingEntityCuriosPacket::new, SyncLivingEntityCuriosPacket::handle);
    }
}
