package pipi.mod.pptc.util;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.menu.TotemStorageMenu;

public class PPTCMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES;
	
	public static final Supplier<MenuType<TotemStorageMenu>> TOTEM_STORAGE;

	static {
		MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, PPTC.MOD_ID);

		TOTEM_STORAGE = MENU_TYPES.register("totem_storage",
				() ->  IMenuTypeExtension.create(TotemStorageMenu::new)
		);
	}
}
