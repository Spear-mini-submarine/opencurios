package org.heike233.opencurios.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.heike233.opencurios.Opencurios;
import org.heike233.opencurios.gui.screen.CuriosScreen;

@Mod.EventBusSubscriber(modid = Opencurios.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Menus.Curios_Menu.get(), CuriosScreen::new);
        });
    }
}
