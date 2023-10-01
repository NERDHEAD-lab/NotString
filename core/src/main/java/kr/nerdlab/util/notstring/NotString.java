package kr.nerdlab.util.notstring;

import kr.nerdlab.lang.exception.NotStringIsNullException;
import kr.nerdlab.util.notstring.env.NotStringEnvironment;
import kr.nerdlab.util.notstring.env.NotStringGlobalEnvironment;
import kr.nerdlab.util.notstring.env.NotStringType;
import kr.nerdlab.util.notstring.handler.DefaulNotToStringHandler;
import kr.nerdlab.util.notstring.handler.NotToStringHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotString<VALUE> {
	public static final NotStringEnvironment DEFAULT_CONFIGS = NotStringGlobalEnvironment.INSTANCE;

	private final static List<String> escapeLists = List.of("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|");

	private NotStringEnvironment environment = DEFAULT_CONFIGS;
	private String notString;
	private String originalNotString;
	private Map<String, VALUE> mapper;
	private final Map<String, String> defaultValues = new HashMap<>();
	private NotToStringHandler<VALUE> valueHandler = new DefaulNotToStringHandler<>();
	private NotToStringHandler<String> defaultValueHandler = new DefaulNotToStringHandler<>();

	//Constructors start
	private NotString(String notString, Map<String, VALUE> mapper, NotToStringHandler<VALUE> valueHandler) {
		this.originalNotString = notString;
		this.mapper = new HashMap<>(mapper);
		this.valueHandler = valueHandler != null ? valueHandler : this.valueHandler;

		initDefaultValues();
	}

	private void initDefaultValues() {
		//#{key:defaultValue} -> defaultValue
		//${key:defaultValue} -> defaultValue

		for (NotStringType type : NotStringType.values()) {
			String start = escapeRegex(startWith(type));
			String end = escapeRegex(endWith(type));


			String pattern = String.format("%s(?<key>[^:}]+)(?::(?<defaultValue>[^}]+))?%s", start, end);
			Pattern compile = Pattern.compile(pattern);
			Matcher matcher = compile.matcher(originalNotString);


			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				String key = matcher.group("key"); // Extract the key
				String value = matcher.group("defaultValue"); // Extract the default value

				value = (value != null && !value.isEmpty()) ? value : "null";
				matcher.appendReplacement(sb, start + key + end);

				defaultValues.put(key, value);
				if (!mapper.containsKey(key)) {
					if (type == NotStringType.NOTNULL)
						throw new NotStringIsNullException(String.format("NotString value is null: %s%s%s", startWith(NotStringType.NOTNULL), key, endWith(NotStringType.NOTNULL)));
					else if (type == NotStringType.NULLABLE) {
						mapper.put(key, null);
					} else {
						throw new RuntimeException("NotStringType is not defined");
					}
				}

				//${key:defaultValue} -> ${key}
				//#{key:defaultValue} -> #{key}
			}
			matcher.appendTail(sb);

			notString = sb.toString();
		}
	}

	private String escapeRegex(String start) {

		for (String escape : escapeLists) {
			start = start.replace(escape, "\\" + escape);
		}
		return start;
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
		this.originalNotString = notString;
		initDefaultValues();
	}

	public String getNotString() {
		return originalNotString;
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

	private String startWith(NotStringType type) {
		return environment.getStart(type);
	}

	private String endWith(NotStringType type) {
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

	@Override
	public String toString() {
		String result = notString;

		for (Map.Entry<String, VALUE> entry : mapper.entrySet()) {
			String key = entry.getKey();
			VALUE value = entry.getValue();
//			String valueString;
//			String temp;
//			if (value != null) {
//				valueString = valueHandler.handle(key, value);
//			} else {
//				valueString = defaultValueHandler.handle(key, defaultValues.get(key));
//			}
//			temp = result.replace(
//					getStart(NotStringType.NULLABLE) + key + getEnd(NotStringType.NULLABLE),
//					valueString
//			);
//
//			if (!temp.equals(result) && value != null) {
//				result = temp;
//			}
//
//			temp = result.replace(
//					getStart(NotStringType.NOTNULL) + key + getEnd(NotStringType.NOTNULL),
//					valueString
//			);
//			if (!temp.equals(result) && value != null) {
//				result = temp;
//			} else {
//				throw new NotStringIsNullException(String.format("NotString value is null: %s%s%s", getStart(NotStringType.NOTNULL), key, getEnd(NotStringType.NOTNULL)));
//			}
			result = toStringWithNotNull(result, key, value);
			result = toStringWithNullable(result, key, value);
		}

		return result;
	}

	private String toStringWithNotNull(String result, String key, VALUE value) {
		String keyPattern = startWith(NotStringType.NOTNULL) + key + endWith(NotStringType.NOTNULL);
		String defaultValue = defaultValues.get(key);
		String valueString;

		if (!result.contains(keyPattern)) { //result에 key가 없을 경우 처리안함
			return result;
		} else if (value == null && defaultValue == null) { //value와 defaultValue가 모두 null일 경우 처리안함
			return result;
		}
		if (value == null) { //value가 null일 경우 defaultValue로 대체
			valueString = defaultValueHandler.toString(key, defaultValue);
		}
		//value가 null이 아닐 경우 value로 대체
		valueString = valueHandler.toString(key, value);//value가 null이 아닐 경우 value를 String으로 변환하는 로직이 필요함.
		return result.replace(keyPattern, valueHandler.handle(key, valueString));

	}

	private String toStringWithNullable(String notString, String key, VALUE value) {
		String keyPattern = startWith(NotStringType.NULLABLE) + key + endWith(NotStringType.NULLABLE);
		String defaultValue = defaultValues.get(key);

		if (!notString.contains(keyPattern)) { //notString에 key가 없을 경우 처리안함
			return notString;
		} else if (value == null && defaultValue == null) { //value와 defaultValue가 모두 null일 경우 처리안함
			return notString;
		}


		String valueString;
		if (value == null) { //value가 null일 경우 defaultValue로 대체
			valueString = defaultValueHandler.toString(key, defaultValue);
		} else { //value가 null이 아닐 경우 value로 대체
			//value에서 String을 뽑아내는 로직과, 조합하는 로직을 분리해야함.
			valueString = valueHandler.toString(key, value);//value가 null이 아닐 경우 value를 String으로 변환하는 로직이 필요함.
		}

		return notString.replace(keyPattern, valueHandler.handle(key, valueString));
	}

}
