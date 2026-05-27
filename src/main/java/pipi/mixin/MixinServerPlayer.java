package pipi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import pipi.mod.pptc.PPTCHelpers;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer implements ILivingEntityExtension {
	
	@Inject(
			method = "die",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	private void onDie(DamageSource source, CallbackInfo ci) {
		if(PPTCHelpers.useTotem(self(), source)) {
			ci.cancel();
		}
	}
	
	//private ServerPlayer self() {
	//	return (ServerPlayer)(Object)this;
	//}
}
