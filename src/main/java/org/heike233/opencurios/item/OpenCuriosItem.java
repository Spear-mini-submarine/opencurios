package org.heike233.touhou_little_maid_irons_spells_n_spellbooks.comm.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui.provider.CuriosMenuProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OpenCuriosItem extends Item {
    public OpenCuriosItem(Properties p_41383_) {
        super(p_41383_);
    }
    private static final Map<UUID, UUID> openLivingEntities = new ConcurrentHashMap<>();
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.touhou_little_maid_irons_spells_n_spellbooks.open_curios_item.desc").withStyle(ChatFormatting.GRAY));
    }
    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player user, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (user instanceof ServerPlayer sp&&!(target instanceof Player)) {
            UUID targetUUID = target.getUUID();
            UUID prev = openLivingEntities.putIfAbsent(targetUUID, sp.getUUID());
            if (prev == null) {
                new CuriosMenuProvider(target).open(sp);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
    public static void clearOpenLivingEntity(LivingEntity entity) {
        openLivingEntities.remove(entity.getUUID());
    }
}
