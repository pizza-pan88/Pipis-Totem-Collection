package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class Totem871Item extends Item implements ITotemItem {

	public Totem871Item() {
		super(new Properties()
				.stacksTo(64)
				.rarity(Rarity.UNCOMMON));
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean selected) {
		int count = stack.getCount();
		int limit = stack.getMaxStackSize();
		if(count >= limit) return;

		// 1.21.11と合わせるため
		int chance = getChance(20);
		if(level.random.nextInt(chance) == 0) {
			int amout = count << 1;
			stack.setCount(Math.min(amout, limit));
		}
	}
	
	private int getChance(float tickRate) {
		return tickRate > 0 ? (int)(20f/tickRate * Level.TICKS_PER_DAY) : 0;
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
	public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_EFFECTS;
	}

	@Override
	public void onTotemUse(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof Player player && player.isCreative())
			return;
		totem.shrink(1);
	}

}
