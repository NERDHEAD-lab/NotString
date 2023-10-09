package kr.nerdlab.util.notstring.entity;

import kr.nerdlab.util.notstring.env.NotStringType;

public class ImNotStringMetaEntity<VALUE> implements NotStringMetaEntity<VALUE> {
	private NotStringType type;
	private String key;
	private String defaultValue;
	private VALUE value;

	private ImNotStringMetaEntity(NotStringType type, String key, String defaultValue, VALUE value) {
		this.type = type;
		this.key = key;
		this.defaultValue = defaultValue;
		this.value = value;
	}

	public static <VALUE> ImNotStringMetaEntity<VALUE> from(NotStringType type, String key, String defaultValue, VALUE value) {
		return new ImNotStringMetaEntity<>(type, key, defaultValue, value);
	}

	@Override
	public NotStringType type() {
		return type;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public String defaultValue() {
		return defaultValue;
	}

	@Override
	public VALUE value() {
		return value;
	}


	public ImNotStringMetaEntity<VALUE> type(NotStringType type) {
		this.type = type;
		return this;
	}

	public ImNotStringMetaEntity<VALUE> key(String key) {
		this.key = key;
		return this;
	}

	public ImNotStringMetaEntity<VALUE> defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public ImNotStringMetaEntity<VALUE> value(VALUE value) {
		this.value = value;
		return this;
	}
}
