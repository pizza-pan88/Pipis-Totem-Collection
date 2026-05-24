package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.util.PPTCItems;
import pipi.mod.pptc.util.PPTCTagKeys;
import pipi.mod.pptc.util.PPTCTooltips;

public class PipisTotemItem extends Item implements ITotemItem {
	
	public PipisTotemItem() {
		super(new Properties()
				.stacksTo(1)
				.rarity(Rarity.EPIC)
				);
	}

	public static boolean isTotemCursed(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag == null ? false : tag.getBoolean(PPTCTagKeys.KEY_CURSED);
	}
	
	public static boolean isInifinityHealth(float health) {
		return health >= Integer.MAX_VALUE;
	}
	
	public static float getTotemHealth(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag == null ? 0f : tag.getFloat(PPTCTagKeys.KEY_HEALTH);
	}
	public static String getTotemHealthString(ItemStack stack, long seed) {
		float health = getTotemHealth(stack);
		if(isInifinityHealth(health)) {
			return PPTCTooltips.toRainbow("Infinity", seed);
		}
		return PPTC.FORMATTER.format(health);
	}
	
	public static ItemStack getTotemStack(float customHealth) {
		var stack = new ItemStack(PPTCItems.PIPI_TOTEM.get());
		stack.getOrCreateTag().putFloat(PPTCTagKeys.KEY_HEALTH, customHealth);
		return stack;
	}
	public static ItemStack getCursedTotemStack(float customHealth) {
		var stack = getTotemStack(customHealth);
		stack.getOrCreateTag().putBoolean(PPTCTagKeys.KEY_CURSED, true);
		return stack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltips, TooltipFlag flag) {
		String value = getTotemHealthString(stack, level == null ? 0 : level.dayTime());
		tooltips.add(Component.translatable("tooltip.pipis_totem.health", value).withStyle(ChatFormatting.GRAY));
		if(isTotemCursed(stack)) {
			tooltips.add(
				Component.translatable("tooltip.pipis_totem.cursed").withStyle(ChatFormatting.DARK_RED)
			);
		}
	}
	
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		stack.getOrCreateTag().putFloat(PPTCTagKeys.KEY_HEALTH, 20.0f);
	}
	
	public boolean isFoil(ItemStack stack) {
		return isTotemCursed(stack);
	}

	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return getTotemHealth(totem) > 0f;
	}

	@Override
	public float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		float heal = isTotemCursed(totem) ? 1.0f : (willBeDead.getMaxHealth() - willBeDead.getHealth());
		return Math.min(heal, getTotemHealth(totem));
	}

	@Override
	public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return null;
	}

	@Override
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		float totemHealth = getTotemHealth(totem);
		if(!isInifinityHealth(totemHealth))
			totem.getOrCreateTag().putFloat(PPTCTagKeys.KEY_HEALTH, totemHealth - healAmount);
		willBeDead.invulnerableTime = isTotemCursed(totem) ? 0 : 100;
	}
	
}
