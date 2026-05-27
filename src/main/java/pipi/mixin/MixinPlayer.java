package pipi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import pipi.mod.pptc.PPTCHelpers;

@Mixin(Player.class)
public abstract class MixinPlayer implements ILivingEntityExtension {
	
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
	
	//private Player self() {
	//	return (Player)(Object)this;
	//}
}
