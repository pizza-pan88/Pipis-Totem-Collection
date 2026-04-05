package pipi.mod.pptc.dataGen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.util.PPTCItems;

public class PPTCGLMProvider extends GlobalLootModifierProvider {
	
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_SERIALIZERS =
			DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PPTC.MOD_ID);

	public static final Supplier<MapCodec<ItemLootModifier>> ITEM_LOOT_MODIFIER =
			GLM_SERIALIZERS.register("item_loot_modifier", () -> ItemLootModifier.CODEC);
	
	
	public PPTCGLMProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries, PPTC.MOD_ID);
	}

	@Override
	protected void start() {
		this.add(
			"villager_drops",
			new ItemLootModifier(
				new LootItemCondition[] {
					LootTableIdCondition.builder(
						Identifier.fromNamespaceAndPath("minecraft", "entities/villager")
					).build(),
				},
				new ItemStack(PPTCItems.VILLAGER_CORE.get())
			),
			List.of()
		);
	}
}
