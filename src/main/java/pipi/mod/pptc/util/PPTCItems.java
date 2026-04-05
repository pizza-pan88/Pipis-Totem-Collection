package pipi.mod.pptc.util;

import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.TORItem;
import pipi.mod.pptc.item.Totem871Item;

public class PPTCItems {
	public static final DeferredRegister<Item> ITEMS;

	// アイテム
	public static final RegistryObject<Item> BROKEN_TOTEM;
	public static final RegistryObject<Item> VILLAGER_CORE;
	public static final RegistryObject<Item> TOTEM_OF_CHOCOLATE;
	// 死なないやつ
	public static final RegistryObject<Item> PIPI_TOTEM;
	public static final RegistryObject<Item> TOTEM_OF_RETURN;
	public static final RegistryObject<Item> TOTEM_871;

	static {
		ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PPTC.MOD_ID);
		
		BROKEN_TOTEM = ITEMS.register("broken_totem", () -> new Item(new Properties().stacksTo(64).rarity(Rarity.RARE)));
		VILLAGER_CORE = ITEMS.register("villager_core", () -> new Item(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));
		TOTEM_OF_CHOCOLATE = ITEMS.register("totem_of_chocolate", () -> new Item(new Properties().stacksTo(64).rarity(Rarity.RARE)
				.food(new FoodProperties.Builder().nutrition(FoodConstants.MAX_FOOD).saturationMod(FoodConstants.MAX_SATURATION).build()))
		);
		
		PIPI_TOTEM = ITEMS.register("pipis_totem", PipisTotemItem::new);
		TOTEM_OF_RETURN = ITEMS.register("totem_of_return", TORItem::new);
		TOTEM_871 = ITEMS.register("totem_871", Totem871Item::new);
	}
}
