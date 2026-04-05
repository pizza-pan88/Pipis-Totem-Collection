package pipi.mod.pptc.util;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import pipi.mod.pptc.PPTC;

public class PPTCDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS
	= DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PPTC.MOD_ID);
	
	public static final Supplier<DataComponentType<Float>> HEALTH =
			DATA_COMPONENTS.registerComponentType("totem_health", builder -> builder
					.persistent(ExtraCodecs.POSITIVE_FLOAT)
					.networkSynchronized(ByteBufCodecs.FLOAT)
			);
	
	public static final Supplier<DataComponentType<Boolean>> CURSED =
			DATA_COMPONENTS.registerComponentType("totem_cursed", builder -> builder
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
			);
}
