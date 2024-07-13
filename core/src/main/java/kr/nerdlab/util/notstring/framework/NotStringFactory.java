package kr.nerdlab.util.notstring.framework;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import kr.nerdlab.util.notstring.NotString;
import kr.nerdlab.util.notstring.NotStringImpl;

import java.util.Map;

public class NotStringFactory {
	private static final Gson gson = new Gson();

	public static NotString create(String template, Object entity) {
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



