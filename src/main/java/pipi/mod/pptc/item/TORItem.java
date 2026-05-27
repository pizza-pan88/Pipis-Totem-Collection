package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.portal.TeleportTransition;

public class TORItem extends Item implements ITotemItem {

	public TORItem(Identifier identifier) {
		super(new Properties()
				.stacksTo(1)
				.rarity(Rarity.RARE)
				.setId(ResourceKey.create(Registries.ITEM, identifier))
				);
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
	public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_EFFECTS;
	}

	@Override
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof ServerPlayer player) {
			// もっといいのあった
			/*RespawnData respawnData = (player.getRespawnConfig() == null) ?
					player.level().getRespawnData() : player.getRespawnConfig().respawnData();
			ServerLevel dimention = player.level().getServer().getLevel(respawnData.dimension());
			BlockPos spawnPoint = respawnData.pos();
			player.findRespawnPositionAndUseSpawnBlock(!player.isCreative(), null);
			Vec3 goal = Vec3.atCenterOf(spawnPoint);
			TeleportTransition to = new TeleportTransition(
				dimention, goal, player.getDeltaMovement(),
				player.getYRot(), player.getXRot(),
				TeleportTransition.DO_NOTHING
			);*/
			
			boolean isCretive = player.isCreative();
			// まさかのこれでよかった
			TeleportTransition to = player.findRespawnPositionAndUseSpawnBlock(
					!isCretive, TeleportTransition.DO_NOTHING
			);
			player.teleport(to);
			if(isCretive) return;
		}
		totem.shrink(1);
	}

}
