package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VanillaTotemItem extends Item implements ITotemItem {

	public VanillaTotemItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return !damage.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
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
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof Player p && p.isCreative()) return;
		
		totem.shrink(1);
	}

}
