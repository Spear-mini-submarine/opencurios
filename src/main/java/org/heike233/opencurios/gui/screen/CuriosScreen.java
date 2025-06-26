package org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.Touhou_little_maid_irons_spells_n_spellbooks;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui.CuriosSlotItemHandler;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui.menu.CuriosMenu;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network.PacketHandler;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.network.packet.CuriosPagePacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CuriosScreen extends AbstractContainerScreen<CuriosMenu> {
    private static final ResourceLocation SPRITE = new ResourceLocation(Touhou_little_maid_irons_spells_n_spellbooks.MODID, "textures/gui/inventory.png");

    public CuriosScreen(CuriosMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(graphics);

        int guiLeft = (this.width - 176) / 2;
        int guiTop = (this.height - 220) / 2;

        graphics.blit(SPRITE, guiLeft, guiTop, 0, 0, 174, 166);

        renderCuriosSlots(graphics, this.leftPos, this.topPos);

        // 翻页按钮
        boolean prevActive = menu.getPage() > 0;
        boolean nextActive = menu.getPage() < menu.maxPage;
        // 按钮区域
        int prevX = this.leftPos + 144, prevY = this.topPos + 5;
        int nextX = this.leftPos + 155, nextY = this.topPos + 5;
        int btnW = 11, btnH = 11;

        // 判断鼠标悬停
        boolean prevHovered = mouseX >= prevX && mouseX < prevX + btnW && mouseY >= prevY && mouseY < prevY + btnH;
        boolean nextHovered = mouseX >= nextX && mouseX < nextX + btnW && mouseY >= nextY && mouseY < nextY + btnH;

        // 绘制上一页按钮
        if (prevActive) {
            int u = 18, v = 166;
            if (prevHovered) u = u+22; // 悬停
            graphics.blit(SPRITE, prevX, prevY, u, v, btnW, btnH);
        }
        // 绘制下一页按钮
        if (nextActive) {
            int u = 29, v = 166;
            if (nextHovered) u = u+22; // 悬停
            graphics.blit(SPRITE, nextX, nextY, u, v, btnW, btnH);
        }
    }

    public void renderCuriosSlots(GuiGraphics graphics, int leftPos, int topPos) {
        int xStart = 8, yStart = 18, slotSize = 18;
        List<CuriosSlotItemHandler> slots = menu.curiosSlots;
        for (int i = 0; i < slots.size(); i++) {
            CuriosSlotItemHandler slot = slots.get(i);
            if (!slot.isActive()) continue; // 跳过dummy槽
            int x = xStart + (i % 9) * slotSize;
            int y = yStart + (i / 9) * slotSize;
            ResourceLocation background = slot.getSlotIcon();
            graphics.blit(SPRITE, leftPos + x, topPos + y, 0, 166, 18, 18); // 槽底
            graphics.blit(background, leftPos + x, topPos + y, 0, 0, 18, 18, 18, 18); // 动态icon
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 151 + 2, 0x404040, false);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isInPrevButton(mouseX, mouseY) && menu.getPage() > 0) {
            sendPageChange(menu.getPage() - 1);
            return true;
        }
        if (isInNextButton(mouseX, mouseY) && menu.getPage() < menu.maxPage) {
            sendPageChange(menu.getPage() + 1);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isInPrevButton(double mouseX, double mouseY) {
        int btnX = this.leftPos + 144, btnY = this.topPos + 5;
        return mouseX >= btnX && mouseX <= btnX + 11 && mouseY >= btnY && mouseY <= btnY + 11;
    }
    private boolean isInNextButton(double mouseX, double mouseY) {
        int btnX = this.leftPos + 155, btnY = this.topPos + 5;
        return mouseX >= btnX && mouseX <= btnX + 11 && mouseY >= btnY && mouseY <= btnY + 11;
    }
    private void sendPageChange(int newPage) {
        PacketHandler.INSTANCE.sendToServer(new CuriosPagePacket(menu.containerId, newPage));
    }
    public void refresh() {
        this.init();
    }
}
