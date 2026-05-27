package pipi.mod.pptc.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.capability.ITotemStorage;

public class TotemStorageContainer implements Container {
	private final NonNullList<ItemStack> io = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	public static final int INDEX_INPUT = 0;
	public static final int INDEX_OUTPUT = 1;
	
	private final Player player;
	private final InteractionHand hand;
	private final ITotemStorage totemStorage;
	public TotemStorageContainer(Player player, InteractionHand hand, ITotemStorage storage) {
		this.player = player;
		this.hand = hand;
		this.totemStorage = storage;
	}
	
	public ItemStack getStorageItem() {
		return this.player.getItemInHand(this.hand);
	}
	
	public ITotemStorage getTotemStorage() {
		return this.totemStorage;
	}
	
	public long getStoredAmount() {
		return this.totemStorage.getStoredAmount(this.getStorageItem());
	}

	@Override
	public int getContainerSize() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : this.io) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return this.io.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(this.io, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ContainerHelper.takeItem(this.io, index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.io.set(index, stack);
	}

	@Override
	public void setChanged() {
		ItemStack input = this.io.get(INDEX_INPUT);
		if(!input.isEmpty() && canStoreItem(input)) {
			// オーバーフロー対策 (スタック上限上がるMOD等)
			//int amount = (int)Math.min(input.getCount(), this.getCapacity());
			
			//input.shrink(amount);
			int stored = (int)this.increaseAmount(input.getCount());
			input.shrink(stored);
		}
	}

	public long increaseAmount(long value) {
		return this.totemStorage.insertTotems(this.getStorageItem(), value);
	}
	public long decreaseAmount(long value) {
		return this.totemStorage.extractTotems(this.getStorageItem(), value);
	}

	/** 在庫があるかどうか */
	public boolean hasTotemsInStock() {
		return this.totemStorage.hasTotems(this.getStorageItem());
	}
	
	/** 残りの容量 */
	public long getCapacity() {
		return this.totemStorage.getCapacity(this.getStorageItem());
	}

	/** 貯蔵できるかどうか */
	public boolean canStoreItem(ItemStack stack) {
		return this.totemStorage.canStoreItem(this.getStorageItem(), stack);
	}

	@Override
	public int getMaxStackSize() {
		return Items.TOTEM_OF_UNDYING.getDefaultInstance().getMaxStackSize();
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return index == INDEX_INPUT && this.canStoreItem(stack);
	}

	@Override
	public void clearContent() {
		this.io.clear();
	}

}
