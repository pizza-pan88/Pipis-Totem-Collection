package pipi.mod.pptc.item;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.util.PPTCDataComponents;
import pipi.mod.pptc.util.PPTCItems;

public class PipisTotemItem extends Item implements ITotemItem {
	
	public PipisTotemItem(Identifier identifier) {
		super(new Properties()
				.stacksTo(1)
				.rarity(Rarity.EPIC)
				.component(PPTCDataComponents.HEALTH, 0f)
				.component(PPTCDataComponents.CURSED, false)
				.setId(ResourceKey.create(Registries.ITEM, identifier))
				);
	}

	public static boolean isTotemCursed(ItemStack stack) {
		return stack.getOrDefault(PPTCDataComponents.CURSED, false);
	}
	
	public static boolean isInifinityHealth(float health) {
		return health >= Integer.MAX_VALUE;
	}
	
	public static float getTotemHealth(ItemStack stack) {
		return stack.getOrDefault(PPTCDataComponents.HEALTH, 0f);
	}
	
	public static ItemStack getTotemStack(float customHealth) {
		var stack = new ItemStack(PPTCItems.PIPI_TOTEM.get());
		stack.set(PPTCDataComponents.HEALTH, customHealth);
		return stack;
	}
	public static ItemStack getCursedTotemStack(float customHealth) {
		var stack = getTotemStack(customHealth);
		stack.set(PPTCDataComponents.CURSED, true);
		return stack;
	}
	
	@Deprecated
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		float health = getTotemHealth(stack);
		String value = isInifinityHealth(health) ? "Infinity" : PPTC.FORMATTER.format(health);
		tooltipAdder.accept(Component.translatable("tooltip.pipis_totem.health", value));
		if(isTotemCursed(stack)) {
			tooltipAdder.accept(
				Component.translatable("tooltip.pipis_totem.cursed").withStyle(ChatFormatting.DARK_RED)
			);
		}
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
	public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return null;
	}

	@Override
	public void onTotemUse(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		float totemHealth = getTotemHealth(totem);
		if(!isInifinityHealth(totemHealth))
			totem.set(PPTCDataComponents.HEALTH, totemHealth - healAmount);
		if(!isTotemCursed(totem))
			willBeDead.invulnerableTime = 400;
	}
	
}
