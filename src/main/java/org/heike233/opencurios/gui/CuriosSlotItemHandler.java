package org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CuriosSlotItemHandler extends Slot {
    protected IItemHandler itemHandler;
    protected int index;
    protected boolean isDummy;
    @Nullable
    protected String slotType;

    private static final net.minecraft.world.SimpleContainer DUMMY_CONTAINER = new net.minecraft.world.SimpleContainer(1);

    public CuriosSlotItemHandler(IItemHandler handler, int index, int x, int y) {
        super(DUMMY_CONTAINER, 0, x, y);
        this.itemHandler = handler;
        this.index = index;
        this.isDummy = false;
        this.slotType = null;
    }

    public void setHandlerAndIndex(IItemHandler handler, int index, boolean isDummy, @Nullable String slotType) {
        this.itemHandler = handler;
        this.index = index;
        this.isDummy = isDummy;
        this.slotType = slotType;
    }

    @Override
    public boolean hasItem() {
        return !isDummy && !getItem().isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return isDummy ? ItemStack.EMPTY : itemHandler.getStackInSlot(index);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        if (!isDummy) {
            if (itemHandler instanceof IItemHandlerModifiable modifiable) {
                modifiable.setStackInSlot(index, stack);
                setChanged();
            }
        }
    }

    @Override
    public int getMaxStackSize() {
        return isDummy ? 0 : itemHandler.getSlotLimit(index);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return !isDummy && itemHandler.isItemValid(index, stack);
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        if (isDummy) return ItemStack.EMPTY;
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            ItemStack result = stack.split(amount);
            setChanged();
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean mayPickup(net.minecraft.world.entity.player.@NotNull Player player) {
        return !isDummy;
    }

    @Override
    public boolean isActive() {
        return !isDummy;
    }

    // 动态获取槽位类型的icon（贴图），供Screen渲染用
    public ResourceLocation getSlotIcon() {
        if (slotType != null) {
            return top.theillusivec4.curios.api.CuriosApi.getSlot(slotType)
                    .map(type -> type.getIcon().withPath("textures/" + type.getIcon().getPath() + ".png"))
                    .orElse(new ResourceLocation("curios", "textures/slot/empty_curio_slot.png"));
        }
        return new ResourceLocation("curios", "textures/slot/empty_curio_slot.png");
    }
}