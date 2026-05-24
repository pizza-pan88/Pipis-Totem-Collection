package pipi.mod.pptc.menu;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.capability.ITotemStorage;
import pipi.mod.pptc.capability.TotemStorage;
import pipi.mod.pptc.menu.slot.TotemStorageOutputSlot;
import pipi.mod.pptc.util.PPTCMenuTypes;

public class TotemStorageMenu extends WithInventoryMenu {
	public static final ResourceLocation EMPTY_SLOT_TOTEM = PPTC.locate("slot/totem");

	public final TotemStorageContainer storage;
	private final int selected;
	private final InteractionHand holdHand;
	public TotemStorageMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, buf.readEnum(InteractionHand.class), new TotemStorage(), playerInv);
	}
	public TotemStorageMenu(int containerId, InteractionHand hand, ITotemStorage totemStorage, Inventory playerInv) {
		super(PPTCMenuTypes.TOTEM_STORAGE.get(), containerId);
		this.storage = new TotemStorageContainer(playerInv.player, hand, totemStorage);
		this.selected = playerInv.selected;
		this.holdHand = hand;
		
		this.addSlot(new Slot(this.storage, 0, 53, 35) {
			public boolean mayPlace(ItemStack stack) {
				return ((TotemStorageContainer)container).canStoreItem(stack);
			}
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_SLOT_TOTEM);
			}
		});
		this.addSlot(new TotemStorageOutputSlot(this.storage, 1, 107, 35));
		
		super.initHotBar(playerInv);
		super.initInventory(playerInv);
		super.initOffhand(playerInv, false);
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
		//int hotbarId = slotId - SLOT_SIZE - hotbarIndex();
		//データ保存のために、GUIを開いたTotemStorageItemは動かせないように
		if(this.isLockedSlot(slotId, true)
				// 数字キーやfキーでの移動に注意
				|| (clickType == ClickType.SWAP && this.isLockedSlot(dragType, false))) {
			return;
		}
		super.clicked(slotId, dragType, clickType, player);
	}
	
	/** スロットがロックされているかどうか */
	public boolean isLockedSlot(int slot, boolean isId) {
		// ClickTypeがSWAPの場合、dragTypeは移動先のスロットのIndexになっている
		return switch(this.holdHand) {
			case MAIN_HAND -> {
				int hotbar = slot - (isId ? this.hotbarId() : this.hotbarIndex());
				yield hotbar == this.selected;
			}
			case OFF_HAND -> slot == (isId ? this.offhandId() : this.offhandIndex());
			default -> false;
		};
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		if(!player.level().isClientSide) {
			this.clearContainer(player, this.storage);
		}
	}
	
	@Override
	public boolean stillValid(Player p_38874_) {
		return true;
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
			
		if (slot != null && slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			
			if(index <= 1) {
				// 入出力はトーテムなので左手優先
				if(!this.moveItemStackTo(slotStack, 2, 39, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onQuickCraft(slotStack, itemstack);
			} else if(slotStack.is(Items.TOTEM_OF_UNDYING) && this.storage.getItem(0).isEmpty()) {
				// トーテムはストレージの入力スロット
				// 埋まってた場合はパス
				if(!moveItemStackTo(slotStack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if(index >= 11 && index < 39) {
				// インベントリとオフハンドはホットバーへ
				if(!this.moveItemStackTo(slotStack, 2, 11, false)) {
					return ItemStack.EMPTY;
				}
			} else if(!this.moveItemStackTo(slotStack, 11, 39, false)) {
				// ホットバーはインベントリへ
				return ItemStack.EMPTY;
			}
		
			if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			
			if (slotStack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, slotStack);
		}
		
		return itemstack;
	}
}
