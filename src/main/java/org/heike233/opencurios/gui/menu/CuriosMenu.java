package org.heike233.opencurios.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.heike233.opencurios.gui.CuriosSlotItemHandler;
import org.heike233.opencurios.init.Menus;
import org.heike233.opencurios.item.OpenCuriosItem;
import org.heike233.opencurios.network.PacketHandler;
import org.heike233.opencurios.network.packet.SyncCuriosPagePacket;
import org.heike233.opencurios.network.packet.SyncLivingEntityCuriosPacket;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuriosMenu extends AbstractContainerMenu {
    protected final LivingEntity entity;
    protected final Player player;
    private int page;
    public int maxPage = 0;
    private static final int SLOTS_PER_PAGE = 27;
    private static final IItemHandler DUMMY_HANDLER = new ItemStackHandler(1);
    public final List<CuriosSlotItemHandler> curiosSlots = new ArrayList<>(SLOTS_PER_PAGE);

    public CuriosMenu(int windowId, Inventory playerInv, FriendlyByteBuf data) {
        this(windowId, playerInv, getEntityFromBuf(playerInv, data), data.readInt());
    }

    public CuriosMenu(int windowId, Inventory playerInv, LivingEntity entity, int newPage) {
        super(getMenuType(), windowId);
        this.entity = entity;
        this.player = playerInv.player;
        this.page = newPage;
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() { return CuriosMenu.this.page; }
            @Override
            public void set(int value) {
                if (CuriosMenu.this.page != value) {
                    CuriosMenu.this.page = value;
                }
            }
        });

        // 初始化 curios 槽
        int xStart = 9, yStart = 19, slotSize = 18;
        for (int i = 0; i < SLOTS_PER_PAGE; ++i) {
            int x = xStart + (i % 9) * slotSize;
            int y = yStart + (i / 9) * slotSize;
            CuriosSlotItemHandler slot = new CuriosSlotItemHandler(DUMMY_HANDLER, 0, x, y);
            curiosSlots.add(slot);
            addSlot(slot);
        }

        // 玩家背包槽位
        int invY = 85;
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col)
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, invY + row * 18));
        for (int hotbar = 0; hotbar < 9; ++hotbar)
            this.addSlot(new Slot(playerInv, hotbar, 8 + hotbar * 18, invY + 58));
        refreshCuriosSlots();
    }

    public void refreshCuriosSlots() {
        // 计算本页要显示的槽
        List<SlotInfo> slotsOnPage = new ArrayList<>();
        int totalSlots = 0;

        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(entity).resolve();
        if (curiosOpt.isPresent()) {
            ICuriosItemHandler curios = curiosOpt.get();
            for (var entry : curios.getCurios().entrySet()) {
                IItemHandler itemHandler = entry.getValue().getStacks();
                totalSlots += itemHandler.getSlots();
            }
            int startIdx = page * SLOTS_PER_PAGE;
            int endIdx = Math.min(startIdx + SLOTS_PER_PAGE, totalSlots);
            this.maxPage = totalSlots == 0 ? 0 : (totalSlots - 1) / SLOTS_PER_PAGE;

            int slotIndex = 0;
            for (var entry : curios.getCurios().entrySet()) {
                String slotTypeName = entry.getKey();
                IItemHandler itemHandler = entry.getValue().getStacks();
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (slotIndex >= startIdx && slotIndex < endIdx) {
                        slotsOnPage.add(new SlotInfo(itemHandler, i, slotTypeName));
                    }
                    slotIndex++;
                }
            }
        } else {
            this.maxPage = 0;
        }

        // 动态绑定槽位
        for (int i = 0; i < SLOTS_PER_PAGE; i++) {
            if (i < slotsOnPage.size()) {
                SlotInfo info = slotsOnPage.get(i);
                curiosSlots.get(i).setHandlerAndIndex(info.handler, info.slot, false, info.slotType);
            } else {
                curiosSlots.get(i).setHandlerAndIndex(DUMMY_HANDLER, 0, true, null);
            }
        }
        broadcastChanges();
    }
    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        // 清理逻辑
        OpenCuriosItem.clearOpenLivingEntity(entity);
    }
    public void changePage(int newPage, Player player) {
        if (newPage < 0 || newPage > maxPage) return;
        if (!player.level().isClientSide()){
            this.page = newPage;
            this.refreshCuriosSlots();
            // 同步 Curios 数据到客户端
            CuriosApi.getCuriosInventory(entity).ifPresent(curios -> {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()-> (ServerPlayer)player),
                        new SyncLivingEntityCuriosPacket(entity.getId(),curios));
            });
            // 发送同步数据包到客户端
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
                    new SyncCuriosPagePacket(this.containerId, newPage, entity.getId()));
        }
    }

    public void setPage(int page) {
        this.page = page;
        refreshCuriosSlots();
    }

    public int getPage() { return page; }
    public int getSlotsPerPage() { return SLOTS_PER_PAGE; }

    private static LivingEntity getEntityFromBuf(Inventory inv, FriendlyByteBuf data) {
        int entityId = data.readInt();
        return (LivingEntity) inv.player.level().getEntity(entityId);
    }
    public LivingEntity getEntity() { return entity; }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.getSlot(index);
        if (slot == null || !slot.hasItem())
            return ItemStack.EMPTY;
        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();

        int curiosStart = 0;
        int curiosEnd = SLOTS_PER_PAGE;

        int invStart = SLOTS_PER_PAGE;
        int invEnd = invStart + 36;

        if (index < curiosEnd) {
            // 从 curios  -> 背包
            if (!this.moveItemStackTo(original, invStart, invEnd, false))
                return ItemStack.EMPTY;
        } else {
            // 背包 -> curios
            if (!this.moveItemStackTo(original, curiosStart, curiosEnd, false))
                return ItemStack.EMPTY;
        }
        if (original.isEmpty())
            slot.set(ItemStack.EMPTY);
        else
            slot.setChanged();

        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceTo(entity) < 16.0F;
    }

    private record SlotInfo(IItemHandler handler, int slot, String slotType) {}

    private static MenuType<CuriosMenu> getMenuType() {
        return Menus.Curios_Menu.get();
    }
}