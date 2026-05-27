package pipi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import pipi.mod.pptc.PPTCEventHandler;
import pipi.mod.pptc.PPTCHelpers;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ILivingEntityExtension {

	@Inject(
			method = "checkTotemDeathProtection",
			at = @At(value = "RETURN"),
			cancellable = true
	)
	private void onCheckTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(cir.getReturnValue()) {
			// トーテム発動成功 -> 壊れたトーテム付与
			PPTCEventHandler.addBrokenTotem(self());
			
			// トーテム発動失敗 -> ITotemItemの発動確認
		} else if(PPTCHelpers.useTotem(self(), source)) {
			cir.setReturnValue(true);
		}
	}
	
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
	
	//private LivingEntity self() {
	//	return (LivingEntity)(Object)this;
	//}
}
