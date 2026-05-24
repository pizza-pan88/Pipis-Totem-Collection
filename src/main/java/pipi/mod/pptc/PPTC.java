package pipi.mod.pptc;

import java.text.DecimalFormat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import pipi.mod.pptc.dataGen.PPTCDataGen;
import pipi.mod.pptc.gui.TotemStorageScreen;
import pipi.mod.pptc.item.PipisTotemItem;
import pipi.mod.pptc.item.TotemStorageItem;
import pipi.mod.pptc.util.PPTCItems;
import pipi.mod.pptc.util.PPTCMenuTypes;

@Mod(PPTC.MOD_ID)
public class PPTC {
	public static final String MOD_ID = "pptc";
	public static final String MOD_NAME = "Pipi's Totem Collection";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public static final DecimalFormat FORMATTER = new DecimalFormat("#,##0.##");
	
	static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
	
	public static final RegistryObject<CreativeModeTab> PPTC_TAB = CREATIVE_MODE_TABS.register(
			"pptc_tab",
			() -> CreativeModeTab.builder()
			.title(Component.literal(MOD_NAME))
			.withTabsBefore(CreativeModeTabs.COMBAT)
			.icon(() -> PipisTotemItem.getTotemStack(Float.MAX_VALUE))
			.displayItems((parameters, output) -> {
				output.accept(PipisTotemItem.getTotemStack(Float.MAX_VALUE));
				output.accept(PipisTotemItem.getCursedTotemStack(Float.MAX_VALUE));
				output.accept(PPTCItems.BROKEN_TOTEM.get());
				output.accept(PPTCItems.VILLAGER_CORE.get());
				output.accept(PPTCItems.TOTEM_OF_RETURN.get());
				output.accept(PPTCItems.TOTEM_871.get());
				output.accept(PPTCItems.TOTEM_OF_CHOCOLATE.get());
				output.accept(PPTCItems.TOTEM_STORAGE.get());
				output.accept(TotemStorageItem.filledStorage(Long.MAX_VALUE));
				output.accept(PPTCItems.TOTEM_OF_BEDROCK.get());
				output.accept(PPTCItems.DIRTEM.get());
			}).build());
		
    
	public PPTC(FMLJavaModLoadingContext context) {
		IEventBus modEventBus = context.getModEventBus();
		
		PPTCItems.ITEMS.register(modEventBus);
		PPTCMenuTypes.MENU_TYPES.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		PPTCDataGen.generate(modEventBus);
		
		modEventBus.addListener(this::clientSetup);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}
	
	public void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(PPTCMenuTypes.TOTEM_STORAGE.get(), TotemStorageScreen::new);
		});
	}
	
	public static ResourceLocation locate(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

}
