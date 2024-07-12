package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.handler.NotToStringEntityHandler;

import java.util.HashMap;
import java.util.Map;

public class NotStringBuilder<VALUE> {
	private String format;
	private Map<String, VALUE> keyValues;

	NotStringBuilder(String format) {
		this.format = format;
	}


	public static <VALUE> NotString<VALUE> from(String format) {
		return ImNotString.from(format, null);
	}
	public static <VALUE> NotString<VALUE> from(String format, Map<String, VALUE> keyValues) {
		return ImNotString.from(format, keyValues);
	}
	public static <VALUE> NotString<VALUE> from(String format, Map<String, VALUE> keyValues, NotToStringEntityHandler<VALUE> valueHandler) {
		return ImNotString.from(format, keyValues);
	}

	public NotStringBuilder<VALUE> of(String format) {
		NotStringBuilder<VALUE> builder = new NotStringBuilder<>(format);
		this.format = format;
		return builder;
	}

	public NotStringBuilder<VALUE> of(String format, Map<String, VALUE> keyValues) {
		NotStringBuilder<VALUE> builder = new NotStringBuilder<>(format);
		this.format = format;
		this.keyValues = keyValues;
		return builder;
	}

	public NotStringBuilder<VALUE> addKeyValue(String key, VALUE value) {
		if (this.keyValues == null) {
			this.keyValues = new HashMap<>();
		}

		this.keyValues.put(key, value);
		return this;
	}



}
