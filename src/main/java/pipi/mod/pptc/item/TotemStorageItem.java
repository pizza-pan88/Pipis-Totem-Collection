package pipi.mod.pptc.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.capability.ITotemStorage;
import pipi.mod.pptc.capability.TotemStorageProvider;
import pipi.mod.pptc.menu.TotemStorageMenu;
import pipi.mod.pptc.util.PPTCCapabilities;
import pipi.mod.pptc.util.PPTCItems;
import pipi.mod.pptc.util.PPTCTagKeys;

public class TotemStorageItem extends Item implements ITotemItem {

	public TotemStorageItem() {
		super(new Properties()
				.stacksTo(1));
	}
	
	public static ItemStack filledStorage(long amount) {
		ItemStack stack = new ItemStack(PPTCItems.TOTEM_STORAGE.get());
		stack.getOrCreateTag().putLong(PPTCTagKeys.KEY_TOTEM_AMOUNT, amount);
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltips, TooltipFlag flag) {
		stack.getCapability(PPTCCapabilities.TOTEM_STORAGE)
		.ifPresent((storage) -> {
				long totems = storage.getStoredAmount(stack);
				String value = PPTC.FORMATTER.format(totems);
				tooltips.add(Component.translatable("tooltip.totem_storage.amount", value).withStyle(ChatFormatting.GRAY));
				tooltips.add(Component.translatable("tooltip.totem_storage.detale").withStyle(ChatFormatting.GRAY));
		 });
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		// 右手に持っているときのみGUI表示 (左手に持っているときに開かれると邪魔)
		if(hand == InteractionHand.MAIN_HAND) {
			if(!level.isClientSide) {
				stack.getCapability(PPTCCapabilities.TOTEM_STORAGE)
				.ifPresent((storage) -> {
					NetworkHooks.openScreen((ServerPlayer)player, new SimpleMenuProvider(
							(containerId, playerInv, p) -> new TotemStorageMenu(containerId, hand, storage, playerInv),
							stack.getHoverName() //Component.translatable("menu.title.pptc.totem_storage")
					), buf -> {
						buf.writeEnum(hand);
					});
				});
			}
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		ITotemStorage storage = totem.getCapability(PPTCCapabilities.TOTEM_STORAGE).orElse(null);
		return storage != null && storage.hasTotems(totem);
	}
	
	@Override
	public float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_HEAL;
	}

	@Override
	public List<MobEffectInstance> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_EFFECTS;
	}

	@Override
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof Player player && player.isCreative()) return;
		
		 totem.getCapability(PPTCCapabilities.TOTEM_STORAGE)
		 .ifPresent((storage) -> {
			 long totems = storage.getStoredAmount(totem);
			 totem.getOrCreateTag().putLong(PPTCTagKeys.KEY_TOTEM_AMOUNT, totems - 1);
		 });
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return TotemStorageProvider.simple();
	}
	
}
