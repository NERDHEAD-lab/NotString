package kr.nerdlab.util.notstring.env;

import kr.nerdlab.lang.entity.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NotStringEnvironment {
	private final Map<NotStringType, Pair<String, String>> notStringStartAndEnd =
			Arrays.stream(NotStringType.values()).collect(
					HashMap::new,
					(map, type) -> map.put(type, type.defualtPair()),
					HashMap::putAll
			);

	public NotStringEnvironment setWhatIsNot(NotStringType type, String start, String end) {
		notStringStartAndEnd.put(type, Pair.of(start, end));
		return this;
	}

	public NotStringEnvironment disableWhatIsNot(NotStringType type) {
		notStringStartAndEnd.put(type, Pair.empty());
		return this;
	}

	private Pair<String, String> pair(NotStringType type) {
		return notStringStartAndEnd.get(type);
	}

	public String getStart(NotStringType type) {
		return pair(type).first();
	}

	public String getEnd(NotStringType type) {
		return pair(type).second();
	}
}
