package pipi.mod.pptc.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class Totem871Item extends Item implements ITotemItem {

	public Totem871Item(Identifier identifier) {
		super(new Properties()
				.stacksTo(64)
				.rarity(Rarity.UNCOMMON)
				.setId(ResourceKey.create(Registries.ITEM, identifier))
				);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
		int count = stack.getCount();
		int limit = stack.getMaxStackSize();
		if(count >= limit) return;
		
		// この計算重そう(偏見)
		int chance = getChance(level.tickRateManager().tickrate());
		if(level.random.nextInt(chance) == 0) {
			int amout = count << 1;
			stack.setCount(Math.min(amout, limit));
		}
	}
	
	private int getChance(float tickRate) {
		return tickRate > 0 ? (int)(20f/tickRate * 24000) : 0;
	}
    
	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return true;
	}

	@Override
	public float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_HEAL;
	}

	@Override
	public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_EFFECTS;
	}

	@Override
	public void onTotemUse(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof Player player && player.isCreative())
			return;
		totem.shrink(1);
	}

}
