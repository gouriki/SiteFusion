package net.gouriki.sitefusion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	public static final String UA = "Mozilla/5.0 (Linux; U; Android 4.0.1; ja-jp; Galaxy Nexus Build/ITL41D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30)";

	public static String replaceWithRegex(String regex, String target,
			String replacement) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(target);
		if (m.find()) {
			return m.replaceAll(replacement);
		} else {
			return target;
		}
	}
}
