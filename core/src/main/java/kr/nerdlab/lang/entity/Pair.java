package kr.nerdlab.lang.entity;

public class Pair<TYPE> {
	private final TYPE first;
	private final TYPE second;

	private Pair(TYPE first, TYPE second) {
		this.first = first;
		this.second = second;
	}

	public static <TYPE> Pair<TYPE> of(TYPE a, TYPE b) {
		return new Pair<>(a, b);
	}

	public static <TYPE> Pair<TYPE> empty() {
		return new Pair<>(null, null);
	}

	public TYPE first() {
		return first;
	}

	public TYPE second() {
		return second;
	}
}
