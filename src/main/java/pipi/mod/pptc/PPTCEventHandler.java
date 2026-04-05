package pipi.mod.pptc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingUseTotemEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.util.PPTCItems;
import pipi.mod.pptc.util.PPTCTagKeys;

@EventBusSubscriber(modid = PPTC.MOD_ID)
public class PPTCEventHandler {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	static void onLivingDeath(LivingDeathEvent event) {
		var entity = event.getEntity();
		if(PPTCHelpers.useTotem(entity, event.getSource())) {
			entity.animateHurt(0);
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	static void onLivingDamaged(LivingDamageEvent event) {
		var entity = event.getEntity();
		if(PPTCHelpers.isDeadOrDying(entity)) {
			PPTCHelpers.useTotem(entity, event.getSource());
		}
		
		// 以降Pipi's Totemの処理
		var direct = event.getSource().getDirectEntity();
		if(direct instanceof LivingEntity attacker) {
			for(InteractionHand hand : InteractionHand.values()) {
				ItemStack stack = attacker.getItemInHand(hand);
				if(stack.is(PPTCItems.PIPI_TOTEM.get())) {
					float health = PipisTotemItem.getTotemHealth(stack);
					if(!PipisTotemItem.isInifinityHealth(health)) {
						float damage = event.getAmount();
						stack.getOrCreateTag()
						.putFloat(PPTCTagKeys.KEY_HEALTH, health+damage);
						return;
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if(left.is(PPTCItems.PIPI_TOTEM.get()) && !PipisTotemItem.isTotemCursed(left)) {
			if(PPTCHelpers.hasStoredEnchantmentTag(right, t -> t.isCurse())) {
				ItemStack cursedTotem = left.copy();
				cursedTotem.getOrCreateTag().putBoolean(PPTCTagKeys.KEY_CURSED, true);
				event.setCost(99);
				event.setMaterialCost(1);
				event.setOutput(cursedTotem);
				return;
			}
		}
	}
	
	@SubscribeEvent
	static void onUsedTotem(LivingUseTotemEvent event) {
		LivingEntity entity = event.getEntity();
		ItemStack broken = new ItemStack(PPTCItems.BROKEN_TOTEM.get());
		if(entity instanceof ServerPlayer player) {
			player.addItem(broken);
		} else {
			Level level = entity.level();
			ItemEntity itemEntity = EntityType.ITEM.create(level);
			itemEntity.setItem(broken);
			itemEntity.setDefaultPickUpDelay();
			itemEntity.setPos(entity.getX(), entity.getY(0.5), entity.getZ());
			itemEntity.setDeltaMovement(entity.getDeltaMovement());
			level.addFreshEntity(itemEntity);
		}
	}

}
