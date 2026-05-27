package pipi.mod.pptc.dataGen;

import java.util.List;

import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class PPTCDataGen {
	
	public static void generate(IEventBus modEventBus) {
		PPTCGLMProvider.GLM_SERIALIZERS.register(modEventBus);
		modEventBus.register(new PPTCDataGen());
	}

	@SubscribeEvent
	public void onGatherData(GatherDataEvent.Client event) {
		event.createProvider(PPTCRecipeProvider.Runner::new);
		//イベントに変更
		//event.createProvider(PPTCGLMProvider::new);
		event.createProvider((output, lookupProvider) -> new AdvancementProvider(
				output, lookupProvider,
				List.of(new PPTCAdvancementGenerator())
		));
	}
	
}
