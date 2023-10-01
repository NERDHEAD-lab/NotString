package kr.nerdlab.util.notstring.handler;

public interface NotToStringHandler<VALUE> {
	String handle(String key, String value);

	String toString(String key, VALUE value);
}
