package pipi.mod.pptc;

import java.text.DecimalFormat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pipi.mod.pptc.dataGen.PPTCDataGen;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.util.PPTCDataComponents;
import pipi.mod.pptc.util.PPTCItems;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PPTC.MOD_ID)
public class PPTC {
	public static final String MOD_ID = "pptc";
	public static final String MOD_NAME = "Pipi's Totem Collection";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public static final DecimalFormat FORMATTER = new DecimalFormat("#,##0.##");
	
	static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
	
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PPTC_TAB = CREATIVE_MODE_TABS.register(
			"pptc_tab",
			() -> CreativeModeTab.builder()
			.title(Component.literal(MOD_NAME))
			.withTabsBefore(CreativeModeTabs.COMBAT)
			.icon(() -> PipisTotemItem.getTotemStack(Float.MAX_VALUE))
			.displayItems((parameters, output) -> {
				output.accept(PipisTotemItem.getTotemStack(Float.MAX_VALUE));
				output.accept(PipisTotemItem.getCursedTotemStack(Float.MAX_VALUE));
				output.accept(PPTCItems.BROKEN_TOTEM);
				output.accept(PPTCItems.VILLAGER_CORE);
				output.accept(PPTCItems.TOTEM_OF_RETURN);
				output.accept(PPTCItems.TOTEM_871);
				output.accept(PPTCItems.TOTEM_OF_CHOCOLATE);
			}).build());
		
    
	public PPTC(IEventBus modEventBus, ModContainer modContainer) {
		PPTCItems.ITEMS.register(modEventBus);
		PPTCDataComponents.DATA_COMPONENTS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		PPTCDataGen.generate(modEventBus);

		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}
	
	public static Identifier locate(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

}
