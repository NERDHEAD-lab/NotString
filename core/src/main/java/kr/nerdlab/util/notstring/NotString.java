package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.framework.NotStringFactory;

public interface NotString {
	void put(Object object);
	String getVar(String var) throws IndexOutOfBoundsException;
	String toString();

	static NotString of(String template) {
		return new NotStringDelegate(template);
	}

	static NotString of(String template, Object entity) {
		return NotStringFactory.create(template, entity);
	}
}



