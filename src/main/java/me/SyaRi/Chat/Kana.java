package me.SyaRi.Chat;

import java.util.HashMap;

public class Kana {

	private static final HashMap<String, String[]> TABLE;
	static {
		TABLE = new HashMap<String, String[]>();
		TABLE.put(  "", new String[]{"あ","い","う","え","お"});
		TABLE.put( "k", new String[]{"か","き","く","け","こ"});
		TABLE.put( "s", new String[]{"さ","し","す","せ","そ"});
		TABLE.put( "t", new String[]{"た","ち","つ","て","と"});
		TABLE.put( "n", new String[]{"な","に","ぬ","ね","の"});
		TABLE.put( "h", new String[]{"は","ひ","ふ","へ","ほ"});
		TABLE.put( "m", new String[]{"ま","み","む","め","も"});
		TABLE.put( "y", new String[]{"や","い","ゆ","いぇ","よ"});
		TABLE.put( "r", new String[]{"ら","り","る","れ","ろ"});
		TABLE.put( "w", new String[]{"わ","うぃ","う","うぇ","を"});
		TABLE.put( "g", new String[]{"が","ぎ","ぐ","げ","ご"});
		TABLE.put( "z", new String[]{"ざ","じ","ず","ぜ","ぞ"});
		TABLE.put( "j", new String[]{"じゃ","じ","じゅ","じぇ","じょ"});
		TABLE.put( "d", new String[]{"だ","ぢ","づ","で","ど"});
		TABLE.put( "b", new String[]{"ば","び","ぶ","べ","ぼ"});
		TABLE.put( "p", new String[]{"ぱ","ぴ","ぷ","ぺ","ぽ"});
		TABLE.put("gy", new String[]{"ぎゃ","ぎぃ","ぎゅ","ぎぇ","ぎょ"});
		TABLE.put("gw", new String[]{"ぐぁ","ぐぃ","ぐぅ","ぐぇ","ぐぉ"});
		TABLE.put("zy", new String[]{"じゃ","じぃ","じゅ","じぇ","じょ"});
		TABLE.put("jy", new String[]{"じゃ","じぃ","じゅ","じぇ","じょ"});
		TABLE.put("dy", new String[]{"ぢゃ","ぢぃ","ぢゅ","ぢぇ","ぢょ"});
		TABLE.put("dh", new String[]{"でゃ","でぃ","でゅ","でぇ","でょ"});
		TABLE.put("dw", new String[]{"どぁ","どぃ","どぅ","どぇ","どぉ"});
		TABLE.put("by", new String[]{"びゃ","びぃ","びゅ","びぇ","びょ"});
		TABLE.put("py", new String[]{"ぴゃ","ぴぃ","ぴゅ","ぴぇ","ぴょ"});
		TABLE.put( "v", new String[]{"ヴぁ","ヴぃ","ヴ","ヴぇ","ヴぉ"});
		TABLE.put("vy", new String[]{"ヴゃ","ヴぃ","ヴゅ","ヴぇ","ヴょ"});
		TABLE.put("sh", new String[]{"しゃ","し","しゅ","しぇ","しょ"});
		TABLE.put("sy", new String[]{"しゃ","し","しゅ","しぇ","しょ"});
		TABLE.put( "c", new String[]{"か","し","く","せ","こ"});
		TABLE.put("ch", new String[]{"ちゃ","ち","ちゅ","ちぇ","ちょ"});
		TABLE.put("cy", new String[]{"ちゃ","ち","ちゅ","ちぇ","ちょ"});
		TABLE.put( "f", new String[]{"ふぁ","ふぃ","ふ","ふぇ","ふぉ"});
		TABLE.put("fy", new String[]{"ふゃ","ふぃ","ふゅ","ふぇ","ふょ"});
		TABLE.put("fw", new String[]{"ふぁ","ふぃ","ふ","ふぇ","ふぉ"});
		TABLE.put( "q", new String[]{"くぁ","くぃ","く","くぇ","くぉ"});
		TABLE.put("ky", new String[]{"きゃ","きぃ","きゅ","きぇ","きょ"});
		TABLE.put("kw", new String[]{"くぁ","くぃ","く","くぇ","くぉ"});
		TABLE.put("ty", new String[]{"ちゃ","ちぃ","ちゅ","ちぇ","ちょ"});
		TABLE.put("ts", new String[]{"つぁ","つぃ","つ","つぇ","つぉ"});
		TABLE.put("th", new String[]{"てゃ","てぃ","てゅ","てぇ","てょ"});
		TABLE.put("tw", new String[]{"とぁ","とぃ","とぅ","とぇ","とぉ"});
		TABLE.put("ny", new String[]{"にゃ","にぃ","にゅ","にぇ","にょ"});
		TABLE.put("hy", new String[]{"ひゃ","ひぃ","ひゅ","ひぇ","ひょ"});
		TABLE.put("my", new String[]{"みゃ","みぃ","みゅ","みぇ","みょ"});
		TABLE.put("ry", new String[]{"りゃ","りぃ","りゅ","りぇ","りょ"});
		TABLE.put( "l", new String[]{"ぁ","ぃ","ぅ","ぇ","ぉ"});
		TABLE.put( "x", new String[]{"ぁ","ぃ","ぅ","ぇ","ぉ"});
		TABLE.put("ly", new String[]{"ゃ","ぃ","ゅ","ぇ","ょ"});
		TABLE.put("lt", new String[]{"た","ち","っ","て","と"});
		TABLE.put("lk", new String[]{"ヵ","き","く","ヶ","こ"});
		TABLE.put("xy", new String[]{"ゃ","ぃ","ゅ","ぇ","ょ"});
		TABLE.put("xt", new String[]{"た","ち","っ","て","と"});
		TABLE.put("xk", new String[]{"ヵ","き","く","ヶ","こ"});
		TABLE.put("wy", new String[]{"わ","ゐ","う","ゑ","を"});
		TABLE.put("wh", new String[]{"うぁ","うぃ","う","うぇ","うぉ"});
	};

	private static String getKanaFromTable(String s, int n) {
		if ( TABLE.containsKey(s) )  return TABLE.get(s)[n];
		else return s + TABLE.get("")[n];
	}

	private static int convVowel(String v) {
		switch(v) {
		case "a": return 0;
		case "i": return 1;
		case "u": return 2;
		case "e": return 3;
		case "o": return 4;
		default: return -1;
		}
	}

	protected static String conv(String bef) {

		String last = "";
		StringBuilder line = new StringBuilder();

		for ( int i=0; i < bef.length(); i++ ) {
			String tmp = bef.substring(i,i+1);
			if (convVowel(tmp) > -1) {
				line.append(getKanaFromTable(last, convVowel(tmp)));
				last = "";
			} else {
				if ( last.equals("n") && !(tmp.equals("y")) ) {
					line.append("ん");
					last = "";
					if ( tmp.equals("n") ) continue;
				}
				if ( Character.isLetter(tmp.charAt(0)) ) {
					if ( Character.isUpperCase(tmp.charAt(0)) ) {
						line.append(last + tmp);
						last = "";
					} else if ( last.equals(tmp) ) {
						line.append("っ");
						last = tmp;
					} else {
						last = last + tmp;
					}
				} else {
					if (tmp.equals("-")) line.append(last + "ー");
					else if (tmp.equals(".")) line.append(last + "。");
					else if (tmp.equals(",")) line.append(last + "、");
					else if (tmp.equals("?")) line.append(last + "？");
					else if (tmp.equals("!")) line.append(last + "！");
					else if (tmp.equals("[")) line.append(last + "「");
					else if (tmp.equals("]")) line.append(last + "」");
					else if (tmp.equals("<")) line.append(last + "＜");
					else if (tmp.equals(">")) line.append(last + "＞");
					else if (tmp.equals("&")) line.append(last + "＆");
					else if (tmp.equals("\"")) line.append(last + "”");
					else if (tmp.equals("(") || tmp.equals(")")) line.append(last);
					else line.append(last + tmp);
					last = "";
				}
			}
		}
		line.append(last);

		return line.toString();
	}
}