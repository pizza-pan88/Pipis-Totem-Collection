package pipi.mod.pptc.capability;

import org.jetbrains.annotations.Range;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ITotemStorage {
	
	/** 格納されてるトーテムの個数
	 * @param storage : ストレージアイテム
	 * @return 格納されてる個数
	 */
	@Range(from = 0, to = Long.MAX_VALUE)
	long getStoredAmount(ItemStack storage);
	
	/** トーテムを格納する
	 * @param storage : ストレージアイテム
	 * @param amount : 格納する個数
	 * @return 格納できた個数
	 */
	long insertTotems(ItemStack storage, long amount);
	
	/** トーテムを取り出す
	 * @param storage : ストレージアイテム
	 * @param amount : 取り出す個数
	 * @return 取り出せた個数
	 */
	long extractTotems(ItemStack storage, long amount);
	
	/** トーテムが格納されているかどうか
	 * @param storage : ストレージアイテム
	 * @return 格納されていればtrue
	 */
	default boolean hasTotems(ItemStack storage) {
		return getStoredAmount(storage) > 0;
	}

	/** トーテムを格納できる最大容量
	 * @param storage : ストレージアイテム
	 * @return 最大容量
	 */
	@Range(from = 0, to = Long.MAX_VALUE)
	long getMaxCapacity(ItemStack storage);
	
	/** トーテムを格納できる残りの容量
	 * @param storage : ストレージアイテム
	 * @return 残りの容量
	 */
	@Range(from = 0, to = Long.MAX_VALUE)
	default long getCapacity(ItemStack storage) {
		long capability = this.getMaxCapacity(storage) - this.getStoredAmount(storage);
		return Math.max(0, capability);
	}
	
	/** アイテムを格納できるかどうか
	 * @param storage : ストレージアイテム
	 * @param stack : 格納しようとしているアイテム
	 * @return stackが格納できるアイテムならtrue
	 */
	default boolean canStoreItem(ItemStack storage, ItemStack stack) {
		return stack.getItem() == Items.TOTEM_OF_UNDYING
				&& stack.getCount() > 0
				&& this.getCapacity(storage) > 0;
	}
}
