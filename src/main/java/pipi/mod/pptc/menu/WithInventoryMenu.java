package pipi.mod.pptc.menu;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import pipi.mod.pptc.PPTC;

public abstract class WithInventoryMenu extends AbstractContainerMenu {
	public static ResourceLocation BACKGROUND_TEXTURE = PPTC.locate("textures/gui/container/container.png");
	public static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
			InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
			InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
			InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
			InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
	};
	private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[] {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	private Optional<Integer> hotbarId = Optional.empty();
	private Optional<Integer> inventoryId = Optional.empty();
	private Optional<Integer> armorId = Optional.empty();
	private Optional<Integer> offhandId = Optional.empty();
	protected WithInventoryMenu(MenuType<?> menuType, int containerId) {
		super(menuType, containerId);
	}
	
	/** インベントリ(9*3のスロット)を追加 */
	protected void initInventory(Inventory playerInventory) {
		this.inventoryId = Optional.of(this.slots.size());
		int startIdx = inventoryIndex();
		for(int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int index = startIdx + j + i * 9;
				int x = 8 + j * 18;
				int y = 84 + i * 18;
				this.addSlot(new Slot(playerInventory, index, x, y));
			}
		}
	}
	
	/** ホットバーを追加 */
	protected void initHotBar(Inventory playerInventory) {
		this.hotbarId = Optional.of(this.slots.size());
		int startIdx = hotbarIndex();
		for(int l = 0; l < 9; ++l) {
			int index = startIdx + l;
			int x = 8 + l * 18;
			this.addSlot(new Slot(playerInventory, index, x, 142));
		}
	}
	/** 固定スロット付きのホットバーを追加 */
	protected void initHotBarWithLocked(Inventory playerInventory, int lockedSlot) {
		this.hotbarId = Optional.of(this.slots.size());
		int startIdx = hotbarIndex();
		for(int l = 0; l < 9; ++l) {
			int index = startIdx + l;
			int x = 8 + l * 18;
			this.addSlot(new Slot(playerInventory, index, x, 142) {
				@Override
				public boolean mayPickup(Player player) {
					return this.getSlotIndex() != lockedSlot;
				}
				@Override
				public boolean mayPlace(ItemStack p_40231_) {
					return this.getSlotIndex() != lockedSlot;
				}
			});
		}
	}

	/** 防具スロットを追加 */
	protected void initArmors(Inventory playerInventory) {
		this.armorId = Optional.of(this.slots.size());
		int startIdx = armorIndex();
		for(int k = 0; k < 4; ++k) {
			int index = startIdx + (3 - k);
			int y = 6 + k * 18;
			final EquipmentSlot equipmentslot = SLOT_IDS[k];
			this.addSlot(new Slot(playerInventory, index, 8, y) {
				public void setByPlayer(ItemStack stack) {
					onEquipItem(playerInventory.player, equipmentslot, stack, this.getItem());
					super.setByPlayer(stack);
				}
	            public int getMaxStackSize() {
					return 1;
				}
				public boolean mayPlace(ItemStack stack) {
					return stack.canEquip(equipmentslot, playerInventory.player);
				}
				public boolean mayPickup(Player playerIn) {
					ItemStack itemstack = this.getItem();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(playerIn);
				}
				public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
					return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
				}
			});
		}
	}
	
	/** オフハンドを追加 */
	protected void initOffhand(Inventory playerInventory, boolean locked) {
		this.offhandId = Optional.of(this.slots.size());
		int index = offhandIndex();
		this.addSlot(new Slot(playerInventory, index, 152, 62) {
			public void setByPlayer(ItemStack stack) {
				onEquipItem(playerInventory.player, EquipmentSlot.OFFHAND, stack, this.getItem());
				super.setByPlayer(stack);
			}
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
			}
			public boolean mayPickup(Player player) {
				return !locked;
			}
			public boolean mayPlace(ItemStack p_40231_) {
				return !locked;
			}
		});
	}
	
	/** @see {@link InventoryMenu#onEquipItem} */
	static void onEquipItem(Player player, EquipmentSlot equipmentslot, ItemStack stack, ItemStack stackInSlot) {
		Equipable equipable = Equipable.get(stack);
		if (equipable != null) {
			player.onEquipItem(equipmentslot, stackInSlot, stack);
		}
	}

	/** ホットバーの最も左のインデックス */
	protected int hotbarIndex() {
		return 0;
	}
	/** ホットバーの最も左のスロットID */
	protected int hotbarId() {
		return this.hotbarId.orElseThrow(IllegalCallerException::new);
	}
	
	/** インベントリ(9*3のスロット)の左上のインデックス */
	protected int inventoryIndex() {
		return 9;
	}
	/** インベントリ(9*3のスロット)の左上のスロットID */
	protected int inventoryId() {
		return this.inventoryId.orElseThrow(IllegalCallerException::new);
	}
	
	/** 防具スロットのヘルメットのインデックス */
	protected int armorIndex() {
		return 36;
	}
	/** 防具スロットのヘルメットのスロットID */
	protected int armorId() {
		return this.armorId.orElseThrow(IllegalCallerException::new);
	}
	
	/** オフハンドのインデックス */
	protected int offhandIndex() {
		return 40;
	}
	/** オフハンドのスロットID */
	protected int offhandId() {
		return this.offhandId.orElseThrow(IllegalCallerException::new);
	}
	
}
