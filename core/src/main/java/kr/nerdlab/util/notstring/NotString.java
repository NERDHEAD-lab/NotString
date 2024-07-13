package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.framework.NotStringFactory;

public interface NotString {
	void put(Object object);
	String getVar(String var) throws IndexOutOfBoundsException;
	String toString();
}



