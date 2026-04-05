package pipi.mod.pptc.dataGen;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.util.PPTCItems;

public class PPTCGLMProvider extends GlobalLootModifierProvider {
	
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PPTC.MOD_ID);

	public static final Supplier<Codec<ItemLootModifier>> ITEM_LOOT_MODIFIER =
			GLM_SERIALIZERS.register("item_loot_modifier", () -> ItemLootModifier.CODEC);
	
	
	public PPTCGLMProvider(PackOutput output) {
		super(output, PPTC.MOD_ID);
	}

	@Override
	protected void start() {
		this.add(
			"villager_drops",
			new ItemLootModifier(
				new LootItemCondition[] {
					LootTableIdCondition.builder(
						ResourceLocation.fromNamespaceAndPath("minecraft", "entities/villager")
					).build(),
				},
				new ItemStack(PPTCItems.VILLAGER_CORE.get())
			)
		);
	}
}
