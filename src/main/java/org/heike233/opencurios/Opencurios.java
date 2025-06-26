package org.heike233.opencurios;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.heike233.opencurios.init.Items;
import org.heike233.opencurios.init.Menus;
import org.heike233.opencurios.network.PacketHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Opencurios.MODID)
public class Opencurios {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "opencurios";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Opencurios() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Items.ITEMS.register(modEventBus);
        Menus.MENUS.register(modEventBus);
        PacketHandler.registerPackets();
    }
}
