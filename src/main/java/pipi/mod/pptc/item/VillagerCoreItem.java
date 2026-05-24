package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class VillagerCoreItem extends Item {

	public VillagerCoreItem() {
		super(new Properties()
				.stacksTo(64)
				.rarity(Rarity.UNCOMMON));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltips, TooltipFlag flag) {
		var villager = EntityType.VILLAGER.getDescription().getString();
		var evoker = EntityType.EVOKER.getDescription().getString();
		tooltips.add(Component.translatable("tooltip.villager_core.looting", villager, evoker).withStyle(ChatFormatting.GRAY));
	}

	/** execute as @e[type=minecraft:evoker] run damage @e[type=minecraft:villager] 9999 magic/indirect_magic */
	public static boolean isSoulReapingAttack(DamageSource source) {
		var attacker = source.getEntity();
		var isMagic = source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC);
		return attacker instanceof Evoker && isMagic;
	}
	
}
