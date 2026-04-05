package pipi.mod.pptc.dataGen;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.util.PPTCDataComponents;
import pipi.mod.pptc.util.PPTCItems;

public class PPTCAdvancementGenerator implements AdvancementSubProvider {

	@Override
	public void generate(Provider registries, Consumer<AdvancementHolder> writer) {
		//root
		Advancement.Builder.advancement()
		.display(Items.TOTEM_OF_UNDYING,
				Component.translatable(title("origin")),
				Component.translatable(description("origin")),
				Identifier.fromNamespaceAndPath("minecraft", "block/bedrock"),
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TOTEM_OF_UNDYING)
		).requirements(AdvancementRequirements.allOf(List.of("get_totem"))
		).save(writer, PPTC.locate("origin"));
		//broke totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("origin").toString()))
		.display(PPTCItems.BROKEN_TOTEM,
				Component.translatable(title("broken")),
				Component.translatable(description("broken")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("broken_totem", UsedTotemTrigger.TriggerInstance.usedTotem(
				ItemPredicate.Builder.item().build()
		)).requirements(AdvancementRequirements.allOf(List.of("broken_totem"))
		).save(writer, PPTC.locate("broken"));
		//totem of return
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_RETURN,
				Component.translatable(title("tor")),
				Component.translatable(description("tor")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_RETURN)
		).requirements(AdvancementRequirements.allOf(List.of("get_totem"))
		).save(writer, PPTC.locate("tor"));
		//totem 871
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_871,
				Component.translatable(title("ttm_871")),
				Component.translatable(description("ttm_871")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_871)
		).requirements(AdvancementRequirements.allOf(List.of("get_totem"))
		).save(writer, PPTC.locate("ttm_871"));
		//totem of chocolate
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_CHOCOLATE,
				Component.translatable(title("chocolate")),
				Component.translatable(description("chocolate")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_CHOCOLATE)
		).requirements(AdvancementRequirements.allOf(List.of("get_totem"))
		).save(writer, PPTC.locate("chocolate"));
		//villager core
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.VILLAGER_CORE,
				Component.translatable(title("villager")),
				Component.translatable(description("villager")),
				null,
				AdvancementType.GOAL,
				true, true, false
		).addCriterion("take_in_villager", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.VILLAGER_CORE)
		).requirements(AdvancementRequirements.allOf(List.of("take_in_villager"))
		).save(writer, PPTC.locate("villager_core"));
		//pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("villager_core").toString()))
		.display(PPTCItems.PIPI_TOTEM,
				Component.translatable(title("pipi")),
				Component.translatable(description("pipi")),
				null,
				AdvancementType.GOAL,
				true, true, false
		).addCriterion("pipi", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.PIPI_TOTEM)
		).requirements(AdvancementRequirements.allOf(List.of("pipi"))
		).save(writer, PPTC.locate("pipi_totem"));
		//cursed pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("pipi_totem").toString()))
		.display(
				PipisTotemItem.getCursedTotemStack(0),
				Component.translatable(title("cursed_pipi")),
				Component.translatable(description("cursed_pipi")),
				null,
				AdvancementType.CHALLENGE,
				true, true, true
		).addCriterion("cursed_pipi", InventoryChangeTrigger.TriggerInstance.hasItems(
				new ItemPredicate.Builder()
				.of(registries.lookupOrThrow(Registries.ITEM), PPTCItems.PIPI_TOTEM)
				.withComponents(DataComponentMatchers.Builder.components()
						.exact(DataComponentExactPredicate.expect(
								PPTCDataComponents.CURSED.get(), true
						)).build()
				).build())
		).requirements(AdvancementRequirements.allOf(List.of("cursed_pipi"))
		).save(writer, PPTC.locate("cursed_pipi"));
	}
	
	static String title(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".title";
	}
	static String description(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".description";
	}

}
