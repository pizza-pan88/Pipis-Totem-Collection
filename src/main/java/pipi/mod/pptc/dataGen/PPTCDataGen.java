package pipi.mod.pptc.dataGen;

import java.util.List;

import net.minecraft.data.DataProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// "gradlew runData"か"runDataSlim"で実行
public final class PPTCDataGen {
	
	public static void generate(IEventBus modEventBus) {
		PPTCGLMProvider.GLM_SERIALIZERS.register(modEventBus);
		modEventBus.register(new PPTCDataGen());
	}

	@SubscribeEvent
	public void onGatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(
				event.includeServer(),
				factory(PPTCRecipeProvider::new)
		);
		/* 難しいのでイベントに変更
		event.getGenerator().addProvider(
				event.includeServer(),
				factory(PPTCGLMProvider::new)
		);*/
		event.getGenerator().addProvider(
				event.includeServer(),
				factory(output -> new ForgeAdvancementProvider(
								output,
								event.getLookupProvider(),
								event.getExistingFileHelper(),
								List.of(new PPTCAdvancementGenerator())
						)
				)
		);
	}
	
	// コンパイルエラーを防ぐため
	<T extends DataProvider> DataProvider.Factory<T>
	factory(DataProvider.Factory<T> factory) {
		return factory;
	}
	
}
