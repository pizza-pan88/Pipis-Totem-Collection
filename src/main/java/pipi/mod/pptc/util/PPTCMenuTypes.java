package pipi.mod.pptc.util;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.menu.TotemStorageMenu;

public class PPTCMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES;
	
	public static final RegistryObject<MenuType<TotemStorageMenu>> TOTEM_STORAGE;
	
	static {
		MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PPTC.MOD_ID);

		TOTEM_STORAGE = MENU_TYPES.register("totem_storage",
				() ->  IForgeMenuType.create(TotemStorageMenu::new)
		);
	}
}
