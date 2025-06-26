package org.heike233.opencurios.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.heike233.opencurios.Opencurios;
import org.heike233.opencurios.gui.menu.CuriosMenu;

public class Menus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Opencurios.MODID);
    public static final RegistryObject<MenuType<CuriosMenu>> Curios_Menu =
            MENUS.register("curios_menu", () -> IForgeMenuType.create(CuriosMenu::new));
}
