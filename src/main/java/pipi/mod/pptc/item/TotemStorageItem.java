package pipi.mod.pptc.item;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.capability.ITotemStorage;
import pipi.mod.pptc.menu.TotemStorageMenu;
import pipi.mod.pptc.util.PPTCCapabilities;
import pipi.mod.pptc.util.PPTCDataComponents;
import pipi.mod.pptc.util.PPTCItems;

public class TotemStorageItem extends Item implements ITotemItem {

	public TotemStorageItem(Identifier identifier) {
		super(new Properties()
				.stacksTo(1)
				.setId(ResourceKey.create(Registries.ITEM, identifier))
				);
	}
	
	public static ItemStack filledStorage(long amount) {
		ItemStack stack = new ItemStack(PPTCItems.TOTEM_STORAGE.get());
		stack.set(PPTCDataComponents.TOTEM_AMOUNT, amount);
		return stack;
	}

	@Override
	@Deprecated
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		ITotemStorage storage = stack.getCapability(PPTCCapabilities.TOTEM_STORAGE);
		if(storage != null) {
			long totems = storage.getStoredAmount(stack);
			String value = PPTC.FORMATTER.format(totems);
			tooltipAdder.accept(Component.translatable("tooltip.totem_storage.amount", value).withStyle(ChatFormatting.GRAY));
			tooltipAdder.accept(Component.translatable("tooltip.totem_storage.detale").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		// 右手に持っているときのみGUI表示 (左手に持っているときに開かれると邪魔)
		if(hand == InteractionHand.MAIN_HAND) {
			if(!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				ITotemStorage storage = stack.getCapability(PPTCCapabilities.TOTEM_STORAGE);
				if(storage != null) {
					serverPlayer.openMenu(new SimpleMenuProvider(
							(containerId, playerInv, p) -> new TotemStorageMenu(containerId, hand, storage, playerInv),
							stack.getHoverName() //Component.translatable("menu.title.pptc.totem_storage")
					), buf -> {
						buf.writeEnum(hand);
					});
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean isTotemActivated(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		ITotemStorage storage = totem.getCapability(PPTCCapabilities.TOTEM_STORAGE);
		return storage != null && storage.hasTotems(totem);
	}
	
	@Override
	public float healAmount(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_HEAL;
	}

	@Override
	public List<ConsumeEffect> getEffects(ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		return VANILLA_EFFECTS;
	}

	@Override
	public void onTotemUsed(float healAmount, ItemStack totem, LivingEntity willBeDead, DamageSource damage) {
		if(willBeDead instanceof Player player && player.isCreative()) return;
		
		 var storage = totem.getCapability(PPTCCapabilities.TOTEM_STORAGE);
		 if(storage != null) {
			 long totems = storage.getStoredAmount(totem);
			 totem.set(PPTCDataComponents.TOTEM_AMOUNT, totems - 1);
		 }
	}
	
}
