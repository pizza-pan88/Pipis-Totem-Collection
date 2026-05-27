package pipi.mod.pptc.item;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class VillagerCoreItem extends Item {

	public VillagerCoreItem(Identifier identifier) {
		super(new Properties()
				.stacksTo(64)
				.rarity(Rarity.UNCOMMON)
				.setId(ResourceKey.create(Registries.ITEM, identifier))
				);
	}

	@Override
	@Deprecated
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		var villager = EntityType.VILLAGER.getDescription().getString();
		var evoker = EntityType.EVOKER.getDescription().getString();
		tooltipAdder.accept(Component.translatable("tooltip.villager_core.looting", villager, evoker).withStyle(ChatFormatting.GRAY));
	}

	/** execute as @e[type=minecraft:evoker] run damage @e[type=minecraft:villager, limit=1] 9999 magic/indirect_magic */
	public static boolean isSoulReapingAttack(DamageSource source) {
		var attacker = source.getEntity();
		var direct = source.getDirectEntity();
		var isMagic = source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC);
		return isMagic && (attacker instanceof Evoker ||  direct instanceof Evoker);
	}
	
}
