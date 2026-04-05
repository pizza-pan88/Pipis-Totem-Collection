package pipi.mod.pptc.dataGen;

import java.util.function.Consumer;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.util.PPTCItems;

public class PPTCRecipeProvider extends RecipeProvider {

	public PPTCRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> writer) {
		// totem of undying
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.TOTEM_OF_UNDYING)
		.requires(PPTCItems.BROKEN_TOTEM.get())
		.requires(PPTCItems.VILLAGER_CORE.get())
		.unlockedBy("has_broken_totem", has(PPTCItems.BROKEN_TOTEM.get()))
		.unlockedBy("has_villager_core", has(PPTCItems.VILLAGER_CORE.get()))
		.save(writer, PPTC.locate("totem_of_undying").toString());
		// totem of return
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PPTCItems.TOTEM_OF_RETURN.get())
		.requires(PPTCItems.BROKEN_TOTEM.get())
		.requires(Items.ENDER_PEARL)
		.unlockedBy("has_broken_totem", has(PPTCItems.BROKEN_TOTEM.get()))
		.save(writer);
		// Totem 871
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PPTCItems.TOTEM_871.get())
		.requires(PPTCItems.BROKEN_TOTEM.get())
		.requires(Items.CAKE)
		.unlockedBy("has_broken_totem", has(PPTCItems.BROKEN_TOTEM.get()))
		.save(writer);
		// Totem of Chocolate
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PPTCItems.TOTEM_OF_CHOCOLATE.get())
		.requires(PPTCItems.BROKEN_TOTEM.get())
		.requires(Items.COCOA_BEANS, 8)
		.unlockedBy("has_broken_totem", has(PPTCItems.BROKEN_TOTEM.get()))
		.save(writer);
		// Pipi's Totem
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PPTCItems.PIPI_TOTEM.get())
		.pattern(" V ")
		.pattern("SBC")
		.pattern(" R ")
		.define('V', PPTCItems.VILLAGER_CORE.get())
		.define('S', PPTCItems.TOTEM_871.get())
		.define('B', PPTCItems.BROKEN_TOTEM.get())
		.define('C', PPTCItems.TOTEM_OF_CHOCOLATE.get())
		.define('R', PPTCItems.TOTEM_OF_RETURN.get())
		.unlockedBy("has_broken_totem", has(PPTCItems.BROKEN_TOTEM.get()))
		.save(writer);
	}

}
