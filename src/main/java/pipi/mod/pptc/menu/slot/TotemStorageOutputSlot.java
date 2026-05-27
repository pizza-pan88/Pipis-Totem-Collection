package pipi.mod.pptc.menu.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.menu.TotemStorageContainer;

public class TotemStorageOutputSlot extends Slot {
	
	private final TotemStorageContainer storage;
	public TotemStorageOutputSlot(TotemStorageContainer storage, int index, int x, int y) {
		super(storage, index, x, y);
		this.storage = storage;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.TOTEM_OF_UNDYING);
	}

	@Override
	public boolean hasItem() {
		return this.storage.hasTotemsInStock();
	}

	@Override
	public void set(ItemStack stack) {
		// 取り出し専用
	}

	@Override
	public ItemStack remove(int count) {
		// マウスクリックや捨てた場合
		this.storage.decreaseAmount(count);
		return new ItemStack(Items.TOTEM_OF_UNDYING);
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return this.storage.hasTotemsInStock();
	}

	@Override
	protected void onQuickCraft(ItemStack stack, int count) {
		// 一括移動(シフト左クリック)の場合
		this.storage.decreaseAmount(count);
	}

	@Override
	protected void onSwapCraft(int count) {
		// 交換(fや数字キー)の場合
		this.storage.decreaseAmount(count);
	}

}
