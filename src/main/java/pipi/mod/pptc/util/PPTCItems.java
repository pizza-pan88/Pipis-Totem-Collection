package pipi.mod.pptc.util;

import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.TORItem;
import pipi.mod.pptc.item.Totem871Item;

public class PPTCItems {
	public static final DeferredRegister.Items ITEMS;

	// アイテム
	public static final DeferredItem<Item> BROKEN_TOTEM;
	public static final DeferredItem<Item> VILLAGER_CORE;
	public static final DeferredItem<Item> TOTEM_OF_CHOCOLATE;
	// 死なないやつ
	public static final DeferredItem<Item> PIPI_TOTEM;
	public static final DeferredItem<Item> TOTEM_OF_RETURN;
	public static final DeferredItem<Item> TOTEM_871;

	static {
		ITEMS = DeferredRegister.createItems(PPTC.MOD_ID);
		
		BROKEN_TOTEM = ITEMS.registerSimpleItem("broken_totem", () -> new Properties().stacksTo(64).rarity(Rarity.RARE));
		VILLAGER_CORE = ITEMS.registerSimpleItem("villager_core", () -> new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
		TOTEM_OF_CHOCOLATE = ITEMS.registerSimpleItem("totem_of_chocolate", () -> new Properties().stacksTo(64).rarity(Rarity.RARE)
				.food(new FoodProperties(FoodConstants.MAX_FOOD, FoodConstants.MAX_SATURATION, false))
		);
		
		PIPI_TOTEM = ITEMS.register("pipis_totem", PipisTotemItem::new);
		TOTEM_OF_RETURN = ITEMS.register("totem_of_return", TORItem::new);
		TOTEM_871 = ITEMS.register("totem_871", Totem871Item::new);
	}
}
