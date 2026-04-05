package pipi.mod.pptc.dataGen;

import java.util.function.Consumer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
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
		.display(Items.TOTEM_OF_UNDYING,
				Component.translatable(title("origin")),
				Component.translatable(description("origin")),
				ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/bedrock.png"),
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(Items.TOTEM_OF_UNDYING))
		.requirements(new String[][] {new String[] {"get_totem"}})
		.save(writer, PPTC.locate("origin"), fileHelper);
		//broke totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("origin").toString()))
		.display(PPTCItems.BROKEN_TOTEM.get(),
				Component.translatable(title("broken")),
				Component.translatable(description("broken")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("broken_totem", UsedTotemTrigger.TriggerInstance.usedTotem(
				ItemPredicate.Builder.item().build()
		)).requirements(new String[][] {new String[] {"broken_totem"}})
		.save(writer, PPTC.locate("broken"), fileHelper);
		//totem of return
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_RETURN.get(),
				Component.translatable(title("tor")),
				Component.translatable(description("tor")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_RETURN.get()))
		.requirements(new String[][] {new String[] {"get_totem"}})
		.save(writer, PPTC.locate("tor"), fileHelper);
		//totem 871
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_871.get(),
				Component.translatable(title("ttm_871")),
				Component.translatable(description("ttm_871")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_871.get()))
		.requirements(new String[][] {new String[] {"get_totem"}})
		.save(writer, PPTC.locate("ttm_871"), fileHelper);
		//totem of chocolate
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("broken").toString()))
		.display(PPTCItems.TOTEM_OF_CHOCOLATE.get(),
				Component.translatable(title("chocolate")),
				Component.translatable(description("chocolate")),
				null,
				FrameType.TASK,
				true, true, false
		).addCriterion("get_totem", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.TOTEM_OF_CHOCOLATE.get()))
		.requirements(new String[][] {new String[] {"get_totem"}})
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
		.requirements(new String[][] {new String[] {"take_in_villager"}})
		.save(writer, PPTC.locate("villager_core"), fileHelper);
		//pipi's totem
		Advancement.Builder.advancement()
		.parent(AdvancementSubProvider.createPlaceholder(PPTC.locate("villager_core").toString()))
		.display(PPTCItems.PIPI_TOTEM.get(),
				Component.translatable(title("pipi")),
				Component.translatable(description("pipi")),
				null,
				FrameType.GOAL,
				true, true, false
		).addCriterion("pipi", InventoryChangeTrigger.TriggerInstance.hasItems(PPTCItems.PIPI_TOTEM.get()))
		.requirements(new String[][] {new String[] {"pipi"}})
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
		).requirements(new String[][] {new String[] {"cursed_pipi"}})
		.save(writer, PPTC.locate("cursed_pipi"), fileHelper);
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
