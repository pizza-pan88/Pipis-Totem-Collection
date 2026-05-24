package pipi.mod.pptc.capability;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import pipi.mod.pptc.util.PPTCCapabilities;

public class TotemStorageProvider implements ICapabilityProvider {

	public static ICapabilityProvider simple() {
		return new TotemStorageProvider(new TotemStorage());
	}
	
	private final LazyOptional<ITotemStorage> storage;
	public TotemStorageProvider(ITotemStorage storage) {
		this.storage = LazyOptional.of(() -> storage);
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return PPTCCapabilities.TOTEM_STORAGE.orEmpty(cap, this.storage);
	}

}
