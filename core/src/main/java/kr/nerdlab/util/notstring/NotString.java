package kr.nerdlab.util.notstring;

import kr.nerdlab.lang.exception.NotStringIsNullException;
import kr.nerdlab.util.notstring.env.NotStringEnvironment;
import kr.nerdlab.util.notstring.env.NotStringGlobalEnvironment;
import kr.nerdlab.util.notstring.env.NotStringType;
import kr.nerdlab.util.notstring.handler.NotToStringHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NotString<VALUE> {
	public static final NotStringEnvironment DEFAULT_CONFIGS = NotStringGlobalEnvironment.INSTANCE;

	private NotStringEnvironment environment = DEFAULT_CONFIGS;
	private String notString;
	private Map<String, VALUE> mapper;
	private final Map<String, String> defaultValues = new HashMap<>();
	private NotToStringHandler<VALUE> valueHandler = (key, value) -> value.toString();
	private NotToStringHandler<String> defaultValueHandler = (key, value) -> value;

	//Constructors start
	private NotString(String notString, Map<String, VALUE> mapper, NotToStringHandler<VALUE> valueHandler) {
		this.notString = notString;
		this.mapper = new HashMap<>(mapper);
		this.valueHandler = valueHandler != null ? valueHandler : this.valueHandler;
	}

	public static <VALUE> NotString<VALUE> from(String notString) {
		return from(notString, Map.of());
	}

	public static <VALUE> NotString<VALUE> from(String notString, Map<String, VALUE> mapper) {
		return from(notString, mapper, null);
	}

	public static <VALUE> NotString<VALUE> from(String notString, Map<String, VALUE> mapper, NotToStringHandler<VALUE> handler) {
		return new NotString<>(notString, mapper, handler);
	}
	//Constructors end
	//init start


	//init end
	//Getters/Setters start
	public void setNotString(String notString) {
		this.notString = notString;
	}

	public void put(String key, VALUE value) {
		mapper.put(key, value);
	}

	public void get(String key) {
		mapper.get(key);
	}

	public void putDefault(String key, String defaultValue) {
		defaultValues.put(key, defaultValue);
	}

	public void getDefaultValue(String key) {
		defaultValues.get(key);
	}

	public NotString<VALUE> settings(NotStringEnvironment environment) {
		this.environment = environment;
		return this;
	}

	private String getStart(NotStringType type) {
		return environment.getStart(type);
	}

	private String getEnd(NotStringType type) {
		return environment.getEnd(type);
	}

	//Getters/Setters end
	//Functions start

	public String toNotString() {
		return notString;
	}

	public Map<String, VALUE> toMap() {
		return new HashMap<>(mapper);
	}

	public String toString() {
		String result = notString;

		for (Map.Entry<String, VALUE> entry : mapper.entrySet()) {
			String key = entry.getKey();
			VALUE value = entry.getValue();
			String valueString;
			String temp;
			if (value != null) {
				valueString = valueHandler.handle(key, value);
			} else {
				valueString = defaultValueHandler.handle(key, defaultValues.get(key));
			}
			temp = result.replace(
					getStart(NotStringType.NULLABLE) + key + getEnd(NotStringType.NULLABLE),
					valueString
			);

			if (!temp.equals(result) && value != null) {
				result = temp;
			}

			temp = result.replace(
					getStart(NotStringType.NOTNULL) + key + getEnd(NotStringType.NOTNULL),
					valueString
			);
			if (!temp.equals(result) && value != null) {
				result = temp;
			} else {
				throw new NotStringIsNullException(String.format("NotString value is null: %s%s%s", getStart(NotStringType.NOTNULL), key, getEnd(NotStringType.NOTNULL)));
			}


		}

		return result;
	}

	private String toStringWithNullable(String notString, String key, VALUE value) {
		String keyPattern = getStart(NotStringType.NULLABLE) + key + getEnd(NotStringType.NULLABLE);
		String defaultValue = defaultValues.get(key);

		if (!notString.contains(keyPattern)) { //notString에 key가 없을 경우 처리안함
			return notString;
		} else if (value == null && defaultValue == null) { //value와 defaultValue가 모두 null일 경우 처리안함
			return notString;
		} else if (value == null) { //value가 null일 경우 defaultValue로 대체
			return notString.replace(keyPattern, defaultValueHandler.handle(key, defaultValue));
		} else { //value가 null이 아닐 경우 value로 대체
			return notString.replace(keyPattern, valueHandler.handle(key, value));
		}

	}

}
