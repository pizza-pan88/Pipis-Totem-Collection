package pipi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import pipi.mod.pptc.PPTCEventHandler;
import pipi.mod.pptc.PPTCHelpers;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

	@Inject(
			method = "checkTotemDeathProtection",
			at = @At(value = "RETURN"),
			cancellable = true
	)
	private void onCheckTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(cir.getReturnValue()) {
			// トーテム発動成功 -> 壊れたトーテム付与
			PPTCEventHandler.addBrokenTotem(thisEntity());
			
			// トーテム発動失敗 -> ITotemItemの発動確認
		} else if(PPTCHelpers.useTotem(thisEntity(), source)) {
			cir.setReturnValue(true);
		}
	}

	/** @see IForgeLivingEntity#self() */ 
	private LivingEntity thisEntity() {
		return (LivingEntity)(Object)this;
	}
}
