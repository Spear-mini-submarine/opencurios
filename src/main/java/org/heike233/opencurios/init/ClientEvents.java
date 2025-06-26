package org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.Touhou_little_maid_irons_spells_n_spellbooks;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.client.gui.screen.CuriosScreen;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.comm.Menus;

@Mod.EventBusSubscriber(modid = Touhou_little_maid_irons_spells_n_spellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Menus.Curios_Menu.get(), CuriosScreen::new);
        });
    }
}
