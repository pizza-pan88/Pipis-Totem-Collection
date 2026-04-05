package pipi.mod.pptc.dataGen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ItemLootModifier extends LootModifier {
	public static final MapCodec<ItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> 
	LootModifier.codecStart(inst).and(
			ItemStack.CODEC.fieldOf("item").forGetter(e -> e.item)
	).apply(inst, ItemLootModifier::new)
);
	
	private final ItemStack item;
	public ItemLootModifier(LootItemCondition[] conditionsIn, ItemStack stack) {
		super(conditionsIn);
		this.item = stack;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(item);
		return generatedLoot;
	}

}
