package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.env.NotStringEnvironment;
import kr.nerdlab.util.notstring.env.NotStringGlobalEnvironment;
import kr.nerdlab.util.notstring.handler.NotToStringEntityHandler;

import java.util.Map;


public interface NotString<VALUE> {
	NotStringEnvironment DEFAULT_CONFIGS = NotStringGlobalEnvironment.INSTANCE;

	//configs
	NotStringEnvironment environment();

	NotString<VALUE> environment(NotStringEnvironment environment);

	NotString<VALUE> notString(String notString);

	NotString<VALUE> valueHandler(NotToStringEntityHandler<VALUE> valueHandler);

	void putAll(Map<String, VALUE> keyValues);

	//mappers
	void put(String key, VALUE value);

	VALUE get(String key);

	void putDefaultAll(Map<String, String> keyDefaultValues);

	void putDefault(String key, String defaultValue);

	String getDefault(String key);

	Map<String, VALUE> keyValues();

	Map<String, String> keyDefaultValues();


	String getOriginalNotString();

	String getNotString();

	String toString();

}
