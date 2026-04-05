package pipi.mod.pptc.dataGen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.util.PPTCItems;

public class PPTCRecipeProvider extends RecipeProvider {

	protected PPTCRecipeProvider(Provider registries, RecipeOutput output) {
		super(registries, output);
	}

	@Override
	protected void buildRecipes() {
		// totem of undying
		ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, Items.TOTEM_OF_UNDYING)
		.requires(PPTCItems.BROKEN_TOTEM)
		.requires(PPTCItems.VILLAGER_CORE)
		.unlockedBy("has_broken_totem", this.has(PPTCItems.BROKEN_TOTEM))
		.unlockedBy("has_villager_core", this.has(PPTCItems.VILLAGER_CORE))
		.save(this.output, PPTC.locate("totem_of_undying").toString());
		// totem of return
		ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, PPTCItems.TOTEM_OF_RETURN)
		.requires(PPTCItems.BROKEN_TOTEM)
		.requires(Items.ENDER_PEARL)
		.unlockedBy("has_broken_totem", this.has(PPTCItems.BROKEN_TOTEM))
		.save(this.output);
		// Totem 871
		ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, PPTCItems.TOTEM_871)
		.requires(PPTCItems.BROKEN_TOTEM)
		.requires(Items.CAKE)
		.unlockedBy("has_broken_totem", this.has(PPTCItems.BROKEN_TOTEM))
		.save(this.output);
		// Totem of Chocolate
		ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, PPTCItems.TOTEM_OF_CHOCOLATE)
		.requires(PPTCItems.BROKEN_TOTEM)
		.requires(Items.COCOA_BEANS, 8)
		.unlockedBy("has_broken_totem", this.has(PPTCItems.BROKEN_TOTEM))
		.save(this.output);
		// Pipi's Totem
		ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, PipisTotemItem.getTotemStack(20))
		.pattern(" V ")
		.pattern("SBC")
		.pattern(" R ")
		.define('V', PPTCItems.VILLAGER_CORE)
		.define('S', PPTCItems.TOTEM_871)
		.define('B', PPTCItems.BROKEN_TOTEM)
		.define('C', PPTCItems.TOTEM_OF_CHOCOLATE)
		.define('R', PPTCItems.TOTEM_OF_RETURN)
		.unlockedBy("has_broken_totem", this.has(PPTCItems.BROKEN_TOTEM))
		.save(this.output);
	}

	public static class Runner extends RecipeProvider.Runner {

		public Runner(PackOutput packOutput, CompletableFuture<Provider> registries) {
			super(packOutput, registries);
		}

		@Override
		public String getName() {
			return PPTC.MOD_ID;
		}

		@Override
		public RecipeProvider createRecipeProvider(Provider registries, RecipeOutput output) {
			return new PPTCRecipeProvider(registries, output);
		}
		
	}
}
