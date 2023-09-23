package kr.nerdlab.util.notstring.handler;

public interface NotToStringHandler<VALUE> {
	String handle(String Key, VALUE value);
}
