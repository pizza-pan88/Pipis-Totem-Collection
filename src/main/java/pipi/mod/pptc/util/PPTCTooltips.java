package pipi.mod.pptc.util;

public class PPTCTooltips {
	static final char PREFIX = '\u00a7';
	static final char[] HEX = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	
	public static String toRainbow(String text, long seed) {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			// 色は"\u00a70"(黒) ~ "\u00a7f"(白)
			int color = (int)((seed + i) & 0xf);
			char[] colorCode = new char[] {PREFIX, HEX[color]};
			str.append(colorCode).append(c);
		}
		return str.toString();
	}

}
