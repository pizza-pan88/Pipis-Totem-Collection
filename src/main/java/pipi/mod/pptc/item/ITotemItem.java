package pipi.mod.pptc.item;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public interface ITotemItem {
	public static final byte TOTEM_EVENT_ID = 35;
	
	public static final float VANILLA_HEAL = 1.0f;
	public static final List<MobEffectInstance> VANILLA_EFFECTS = List.of(
			new MobEffectInstance(MobEffects.REGENERATION, 900, 1),
			new MobEffectInstance(MobEffects.ABSORPTION, 100, 1),
			new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0)
	);
	
	/**
	 * エンティティの死亡時にcall. タイミングは以下の２通り
	 * <br> - {@link ForgeHooks#onLivingDeath(LivingEntity, DamageSource)}が呼び出される
	 * <br> - {@link ForgeHooks#onLivingDamage(LivingEntity, DamageSource, float)}が呼び出され、HPが0以下の場合
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 * @return トーテムを発動するかどうか
	 */
	boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage);
	
	/**
	 * 発動した際の回復量。0だと死ぬ<br>
	 * 回復は{@link LivingEntity#setHealth(float)}経由
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
	List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage);

	/**
	 * トーテムが発動した際に呼び出す。
	 * 呼び出しはITotemItem内のすべてのMethodが呼び出された後
	 * @param healAmount : 実際の回復量(≦{@link ITotemItem#healAmount})
	 * @param totem : トーテム
	 * @param willBeDead : トーテムの効果対象
	 * @param damage: エンティティが死亡する要因となったダメージ
	 */
	void onTotemUse(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage);
	
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
}
