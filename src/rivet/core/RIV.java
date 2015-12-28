package rivet.core;

import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("serial")
public class RIV extends HashMap<Integer, Integer> {
	
	public static RIV fromString (String mapString) {
		RIV ret = new RIV();
		Arrays.stream(
					mapString.trim()
						.substring(1, mapString.length() - 2)
						.split(", "))
			.map((x) -> x.split("="))
			.forEach((entry) -> ret.put(
									Integer.parseInt(entry[0]),
									Integer.parseInt(entry[1])));
		return ret;		
	}
	
}
