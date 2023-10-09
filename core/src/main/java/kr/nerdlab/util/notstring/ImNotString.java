package kr.nerdlab.util.notstring;

import kr.nerdlab.lang.exception.NotStringIsNullException;
import kr.nerdlab.util.notstring.entity.ImNotStringMetaEntity;
import kr.nerdlab.util.notstring.env.NotStringEnvironment;
import kr.nerdlab.util.notstring.env.NotStringType;
import kr.nerdlab.util.notstring.handler.NotToStringEntityHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImNotString<VALUE> implements NotString<VALUE> {
	public static final Map<Character, String> escapeMap =
			Map.ofEntries(
					Map.entry('$', "\\$"), Map.entry('(', "\\("), Map.entry(')', "\\)"), Map.entry('*', "\\*"),
					Map.entry('+', "\\+"), Map.entry('.', "\\."), Map.entry('[', "\\["), Map.entry(']', "\\]"),
					Map.entry('?', "\\?"), Map.entry('^', "\\^"), Map.entry('{', "\\{"), Map.entry('}', "\\}"),
					Map.entry('|', "\\|"), Map.entry('\\', "\\\\")
			);


	private NotStringEnvironment environment = DEFAULT_CONFIGS;
	private String notString;
	private String originalNotString;
	private final Map<String, VALUE> keyValues = new HashMap<>();
	private final Map<String, String> keyDefaultValues = new HashMap<>();
	private NotToStringEntityHandler<VALUE> valueHandler = entity -> entity.value().toString();

	protected ImNotString(String notString, Map<String, VALUE> keyValues, NotToStringEntityHandler<VALUE> valueHandler) {
		initNotString(notString, keyValues);
		if (valueHandler != null) {
			this.valueHandler = valueHandler;
		}
	}

	private void initNotString(String notString, Map<String, VALUE> keyValues) {
		notString(notString);
		putAll(keyValues);
	}

	public NotString<VALUE> notString(String notString) {
		return notString(notString, false);
	}

	public NotString<VALUE> notString(String notString, boolean overrideDefaultValue) {
		this.originalNotString = notString;
		String temp = originalNotString;

		for (NotStringType type : NotStringType.values()) {
			String start = startWith(type);
			String end = endWith(type);

			String pattern = String.format("%s(?<key>[^:}]+)(?::(?<defaultValue>[^}]+))?%s", escapeRegex(start), escapeRegex(end));
			Pattern compile = Pattern.compile(pattern);
			Matcher matcher = compile.matcher(temp);

			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String key = matcher.group("key"); // Extract the key
				String defaultValue = matcher.group("defaultValue"); // Extract the default value

				if (defaultValue != null && !defaultValue.isEmpty()) {
					if (overrideDefaultValue) {
						keyDefaultValues.put(key, defaultValue);
					} else {
						keyDefaultValues.putIfAbsent(key, defaultValue);
					}
				}

				matcher.appendReplacement(sb, start + key + end);
			}
			matcher.appendTail(sb);
			temp = sb.toString();
		}
		this.notString = temp;
		return this;
	}

	public NotString<VALUE> valueHandler(NotToStringEntityHandler<VALUE> valueHandler) {
		if (valueHandler != null) {
			this.valueHandler = valueHandler;
		}
		return this;
	}

	public static <VALUE> NotString<VALUE> from(String format) {
		return from(format, null);
	}

	public static <VALUE> NotString<VALUE> from(String format, Map<String, VALUE> keyValues) {
		return from(format, keyValues, null);
	}

	public static <VALUE> NotString<VALUE> from(String format, Map<String, VALUE> keyValues, NotToStringEntityHandler<VALUE> valueHandler) {
		return new ImNotString<>(format, keyValues, valueHandler);
	}

	@Override
	public NotStringEnvironment environment() {
		return environment;
	}

	@Override
	public NotString<VALUE> environment(NotStringEnvironment environment) {
		this.environment = environment;
		return this;
	}

	@Override
	public void putAll(Map<String, VALUE> keyValues) {
		if (keyValues != null) {
			this.keyValues.putAll(keyValues);
		}
	}

	@Override
	public void put(String key, VALUE value) {
		keyValues.put(key, value);
	}

	@Override
	public VALUE get(String key) {
		return keyValues.get(key);
	}

	@Override
	public void putDefaultAll(Map<String, String> keyDefaultValues) {
		if (keyDefaultValues != null) {
			this.keyDefaultValues.putAll(keyDefaultValues);
		}
	}

	@Override
	public void putDefault(String key, String defaultValue) {
		keyDefaultValues.put(key, defaultValue);
	}

	@Override
	public String getDefault(String key) {
		return keyDefaultValues.get(key);
	}

	@Override
	public Map<String, VALUE> keyValues() {
		//mapper의 value인 NotStringMetaEntity의 value를 가져온다.
		return new HashMap<>(keyValues);
	}

	@Override
	public Map<String, String> keyDefaultValues() {
		return new HashMap<>(keyDefaultValues);
	}

	@Override
	public String getOriginalNotString() {
		return originalNotString;
	}

	@Override
	public String getNotString() {
		return notString;
	}

	@Override
	public String toString() {
		String temp = notString;

		for (NotStringType type : NotStringType.values()) {
			String start = startWith(type);
			String end = endWith(type);

			String pattern = String.format("%s(?<key>[^:}]+)%s", escapeRegex(start), escapeRegex(end));
			Pattern compile = Pattern.compile(pattern);
			Matcher matcher = compile.matcher(temp);

			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String key = matcher.group("key"); // Extract the key
				matcher.appendReplacement(
						sb,
						Objects.requireNonNull(
								handle(
										ImNotStringMetaEntity.from(type, key, keyDefaultValues.get(key), keyValues.get(key))
								)
						)
				);
			}
			matcher.appendTail(sb);
			temp = sb.toString();
		}

		return temp;
	}

	private String handle(ImNotStringMetaEntity<VALUE> entity) {
		String value;


		String start = startWith(entity.type());
		String end = endWith(entity.type());

		switch (entity.type()) {
			case NULLABLE:
				if (entity.value() == null) {
					return start + entity.key() + end;
				}
				value = valueHandler.handle(entity);
				if (value == null) {
					value = "null";
				}
				break;
			case NOTNULL:
				if (entity.value() == null) {
					throw new NotStringIsNullException(String.format("NotString value is null: %s%s%s", start, entity.key(), end));
				} else {
					value = valueHandler.handle(entity);
					if (value == null) {
						throw new NotStringIsNullException(String.format("NotString value handler return null. return value cannot be null: %s%s%s", start, entity.key(), end));
					}
				}
			default:
				throw new RuntimeException("NotStringType is not defined");
		}

		return value;

	}

	private String escapeRegex(String input) {
		StringBuilder escapedString = new StringBuilder();
		for (char c : input.toCharArray()) {
			if (escapeMap.containsKey(c)) {
				escapedString.append(escapeMap.get(c));
			} else {
				escapedString.append(c);
			}
		}
		return escapedString.toString();
	}

	private String startWith(NotStringType type) {
		return environment.getStart(type);
	}

	private String endWith(NotStringType type) {
		return environment.getEnd(type);
	}
}
