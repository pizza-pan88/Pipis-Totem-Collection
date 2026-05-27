package pipi.mod.pptc;

import java.lang.StackWalker.StackFrame;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import pipi.mod.pptc.item.ITotemItem;

public class PPTCHelpers {
	
	public static boolean isDeadOrDying(LivingEntity entity) {
		return entity.getHealth() <= 0f || entity.isDeadOrDying();
	}

	public static boolean useTotem(LivingEntity entity, DamageSource damage) {
		if(entity == null || entity.level().isClientSide()) return false;
		ServerLevel server = (ServerLevel)entity.level();
		
		// これでは、メインハンドに発動しないトーテムを持っていた場合、オフハンドの確認をせずに終わって仕舞う
		//var totemHand = ITotemItem.getTotemHand(entity);
		//if(totemHand == null) return false;
		var totemStack = ITotemItem.getActivatedTotem(entity, damage);
		if(totemStack.isEmpty()) return false;
		
		ITotemItem totem = (ITotemItem)totemStack.getItem();
		float heal = totem.healAmount(totemStack, entity, damage);
		float preHealth = entity.getHealth();
		entity.setHealth(preHealth+heal);
			
		var effects = totem.getEffects(totemStack, entity, damage);
		if(effects != null)
			effects.forEach(effect -> effect.apply(server, totemStack, entity));
			
		if(entity instanceof ServerPlayer player) {
			player.awardStat(Stats.ITEM_USED.get(totemStack.getItem()));
			CriteriaTriggers.USED_TOTEM.trigger(player, totemStack);
		}
		
		float healedAmount = entity.getHealth() - preHealth;
		// トーテム使用の処理を少し後ろへ
		totem.onTotemUsed(healedAmount, totemStack, entity, damage);
		server.playSound(null, entity.blockPosition(), SoundEvents.TOTEM_USE, entity.getSoundSource());
		var speed = entity.getDeltaMovement();
		for(ServerPlayer player : server.players()) {
			server.sendParticles(
					player, ParticleTypes.TOTEM_OF_UNDYING, false, false,
					entity.getX(), entity.getY(0.5f), entity.getZ(),
					128,
					speed.x, speed.y, speed.z, 1);
		}
		//PipisHelpers.debugSide(entity);
		return true;
	}
	
	// ﾅﾏｴﾅｶﾞ
	public static boolean hasStoredEnchantmentTag(ItemStack stack, TagKey<Enchantment> tag) {
		ItemEnchantments storedEnchantments = stack.getOrDefault(
			DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY
		);
		
		for (Entry<Holder<Enchantment>> entry : storedEnchantments.entrySet()) {
			Holder<Enchantment> holder = entry.getKey();
			if (holder.is(tag)) {
				return true;
			}
		}
		return false;
	}

	public static ItemEntity createDropItem(ItemStack item, LivingEntity entity) {
		ItemEntity itemEntity = new ItemEntity(
				entity.level(),
				entity.getX(), entity.getY(0.5), entity.getZ(),
				item
		);
		itemEntity.setDefaultPickUpDelay();
		itemEntity.setDeltaMovement(entity.getDeltaMovement());
		return itemEntity;
	}
	
	public static void debugSide(LivingEntity e) {
		if(e instanceof Player p) {
			StackFrame sf = getStackFrames().get(2); //debugSideとgetStackFramesを除く
			String method = sf.getClassName()+"#"+sf.getMethodName();
			String side = p.level().isClientSide() ? "client" : "server";
			p.displayClientMessage(Component.literal(method+":"+side), false);
		}
	}
	
	public static List<StackFrame> getStackFrames(int limit, Predicate<StackFrame> filter) {
		return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
				.walk(s -> s.filter(filter).limit(limit).collect(Collectors.toList()));
	}
	public static List<StackFrame> getStackFrames(int limit) {
		return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
				.walk(s -> s.limit(limit).collect(Collectors.toList()));
	}
	public static List<StackFrame> getStackFrames() {
		return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
				.walk(s -> s.collect(Collectors.toList()));
	}
}
