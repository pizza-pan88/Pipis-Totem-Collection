package pipi.mod.pptc.dataGen;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.advancements.criterion.RecipeCraftedTrigger;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
		.display(PPTCItems.PIPI_TOTEM,
				Component.translatable(title("root")),
				Component.translatable(description("root")),
				Identifier.fromNamespaceAndPath("minecraft", "block/bedrock"),
				AdvancementType.TASK,
				false, false, false
		).addCriterion("on_tick", PlayerTrigger.TriggerInstance.tick())
		// 下記2行どちらも同じ意味。ANDの場合は指定不要
		//.requirements(AdvancementRequirements.Strategy.AND)
		//.requirements(AdvancementRequirements.allOf(List.of("on_tick")))
		.save(writer, PPTC.locate("root"));
		
		//totem of undying
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("root")))
		.display(Items.TOTEM_OF_UNDYING,
				Component.translatable(title("collect_totem")),
				Component.translatable(description("collect_totem")),
				null,
				AdvancementType.GOAL,
				true, true, false
		).addCriterion("collect_totem", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TOTEM_OF_UNDYING))
		.save(writer, PPTC.locate("origin"));
		
		//broke totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("origin")))
		.display(PPTCItems.BROKEN_TOTEM,
				Component.translatable(title("break_totem")),
				Component.translatable(description("break_totem")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("broken_totem", UsedTotemTrigger.TriggerInstance.usedTotem(
				ItemPredicate.Builder.item().build()
		)).save(writer, PPTC.locate("broken"));
		
		//totem of return
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("broken")))
		.display(PPTCItems.TOTEM_OF_RETURN,
				Component.translatable(title("collect_tor")),
				Component.translatable(description("collect_tor")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_RETURN))
		.save(writer, PPTC.locate("tor"));
		
		//totem 871
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("broken")))
		.display(PPTCItems.TOTEM_871,
				Component.translatable(title("collect_ttm_871")),
				Component.translatable(description("collect_ttm_871")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_871))
		.save(writer, PPTC.locate("ttm_871"));
		
		//totem of chocolate
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("broken")))
		.display(PPTCItems.TOTEM_OF_CHOCOLATE,
				Component.translatable(title("collect_chocotem")),
				Component.translatable(description("collect_chocotem")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_CHOCOLATE))
		.save(writer, PPTC.locate("chocolate"));
		
		//villager core
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("broken")))
		.display(PPTCItems.VILLAGER_CORE,
				Component.translatable(title("villager")),
				Component.translatable(description("villager")),
				null,
				AdvancementType.GOAL,
				true, true, false
		).addCriterion("take_in_villager", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.VILLAGER_CORE))
		.save(writer, PPTC.locate("villager_core"));
		
		//repair totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("villager_core")))
		.display(PPTCItems.VILLAGER_CORE.get(),
				Component.translatable(title("repair_totem")),
				Component.translatable(description("repair_totem")),
				null,
				AdvancementType.CHALLENGE,
				true, true, false
		).addCriterion("repair_totem", RecipeCraftedTrigger.TriggerInstance.craftedItem(
				ResourceKey.create(Registries.RECIPE, PPTC.locate("totem_of_undying"))
		)).save(writer, PPTC.locate("repair_totem"));
		
		//pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("villager_core")))
		.display(PPTCItems.PIPI_TOTEM,
				Component.translatable(title("pipi")),
				Component.translatable(description("pipi")),
				null,
				AdvancementType.TASK,
				true, true, false
		).addCriterion("pipi", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.PIPI_TOTEM))
		.save(writer, PPTC.locate("pipi_totem"));
		
		//cursed pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locateStr("pipi_totem")))
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
		).save(writer, PPTC.locate("cursed_pipi"));
	}
	
	static String title(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".title";
	}
	static String description(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".description";
	}

}
