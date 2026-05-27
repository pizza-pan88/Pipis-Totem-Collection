package pipi.mod.pptc.gui;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import pipi.mod.pptc.PPTC;
import pipi.mod.pptc.menu.TotemStorageMenu;

public class TotemStorageScreen extends AbstractContainerScreen<TotemStorageMenu> {
	private static final Identifier GUI_TEXTURE = PPTC.locate("textures/gui/container/totem_storage.png");

	public TotemStorageScreen(TotemStorageMenu storageMenu, Inventory playerInv, Component title) {
		super(storageMenu, playerInv, title);
	}

	public void init() {
		super.init();
		this.titleLabelY = 10;
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}
	
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
	
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		// ストレージ内のトーテムの個数
		Component amount = Component.translatable("tooltip.totem_storage.amount", formattedStoredAmount());
		int amountX = (this.imageWidth - this.font.width(amount)) / 2;
		int amountY = 62;
		guiGraphics.drawString(this.font, amount, amountX, amountY, -12566464, false);
	}
	
	protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if(this.menu.getCarried().isEmpty() && hoveredOutputSlot()) {
			List<FormattedCharSequence> tips = Lists.newArrayList();
			
			ItemStack totem = Items.TOTEM_OF_UNDYING.getDefaultInstance();
			tips.add(Component.empty()
					.append(totem.getHoverName())
					.withStyle(totem.getRarity().getStyleModifier())
					.getVisualOrderText()
			);
			String amount = PPTC.FORMATTER.format(this.menu.storage.getStoredAmount());
			tips.add(
					Component.translatable("tooltip.totem_storage.amount", amount)
					.getVisualOrderText()
			);

			guiGraphics.setTooltipForNextFrame(tips, mouseX, mouseY);
		} else {
			super.renderTooltip(guiGraphics, mouseX, mouseY);
		}
	}
	
	private boolean hoveredOutputSlot() {
		return this.hoveredSlot != null && this.hoveredSlot.index == 1;
	}
	   
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		int x = this.leftPos;
		int y = this.topPos;
		guiGraphics.blit(
				RenderPipelines.GUI_TEXTURED,
				GUI_TEXTURE,
				x, y, 0.0f, 0.0f,
				this.imageWidth, this.imageHeight, 256, 256);
	}

	String[] suffixes = new String[] {"K","M","G","T","P","E","Z","Y"};
	public String formattedStoredAmount() {
		DecimalFormat formatter = PPTC.FORMATTER;
		long amount = this.menu.storage.getStoredAmount();
		if(amount < 1000L) {
			return formatter.format(amount);
		}
		
		double value = Long.valueOf(amount).doubleValue();
		// Yottaまであれば足りるはず
		for(String suffix : suffixes) {
			value /= 1000.0d;
			if(value < 1000) {
				return formatter.format(value) + suffix;
			}
		}
		return formatter.format(amount);
	}
}
