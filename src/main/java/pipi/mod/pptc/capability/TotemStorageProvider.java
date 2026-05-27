package pipi.mod.pptc.capability;

import org.jspecify.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;

public class TotemStorageProvider implements ICapabilityProvider<ItemStack, Void, ITotemStorage> {

	public static ICapabilityProvider<ItemStack, Void, ITotemStorage> simple() {
		return new TotemStorageProvider(new TotemStorage());
	}

	private final ITotemStorage storage;
	public TotemStorageProvider(ITotemStorage storage) {
		this.storage = storage;
	}
	
	@Override
	@Nullable
	public ITotemStorage getCapability(ItemStack object, Void context) {
		return this.storage;
	}


}
