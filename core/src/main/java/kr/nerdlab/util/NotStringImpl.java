package kr.nerdlab.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NotStringImpl implements NotString {
	private final String template;
	private final Map<String, JsonElement> context = new HashMap<>();
	private final Gson gson = new Gson();

	NotStringImpl(String template) {
		this.template = template;
	}

	@Override
	public void put(Object object) {
		if (object instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) object;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String key = entry.getKey().toString();
				JsonElement jsonElement = gson.toJsonTree(entry.getValue());
				context.put(key, jsonElement);
			}
		} else {
			String key = object.getClass().getSimpleName().toLowerCase();
			JsonElement jsonElement = gson.toJsonTree(object);
			context.put(key, jsonElement);
		}
	}

	@Override
	public String getVar(String var) throws IndexOutOfBoundsException {
		String[] parts = var.split("\\.");
		JsonElement element = context.get(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			if (element == null || element.isJsonNull()) {
				return "null";
			}

			Matcher matcher = Pattern.compile("(\\w+)\\[(\\d+)]").matcher(parts[i]);
			if (matcher.matches()) {
				String arrayName = matcher.group(1);
				int index = Integer.parseInt(matcher.group(2));
				if (element.isJsonObject() && element.getAsJsonObject().has(arrayName)) {
					element = element.getAsJsonObject().get(arrayName);
					if (element.isJsonArray()) {
						JsonArray jsonArray = element.getAsJsonArray();
						if (index < jsonArray.size()) {
							element = jsonArray.get(index);
						} else {
							throw new IndexOutOfBoundsException("Index " + index + " is out of range for array " + arrayName);
						}
					} else {
						return "null";
					}
				} else {
					return "null";
				}
			} else {
				if (element.isJsonObject()) {
					element = element.getAsJsonObject().get(parts[i]);
				} else {
					return "null";
				}
			}
		}
		return element != null && !element.isJsonNull() ? element.getAsString() : "null";
	}

	@Override
	public String toString() {
		String result = template;
		Pattern pattern = Pattern.compile("\\$\\{(.+?)}");
		Matcher matcher = pattern.matcher(template);

		while (matcher.find()) {
			String variable = matcher.group(1);
			String value;
			try {
				value = getVar(variable);
			} catch (IndexOutOfBoundsException e) {
				value = "null";
			}
			if (value == null) {
				value = "null";
			}
			result = result.replace("${" + variable + "}", value);
		}
		return result;
	}
}