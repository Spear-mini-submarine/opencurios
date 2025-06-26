package org.heike233.opencurios.gui.provider;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkHooks;
import org.heike233.opencurios.gui.menu.CuriosMenu;
import org.jetbrains.annotations.NotNull;

public class CuriosMenuProvider implements MenuProvider {
    private final LivingEntity target;

    public CuriosMenuProvider(LivingEntity entity) {
        this.target = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return target.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inv, @NotNull Player player) {
        return new CuriosMenu(windowId, inv, target, 0);
    }

    public void open(ServerPlayer player) {
        this.open(player,0);
    }
    public void open(ServerPlayer player,int page) {
        NetworkHooks.openScreen(player, this, buf -> {
            buf.writeInt(target.getId());
            buf.writeInt(page);
        });
    }
}
