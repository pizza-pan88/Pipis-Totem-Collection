package pipi.mod.pptc.util;

import net.minecraft.ChatFormatting;

public class PPTCTooltips {
	static final char PREFIX = ChatFormatting.PREFIX_CODE;
	static final char[] HEX = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	
	public static String toRainbow(String text, long seed) {
		int len = text.length() * 3;
		StringBuilder str = new StringBuilder(len);
		for(int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			// 色は"\u00a70"(黒) ~ "\u00a7f"(白)
			int colorCode = (int)((seed + i) & 0xf);
			char hexCode = HEX[colorCode];
			// "§" + "カラーコード" + "charAt"
			str.append(PREFIX).append(hexCode).append(c);
		}
		return str.toString();
	}

}
