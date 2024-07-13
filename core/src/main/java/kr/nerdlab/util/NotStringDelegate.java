package kr.nerdlab.util;

class NotStringDelegate implements NotString {
	private final String template;
	private NotString delegate;

	NotStringDelegate(String template) {
		this.template = template;
	}

	@Override
	public void put(Object object) {
		if (delegate == null) {
			delegate = NotStringFactory.create(template, object);
		} else {
			delegate.put(object);
		}
	}

	@Override
	public String getVar(String var) throws IndexOutOfBoundsException {
		if (delegate == null) {
			throw new IllegalStateException("Delegate is not initialized");
		}
		return delegate.getVar(var);
	}

	@Override
	public String toString() {
		if (delegate == null) {
			return template;
		}
		return delegate.toString();
	}
}

