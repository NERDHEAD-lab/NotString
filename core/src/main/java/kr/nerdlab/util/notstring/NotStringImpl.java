package kr.nerdlab.util.notstring;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotStringImpl implements NotString {
	private final String template;
	private final Map<String, JsonElement> context = new HashMap<>();
	private final Gson gson = new Gson();

	public NotStringImpl(String template) {
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
		Pattern pattern = Pattern.compile("(\\w+)\\[(\\d+)\\]\\.(\\w+)");
		Matcher matcher = pattern.matcher(var);
		if (matcher.matches()) {
			String arrayName = matcher.group(1);
			int index = Integer.parseInt(matcher.group(2));
			String fieldName = matcher.group(3);
			if (context.containsKey(arrayName)) {
				JsonElement jsonElement = context.get(arrayName);
				if (jsonElement != null && jsonElement.isJsonArray()) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					if (jsonArray.size() > index) {
						JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
						if (jsonObject.has(fieldName)) {
							JsonElement fieldElement = jsonObject.get(fieldName);
							return fieldElement.isJsonNull() ? "null" : fieldElement.getAsString();
						}
						return "null";
					} else {
						throw new IndexOutOfBoundsException("Index " + index + " is out of range for array " + arrayName);
					}
				}
			}
		} else {
			String[] parts = var.split("\\.");
			if (parts.length == 2) {
				String objectName = parts[0];
				String fieldName = parts[1];
				if (context.containsKey(objectName)) {
					JsonElement jsonElement = context.get(objectName);
					if (jsonElement != null && jsonElement.isJsonObject()) {
						JsonObject jsonObject = jsonElement.getAsJsonObject();
						if (jsonObject.has(fieldName)) {
							JsonElement fieldElement = jsonObject.get(fieldName);
							return fieldElement.isJsonNull() ? "null" : fieldElement.getAsString();
						}
						return "null";
					}
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String result = template;
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
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