package pipi.mod.pptc.util;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import pipi.mod.pptc.capability.ITotemStorage;

public class PPTCCapabilities {

	public static final Capability<ITotemStorage> TOTEM_STORAGE;
	
	static {
		TOTEM_STORAGE = CapabilityManager.get(new CapabilityToken<>() {});
	}
	
}
