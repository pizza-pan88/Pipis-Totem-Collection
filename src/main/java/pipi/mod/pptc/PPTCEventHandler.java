package pipi.mod.pptc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.VillagerCoreItem;
import pipi.mod.pptc.util.PPTCDataComponents;
import pipi.mod.pptc.util.PPTCItems;

@EventBusSubscriber(modid = PPTC.MOD_ID)
public class PPTCEventHandler {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	static void onLivingDeath(LivingDeathEvent event) {
		// Mixin移行
		//var entity = event.getEntity();
		//if(PPTCHelpers.useTotem(entity, event.getSource())) {
		//	entity.animateHurt(0);
		//	event.setCanceled(true);
		//}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	static void onLivingDamaged(LivingDamageEvent.Post event) {
		// Mixin移行
		//var entity = event.getEntity();
		//if(PPTCHelpers.isDeadOrDying(entity)) {
		//	PPTCHelpers.useTotem(entity, event.getSource());
		//	//PipisHelpers.debugSide(event.getEntity());
		//}
		
		// 以降Pipi's Totemの処理
		var direct = event.getSource().getDirectEntity();
		if(direct instanceof LivingEntity attacker) {
			for(InteractionHand hand : InteractionHand.values()) {
				ItemStack stack = attacker.getItemInHand(hand);
				if(stack.is(PPTCItems.PIPI_TOTEM)) {
					float health = PipisTotemItem.getTotemHealth(stack);
					if(!PipisTotemItem.isInifinityHealth(health)) {
						float damage = event.getNewDamage();
						stack.set(PPTCDataComponents.HEALTH, health+damage);
						return;
					}
				}
			}
		}
	}

	@SubscribeEvent
	static void onLivingDrops(LivingDropsEvent event) {
		var entity = event.getEntity();
		// 大人の村人のみ(子供は敵対されない)
		if(entity instanceof Villager villager && !villager.isBaby()) {
			var source = event.getSource();
			if(VillagerCoreItem.isSoulReapingAttack(source)) {
				ItemStack villagerCore = new ItemStack(PPTCItems.VILLAGER_CORE.get());
				event.getDrops().add(PPTCHelpers.createDropItem(villagerCore, entity));
			}
		}
	}
	
	@SubscribeEvent
	static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if(left.is(PPTCItems.PIPI_TOTEM) && !PipisTotemItem.isTotemCursed(left)) {
			if(PPTCHelpers.hasStoredEnchantmentTag(right, EnchantmentTags.CURSE)) {
				ItemStack cursedTotem = left.copy();
				cursedTotem.set(PPTCDataComponents.CURSED, true);
				event.setXpCost(99);
				event.setMaterialCost(1);
				event.setOutput(cursedTotem);
				return;
			}
		}
	}
	
	public static void addBrokenTotem(LivingEntity entity) {
		ItemStack broken = new ItemStack(PPTCItems.BROKEN_TOTEM.getDelegate());
		if(entity instanceof ServerPlayer player) {
			if(!player.addItem(broken)) {
				player.drop(broken, false);
			}
		} else {
			entity.drop(broken, false, false);
		}
	}

}
