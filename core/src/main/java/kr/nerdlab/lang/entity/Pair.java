package kr.nerdlab.lang.entity;

public class Pair<TYPE_A, TYPE_B> {
	private final TYPE_A first;
	private final TYPE_B second;

	private Pair(TYPE_A first, TYPE_B second) {
		this.first = first;
		this.second = second;
	}

	public static <TYPE_A, TYPE_B> Pair<TYPE_A, TYPE_B> of(TYPE_A a, TYPE_B b) {
		return new Pair<>(a, b);
	}

	public static <TYPE> Pair<TYPE, TYPE> empty() {
		return new Pair<>(null, null);
	}

	public TYPE_A first() {
		return first;
	}

	public TYPE_B second() {
		return second;
	}
}
