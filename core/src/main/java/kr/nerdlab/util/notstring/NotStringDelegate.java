package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.framework.NotStringFactory;

public class NotStringDelegate implements NotString {
	private final String template;
	private NotString delegate;
	private Class<?> currentType;

	public NotStringDelegate(String template) {
		this.template = template;
	}

	@Override
	public void put(Object object) {
		if (delegate == null || !object.getClass().equals(currentType)) {
			delegate = NotStringFactory.create(template, object);
			currentType = object.getClass();
		} else {
			delegate.put(object);
		}
	}

	@Override
	public String getVar(String var) {
		if (delegate == null) {
			throw new IllegalStateException("Delegate not initialized");
		}
		return delegate.getVar(var);
	}

	@Override
	public String toString() {
		if (delegate == null) {
			throw new IllegalStateException("Delegate not initialized");
		}
		return delegate.toString();
	}
}

