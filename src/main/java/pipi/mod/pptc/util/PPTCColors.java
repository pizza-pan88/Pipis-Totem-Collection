package pipi.mod.pptc.util;

import net.minecraft.ChatFormatting;

public class PPTCColors {
	static final char PREFIX = ChatFormatting.PREFIX_CODE;
	static final char[] COLOR_CODES = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	// 赤 ➔ 橙 ➔ 黄 ➔ 緑 ➔ 青 ➔ 藍 ➔ 紫 ➔ 赤紫 ➔ 赤 ➔ ...
	static final char[] RAINBOW_CODES = {
			'c', '6', 'e', 'a', 'b', '9', '5', 'd'
	};
	
	public static String toColorful(CharSequence text, long seed) {
		return color(COLOR_CODES, text, (int)seed);
	}
	
	public static String toRainbow(CharSequence text, long seed) {
		return color(RAINBOW_CODES, text, (int)seed);
	}
	
	/** 一文字ずつ着色する
	 * @param colorCodes : カラーコードの配列
	 * @param text : 着色する対象
	 * @param seed : 配色の組み合わせ
	 * @return 着色された{@code text}
	 */
	public static String colorText(char[] colorCodes, String text, long seed) {
		int colors = colorCodes.length;
		// 着色しない
		if(colors == 0) {
			return text;
		}
		
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			// 一色ずつ変化させる
			int color = Math.floorMod(seed + i, colors);
			char colorCode = colorCodes[color];
			str.append(PREFIX).append(colorCode).append(c);
		}
		return str.toString();
	}
	
	static String color(char[] colorCodes, CharSequence text, int seed) {
		int size = colorCodes.length - 1;
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			int cl = (seed + i) & size;
			char cc = colorCodes[cl];
			str.append(PREFIX).append(cc).append(c);
		}
		return str.toString();
	}

}
