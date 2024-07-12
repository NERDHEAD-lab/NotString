package kr.nerdlab.util.notstring.feature;

import kr.nerdlab.util.notstring.ImNotString;

import java.util.Map;


//Proxy로 특정 method를 override하여 테스트 하기 위한 클래스
//TODO : 나중에 연구하자
public class NotStringProxy<VALUE> extends ImNotString<VALUE> {
	public NotStringProxy(String notString, Map<String, VALUE> keyValues) {
		super(notString, keyValues, null);
	}

	private String escapeRegex_v1(String input) {
		String regexSpecialCharacters = "[\\[\\\\^$.|?*+(){}]";
		if (input == null || input.isEmpty()) return input;
		return input.replaceAll(regexSpecialCharacters, "\\\\$0");
	}

	private String escapeRegex_v2(String input) {
		Map<Character, String> escapeMap =
				Map.ofEntries(
						Map.entry('$', "\\$"), Map.entry('(', "\\("), Map.entry(')', "\\)"), Map.entry('*', "\\*"),
						Map.entry('+', "\\+"), Map.entry('.', "\\."), Map.entry('[', "\\["), Map.entry(']', "\\]"),
						Map.entry('?', "\\?"), Map.entry('^', "\\^"), Map.entry('{', "\\{"), Map.entry('}', "\\}"),
						Map.entry('|', "\\|"), Map.entry('\\', "\\\\")
				);


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

	private void put_v1(String key, String defaultValue, VALUE value, final boolean force) {
//		NotStringMetaEntity<VALUE> originEntity = mapper.get(key);
//		if(defaultValue == null && value == null) {
//			return;
//		} else if(defaultValue == null) {
//			defaultValue = originEntity.defaultValue();
//		} else if(value == null) {
//			value = originEntity.value();
//		}
//
//			try {
//				if(!Objects.equals(defaultValue, originEntity.defaultValue())) {
//					defaultValueField.set(originEntity, defaultValue);
//				}
//				if(!Objects.equals(value, originEntity.value())) {
//					valueField.set(originEntity, value);
//				}
//			} catch (IllegalAccessException e) {
//				throw new RuntimeException(e);
//			}
	}

}
