package pipi.mod.pptc.util;

import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.ITotemItem;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.TORItem;
import pipi.mod.pptc.item.Totem871Item;
import pipi.mod.pptc.item.TotemStorageItem;
import pipi.mod.pptc.item.VanillaTotemItem;
import pipi.mod.pptc.item.VillagerCoreItem;

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
	public static final DeferredItem<Item> TOTEM_STORAGE;
	public static final DeferredItem<Item> TOTEM_OF_BEDROCK;
	public static final DeferredItem<Item> DIRTEM;

	static {
		ITEMS = DeferredRegister.createItems(PPTC.MOD_ID);
		
		BROKEN_TOTEM = ITEMS.registerSimpleItem("broken_totem", () -> new Properties().stacksTo(64).rarity(Rarity.RARE));
		VILLAGER_CORE = ITEMS.register("villager_core", VillagerCoreItem::new);
		TOTEM_OF_CHOCOLATE = ITEMS.registerSimpleItem("totem_of_chocolate", () -> new Properties().stacksTo(64).rarity(Rarity.RARE)
				.food(new FoodProperties(FoodConstants.MAX_FOOD, FoodConstants.MAX_SATURATION, false))
		);
		
		PIPI_TOTEM = ITEMS.register("pipis_totem", PipisTotemItem::new);
		TOTEM_OF_RETURN = ITEMS.register("totem_of_return", TORItem::new);
		TOTEM_871 = ITEMS.register("totem_871", Totem871Item::new);
		TOTEM_STORAGE = ITEMS.register("totem_storage", TotemStorageItem::new);
		TOTEM_OF_BEDROCK = ITEMS.register("totem_of_bedrock", (id) -> new VanillaTotemItem(id, new Properties().rarity(Rarity.UNCOMMON)) {
			public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return true;
			}
			public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return ITotemItem.effectsWithClear(new MobEffectInstance(MobEffects.RESISTANCE, 1200, 255));
			}
		});
		DIRTEM = ITEMS.register("dirtem", (id) -> new VanillaTotemItem(id, new Properties().rarity(Rarity.UNCOMMON)) {
			public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				return null;
			}
			public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
				super.onTotemUsed(healAmount, totem, willBeDead, damage);
				willBeDead.removeAllEffects();
				BuiltInRegistries.MOB_EFFECT.forEach(t -> {
					if(t.isBeneficial()) {
						Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(t);
						willBeDead.addEffect(new MobEffectInstance(effect, 6000, 255));
					}
				});
			}
		});
	}
}
