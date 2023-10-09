package kr.nerdlab.util.notstring.env;

import kr.nerdlab.lang.entity.Pair;

public enum NotStringType {
	NULLABLE("${", "}"),
	NOTNULL("#{", "}")
	;

	private final String start;
	private final String end;

	NotStringType(String start, String end) {
		this.start = start;
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public Pair<String, String> defualtPair() {
		return Pair.of(start, end);
	}
}
