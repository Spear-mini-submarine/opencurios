package org.heike233.opencurios;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.Touhou_little_maid_irons_spells_n_spellbooks;

@Mod.EventBusSubscriber(modid = Touhou_little_maid_irons_spells_n_spellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventBusEvents {
    @SubscribeEvent
    public static void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            event.accept(Items.OPEN_CURIOS_ITEM.get());
        }
    }
}