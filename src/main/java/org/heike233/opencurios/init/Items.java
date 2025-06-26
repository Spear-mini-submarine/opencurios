package org.heike233.opencurios.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.heike233.opencurios.Opencurios;
import org.heike233.opencurios.item.OpenCuriosItem;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, Opencurios.MODID);

    public static final RegistryObject<Item> OPEN_CURIOS_ITEM = ITEMS.register(
            "open_curios_item", () -> new OpenCuriosItem(new Item.Properties()));
}
