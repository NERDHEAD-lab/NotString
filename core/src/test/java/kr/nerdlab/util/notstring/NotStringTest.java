package kr.nerdlab.util.notstring;

import kr.nerdlab.lang.exception.NotStringIsNullException;
import kr.nerdlab.util.notstring.env.NotStringType;
import kr.nerdlab.util.notstring.handler.NotToStringHandler;
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
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);

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
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);

		Assertions.assertEquals(
				"this is nerdlab's story",
				notString.toString()
		);
	}

	//${} -> null일 경우 ${name}으로 표시 (nullable)
	@Test
	public void nullableTest() {
		String testString = "this is #{name}'s ${what:story}";

		NotString<String> notString = NotString.from(testString, Map.of("name", "nerdlab"));
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
			NotString<Object> notString = NotString.from(testString);
		});
	}

	@Test
	public void toNotStringTest() {
		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);

		Assertions.assertEquals(
				"this is #{name}'s ${what}",
				notString.toNotString()
		);
	}

	@Test
	public void getOriginNotStringTest() {
		String testString = "this is #{name}'s ${what:story}";
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);

		Assertions.assertEquals(
				testString,
				notString.getOriginalNotString()
		);
	}

	@Test
	public void toMapTest() {
		String testString = "this is ${name}'s ${what:story}";
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);
		Map<String, String> notStringMap = notString.toMap();

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
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(
				testString,
				map,
//				(key, value) -> {
//					/*"[" + key + " = " + value + "]"*/
//					return String.format("[%s = %s]", key, value);
//				}
				new NotToStringHandler<>() {
					@Override
					public String handle(String key, String value) {
						return String.format("[%s = %s]", key, value);
					}

					@Override
					public String toString(String key, String value) {
						return value;
					}
				}
		);

		Assertions.assertEquals(
				"this is [name = nerdlab]'s [what = story]",
				notString.toString()
		);
	}

	@Test
	public void toStringHandler2Test() {
		String testString = "this is ${name}'s ${what:story}";
		Map<String, List<String>> map = Map.of("name", List.of("empty", "nerdlab"));
		NotString<List<String>> notString = NotString.from(
				testString,
				map,

				new NotToStringHandler<>() {
					@Override
					public String handle(String key, String value) {
						return String.format("[%s = %s]", key, value);
					}

					@Override
					public String toString(String key, List<String> value) {
						return value.get(1);
					}
				}
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
		Map<String, String> map = Map.of("name", "nerdlab");
		NotString<String> notString = NotString.from(testString, map);

		Assertions.assertEquals(
				testString,
				notString.toString()
		);
	}

}
