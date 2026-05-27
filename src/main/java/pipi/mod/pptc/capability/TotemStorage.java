package pipi.mod.pptc.capability;

import org.jetbrains.annotations.Range;

import net.minecraft.world.item.ItemStack;
import pipi.mod.pptc.util.PPTCDataComponents;

public class TotemStorage implements ITotemStorage {
	
	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getStoredAmount(ItemStack storage) {
		return storage.getOrDefault(PPTCDataComponents.TOTEM_AMOUNT, 0L);
	}
	
	/** @return 差分の絶対値 */
	private long setAmount(ItemStack stack, long value) {
		long stored = this.getStoredAmount(stack);
		// Positive long
		long amount = Math.max(0, Math.min(value, this.getMaxCapacity(stack)));
		stack.set(PPTCDataComponents.TOTEM_AMOUNT, amount);
		return Math.abs(amount - stored);
	}

	@Override
	public long insertTotems(ItemStack storage, long amount) {
		if(amount < 0) {
			this.extractTotems(storage, -amount);
		}
		long value = this.getStoredAmount(storage) + amount;
		return this.setAmount(storage, value);
	}

	@Override
	public long extractTotems(ItemStack storage, long amount) {
		if(amount < 0) {
			this.insertTotems(storage, -amount);
		}
		long value = this.getStoredAmount(storage) - amount;
		return this.setAmount(storage, value);
	}

	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getMaxCapacity(ItemStack storage) {
		return Long.MAX_VALUE;
	}
	
}
