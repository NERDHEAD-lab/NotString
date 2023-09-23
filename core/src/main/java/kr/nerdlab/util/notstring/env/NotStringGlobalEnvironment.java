package kr.nerdlab.util.notstring.env;

public class NotStringGlobalEnvironment extends NotStringEnvironment {
	public static NotStringEnvironment INSTANCE = SingletonHolder.INSTANCE;

	private static class SingletonHolder {
		private static final NotStringEnvironment INSTANCE = new NotStringEnvironment();
	}
}
