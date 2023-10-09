package kr.nerdlab.util.notstring;

import kr.nerdlab.lang.exception.NotStringIsNullException;
import kr.nerdlab.util.notstring.env.NotStringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;
import java.util.Map;

public class NotStringTest {
	Logger logger = LoggerFactory.getLogger(NotStringTest.class);

	@Test
	public void replaceStringTest() {
		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);

		Assertions.assertEquals(
				"this is nerdlab's story",
				notString.toString()
		);

		notString.put("name", "yanagi");
		notString.put("what", "helloWorld");

		Assertions.assertEquals(
				"this is yanagi's helloWorld",
				notString.toString()
		);
	}

	@Test
	public void setWhatIsNotTest() {
		NotString.DEFAULT_CONFIGS
				.setWhatIsNot(NotStringType.NULLABLE, "${{", "}}")
				.setWhatIsNot(NotStringType.NOTNULL, "#{{", "}}");

		String testString = "this is #{{name}}'s ${{what:story}}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);

		Assertions.assertEquals(
				"this is nerdlab's story",
				notString.toString()
		);
	}

	//${} -> null일 경우 ${name}으로 표시 (nullable)
	@Test
	public void nullableTest() {
		String testString = "this is #{name}'s ${what:story}";

		NotString<String> notString = ImNotString.from(testString, Map.of("name", "nerdlab"));
		notString.put("what", null);
		Assertions.assertEquals(
				"this is nerdlab's story",
				notString.toString()
		);
	}

	//#{} -> null일 경우 NotStringIsNullException 발생 (notnull)
	@Test
	public void notnullExceptionTest() {
		String testString = "this is #{name}'s ${what:story}";

//		notString.toString()을 예외 발생시키기에는 dubug에서 자동으로 toString을 호출하기 때문에 불편함이 있음
		Assertions.assertThrows(NotStringIsNullException.class, () -> {
			NotString<Object> notString = ImNotString.from(testString);
		});
	}

	@Test
	public void toNotStringTest() {
		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);

		Assertions.assertEquals(
				"this is #{name}'s ${what}",
				notString.getNotString()
		);
	}

	@Test
	public void getOriginNotStringTest() {
		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);

		Assertions.assertEquals(
				testString,
				notString.getOriginalNotString()
		);
	}

	@Test
	public void toMapTest() {
		String testString = "this is ${name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);
		Map<String, String> notStringMap = notString.keyValues();

		Assertions.assertEquals(
				notStringMap.get("name"),
				"nerdlab"
		);

		//default 값이 있더라도 put으로 값을 넣지 않으면 null
		Assertions.assertNull(notStringMap.get("what"));
	}

	@Test
	public void toStringHandler1Test() {
		String testString = "this is ${name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
//		NotString<String> notString = NotString.from(
//				testString,
//				map,
//				NotToStringHandlerBuilder.builder(String.class)
//						.handle((key, value) -> String.format("[%s = %s]", key, value))
//						.build()
//		);
//
//		Assertions.assertEquals(
//				"this is [name = nerdlab]'s [what = story]",
//				notString.toString()
//		);
	}

	@Test
	public void toStringHandler2Test() {
		String testString = "this is ${name}'s ${what:story}";
		List<String> valueOfName = List.of("empty", "nerdlab");
		Map<String, List<String>> keyValues = Map.of("name", valueOfName);
		NotString<List<String>> notString = ImNotString.from(
				testString,
				keyValues,
//				NotStringBuilder.builder(valueOfName)
//						.handle((key, value) -> String.format("[%s = %s]", key, value))
//						.toString((key, value) -> value.get(1))
//						.build()
				entity -> String.format("[%s = %s]", entity.key(), entity.value().get(1))
		);

		Assertions.assertEquals(
				"this is [name = nerdlab]'s [what = story]",
				notString.toString()
		);
	}

	@Test
	public void disableWhatIsNotTest() {
		NotString.DEFAULT_CONFIGS
				.disableWhatIsNot(NotStringType.NULLABLE)
				.disableWhatIsNot(NotStringType.NOTNULL);

		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> keyValues = Map.of("name", "nerdlab");
		NotString<String> notString = ImNotString.from(testString, keyValues);

		Assertions.assertEquals(
				testString,
				notString.toString()
		);
	}

}
