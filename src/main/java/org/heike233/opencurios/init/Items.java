package org.heike233.opencurios;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.Touhou_little_maid_irons_spells_n_spellbooks;
import org.heike233.touhou_little_maid_irons_spells_n_spellbooks.comm.item.OpenCuriosItem;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, Touhou_little_maid_irons_spells_n_spellbooks.MODID);

    public static final RegistryObject<Item> OPEN_CURIOS_ITEM = ITEMS.register(
            "open_curios_item", () -> new OpenCuriosItem(new Item.Properties()));
}
