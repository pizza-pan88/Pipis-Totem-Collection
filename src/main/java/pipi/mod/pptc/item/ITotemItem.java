package pipi.mod.pptc.item;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.common.CommonHooks;

public interface ITotemItem {
	public static final byte TOTEM_EVENT_ID = 35;
	
	public static final float VANILLA_HEAL = 1.0f;
	public static final List<ConsumeEffect> VANILLA_EFFECTS =
			DeathProtection.TOTEM_OF_UNDYING.deathEffects();
	
	public static List<ConsumeEffect> effectsWithClear(MobEffectInstance effect) {
		return List.of(new ClearAllStatusEffectsConsumeEffect(), new ApplyStatusEffectsConsumeEffect(effect));
	}
	public static List<ConsumeEffect> effectsWithClear(List<MobEffectInstance> effects) {
		return List.of(new ClearAllStatusEffectsConsumeEffect(), new ApplyStatusEffectsConsumeEffect(effects));
	}
	
	/**
	 * エンティティの死亡時({@link CommonHooks#onLivingDeath(LivingEntity, DamageSource)})にcall
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 * @return トーテムを発動するかどうか
	 */
	boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage);
	
	/**
	 * 発動した際の回復量。0だと死ぬ<br>
	 * 回復は{@link LivingEntity#heal(float)}経由
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 * @return トーテム発動での回復量
	 */
	float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage);
	
	/**
	 * トーテム発動後にエンティティに付与する効果。
	 * {@link List#forEach(Consumer)}で回す
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 * @return 付与するエフェクトのリスト
	 */
	List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage);

	/**
	 * トーテムが発動した際に呼び出す。
	 * 呼び出しはITotemItem内のすべてのMethodが呼び出された後
	 * @param healAmount : 実際の回復量(≦{@link ITotemItem#healAmount})
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 */
	void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage);
	
	/**
	 * トーテムを持っている手をgetします
	 * @param entity : 対象
	 * @return トーテムを持っている手。持っていなかったらNull
	 */
	@Nullable
	public static InteractionHand getTotemHand(LivingEntity entity) {
		if(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ITotemItem) {
			return InteractionHand.MAIN_HAND;
		} else if(entity.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ITotemItem) {
			return InteractionHand.OFF_HAND;
		}
		return null;
	}
	/**
	 * 引数の条件で、発動するトーテムのgetter
	 * @param entity : 対象
	 * @param damage : ダメージ
	 * @return 発動するトーテム。持ってないなら{@link ItemStack#EMPTY}
	 */
	@Nonnull
	public static ItemStack getActivatedTotem(LivingEntity entity, DamageSource source) {
		for(InteractionHand hand : InteractionHand.values()) {
			ItemStack stack = entity.getItemInHand(hand);
			if(stack.getItem() instanceof ITotemItem totem && totem.isTotemActivated(stack, entity, source)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
