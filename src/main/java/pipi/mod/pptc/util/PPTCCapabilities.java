package pipi.mod.pptc.util;

import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.capability.ITotemStorage;
import pipi.mod.pptc.capability.TotemStorageProvider;

public final class PPTCCapabilities {

	public static final ItemCapability<ITotemStorage, Void> TOTEM_STORAGE;

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(TOTEM_STORAGE, TotemStorageProvider.simple(), PPTCItems.TOTEM_STORAGE);
	}
	
	static {
		TOTEM_STORAGE = ItemCapability.createVoid(PPTC.locate("totem_storage"), ITotemStorage.class);
	}

}
