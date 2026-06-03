package pipi.mod.pptc.util;

import java.util.List;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.TORItem;
import pipi.mod.pptc.item.Totem871Item;
import pipi.mod.pptc.item.TotemStorageItem;
import pipi.mod.pptc.item.VanillaTotemItem;
import pipi.mod.pptc.item.VillagerCoreItem;

public class PPTCItems {
	public static final DeferredRegister<Item> ITEMS;

	// アイテム
	public static final RegistryObject<Item> BROKEN_TOTEM;
	public static final RegistryObject<Item> VILLAGER_CORE;
	public static final RegistryObject<Item> TOTEM_OF_CHOCOLATE;
	public static final RegistryObject<Item> TOTEM_STORAGE;
	// 死なないやつ
	public static final RegistryObject<Item> PIPI_TOTEM;
	public static final RegistryObject<Item> TOTEM_OF_RETURN;
	public static final RegistryObject<Item> TOTEM_871;
	public static final RegistryObject<Item> TOTEM_OF_BEDROCK;
	public static final RegistryObject<Item> DIRTEM;

	static {
		ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PPTC.MOD_ID);
		
		BROKEN_TOTEM = ITEMS.register("broken_totem", () -> new Item(new Properties().stacksTo(64).rarity(Rarity.RARE)));
		VILLAGER_CORE = ITEMS.register("villager_core", VillagerCoreItem::new);
		TOTEM_OF_CHOCOLATE = ITEMS.register("totem_of_chocolate", () -> new Item(new Properties().stacksTo(64).rarity(Rarity.RARE)
				.food(new FoodProperties.Builder().nutrition(FoodConstants.MAX_FOOD).saturationMod(FoodConstants.MAX_SATURATION).build()))
		);
		TOTEM_STORAGE = ITEMS.register("totem_storage", TotemStorageItem::new);
		
		PIPI_TOTEM = ITEMS.register("pipis_totem", PipisTotemItem::new);
		TOTEM_OF_RETURN = ITEMS.register("totem_of_return", TORItem::new);
		TOTEM_871 = ITEMS.register("totem_871", Totem871Item::new);
		TOTEM_OF_BEDROCK = ITEMS.register("totem_of_bedrock", () -> new VanillaTotemItem(new Properties().rarity(Rarity.UNCOMMON)) {
			public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return true;
			}
			public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return List.of(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 255));
			}
		});
		DIRTEM = ITEMS.register("dirtem", () -> new VanillaTotemItem(new Properties().rarity(Rarity.UNCOMMON)) {
			public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return null;
			}
			public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				super.onTotemUsed(healAmount, totem, willBeDead, damage);
				willBeDead.removeAllEffects();
				ForgeRegistries.MOB_EFFECTS.forEach(t -> {
					if(t.isBeneficial()) willBeDead.addEffect(new MobEffectInstance(t, 6000, 4));
				});
			}
		});
	}
}
