package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TORItem extends Item implements ITotemItem {

	public TORItem() {
		super(new Properties()
				.stacksTo(1)
				.rarity(Rarity.RARE));
	}

	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return true;
	}

	@Override
	public float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_HEAL;
	}

	@Override
	public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return null;
	}

	@Override
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof ServerPlayer player) {
			BlockPos spawnPoint = player.getRespawnPosition();
			ResourceKey<Level> dimentionKey = (spawnPoint == null) ?
					Level.OVERWORLD : player.getRespawnDimension();
			ServerLevel dimention = player.level().getServer().getLevel(dimentionKey);
			Vec3 spawnIn = (spawnPoint == null) ? Vec3.atCenterOf(player.level().getSharedSpawnPos()) :
					Player.findRespawnPositionAndUseSpawnBlock(
							dimention, spawnPoint, player.getRespawnAngle(), player.isRespawnForced(), player.isCreative()
					).orElse(Vec3.atCenterOf(spawnPoint));
			player.teleportTo(
					dimention, spawnIn.x(), spawnIn.y(), spawnIn.z(),
					player.getYRot(), player.getXRot()
			);
			player.removeAllEffects();
			VANILLA_EFFECTS.forEach(effect -> player.addEffect(new MobEffectInstance(effect)));
			if(player.isCreative()) return;
		}
		totem.shrink(1);
	}

}
