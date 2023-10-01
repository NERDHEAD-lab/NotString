package kr.nerdlab.util.notstring.handler;

public class DefaulNotToStringHandler<VALUE> implements NotToStringHandler<VALUE>{
	@Override
	public String handle(String key, String value) {
		return value;
	}

	@Override
	public String toString(String key, VALUE value) {
		return value.toString();
	}
}
