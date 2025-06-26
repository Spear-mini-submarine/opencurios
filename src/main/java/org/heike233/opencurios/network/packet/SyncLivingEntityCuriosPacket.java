package org.heike233.opencurios.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.function.Supplier;

public class SyncLivingEntityCuriosPacket {
    public int entityId;
    public CompoundTag curiosTag;

    public SyncLivingEntityCuriosPacket(int entityId, ICuriosItemHandler handler) {
        this.entityId = entityId;
        this.curiosTag = (CompoundTag) handler.writeTag();
    }

    public SyncLivingEntityCuriosPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.curiosTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeNbt(curiosTag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof LivingEntity livingEntity) {
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> handler.readTag(curiosTag));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
