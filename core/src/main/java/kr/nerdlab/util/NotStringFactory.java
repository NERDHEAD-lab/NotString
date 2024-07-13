package kr.nerdlab.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

class NotStringFactory {
	private static final Gson gson = new Gson();

	static NotString create(String template, Object entity) {
		NotStringImpl notString = new NotStringImpl(template);
		JsonElement jsonElement = gson.toJsonTree(entity);

		if (jsonElement.isJsonObject()) {
			notString.put(entity);
		} else if (jsonElement.isJsonArray()) {
			notString.put(Map.of("list", entity));
		} else if (entity instanceof Map) {
			notString.put(entity);
		}

		return notString;
	}
}



