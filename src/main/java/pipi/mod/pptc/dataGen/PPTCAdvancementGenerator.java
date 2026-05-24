package pipi.mod.pptc.dataGen;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.util.PPTCItems;
import pipi.mod.pptc.util.PPTCTagKeys;

public class PPTCAdvancementGenerator implements AdvancementGenerator {
	
	@Override
	public void generate(Provider registries, Consumer<Advancement> writer, ExistingFileHelper fileHelper) {
		//root
		Advancement.Builder.advancement()
		.display(PPTCItems.PIPI_TOTEM.get(),
				Component.translatable(title("root")),
				Component.translatable(description("root")),
				ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/bedrock.png"),
				FrameType.TASK,
				false, false, false
		).addCriterion("on_tick", PlayerTrigger.TriggerInstance.tick())
		// 下記2行どちらも同じ意味。ANDの場合は指定不要
		//.requirements(RequirementsStrategy.AND)
		//.requirements(new String[][] {new String[] {"on_tick"}})
		.save(writer, PPTC.locate("root"), fileHelper);
		
		//totem of undying
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("root").toString()))
		.display(Items.TOTEM_OF_UNDYING,
				Component.translatable(title("collect_totem")),
				Component.translatable(description("collect_totem")),
				null,
				FrameType.GOAL,
				true, true, false
		).addCriterion("collect_totem", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TOTEM_OF_UNDYING))
		.save(writer, PPTC.locate("origin"), fileHelper);
		
		//broken totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("origin").toString()))
		.display(PPTCItems.BROKEN_TOTEM.get(),
				Component.translatable(title("break_totem")),
				Component.translatable(description("break_totem")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("broken_totem", UsedTotemTrigger.TriggerInstance.usedTotem(
				ItemPredicate.Builder.item().build()
		)).save(writer, PPTC.locate("broken"), fileHelper);
		
		//totem of return
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_RETURN.get(),
				Component.translatable(title("collect_tor")),
				Component.translatable(description("collect_tor")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_RETURN.get()))
		.save(writer, PPTC.locate("tor"), fileHelper);
		
		//totem 871
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_871.get(),
				Component.translatable(title("collect_ttm_871")),
				Component.translatable(description("collect_ttm_871")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_871.get()))
		.save(writer, PPTC.locate("ttm_871"), fileHelper);
		
		//totem of chocolate
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_CHOCOLATE.get(),
				Component.translatable(title("collect_chocotem")),
				Component.translatable(description("collect_chocotem")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_CHOCOLATE.get()))
		.save(writer, PPTC.locate("chocolate"), fileHelper);
		
		//villager core
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.VILLAGER_CORE.get(),
				Component.translatable(title("villager")),
				Component.translatable(description("villager")),
				null,
				FrameType.GOAL,
				true, true, false
		).addCriterion("take_in_villager", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.VILLAGER_CORE.get()))
		.save(writer, PPTC.locate("villager_core"), fileHelper);
		
		//repair totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("villager_core").toString()))
		.display(PPTCItems.VILLAGER_CORE.get(),
				Component.translatable(title("repair_totem")),
				Component.translatable(description("repair_totem")),
				null,
				FrameType.CHALLENGE,
				true, true, false
		).addCriterion("repair_totem", RecipeCraftedTrigger.TriggerInstance.craftedItem(PPTC.locate("totem_of_undying")))
		.save(writer, PPTC.locate("repair_totem"), fileHelper);
		
		//pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("villager_core").toString()))
		.display(PPTCItems.PIPI_TOTEM.get(),
				Component.translatable(title("pipi")),
				Component.translatable(description("pipi")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("pipi", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.PIPI_TOTEM.get()))
		.save(writer, PPTC.locate("pipi_totem"), fileHelper);
		
		//cursed pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("pipi_totem").toString()))
		.display(
				PipisTotemItem.getCursedTotemStack(0),
				Component.translatable(title("cursed_pipi")),
				Component.translatable(description("cursed_pipi")),
				null,
				FrameType.CHALLENGE,
				true, true, true
		).addCriterion("cursed_pipi", InventoryChangeTrigger.TriggerInstance.hasItems(
				ItemPredicate.Builder.item()
				.of(PPTCItems.PIPI_TOTEM.get())
				.hasNbt(cursedTag())
				.build())
		).save(writer, PPTC.locate("cursed_pipi"), fileHelper);
	}
	
	static String title(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".title";
	}
	static String description(String name) {
		return "advancements."+PPTC.MOD_ID+"."+name+".description";
	}
	
	static CompoundTag cursedTag() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean(PPTCTagKeys.KEY_CURSED, true);
		return tag;
	}

}
