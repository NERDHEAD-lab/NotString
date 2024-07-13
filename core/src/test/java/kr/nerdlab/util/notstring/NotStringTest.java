package kr.nerdlab.util.notstring;

import kr.nerdlab.util.notstring.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class NotStringTest {

	@Test
	public void testSingleObject() {
		User user = new User("류주성", 26, "남");
		NotString nstr = NotString.of("안녕 나는 ${user.name}이라고 해");
		nstr.put(user);

		String userName = null;
		try {
			userName = nstr.getVar("user.name");
		} catch (IndexOutOfBoundsException e) {
			Assertions.fail("IndexOutOfBoundsException thrown: " + e.getMessage());
		}
		Assertions.assertEquals("류주성", userName);

		String expected = "안녕 나는 류주성이라고 해";
		String result = nstr.toString();
		Assertions.assertEquals(expected, result);

		nstr.put(new User("류주현", 29, "남"));
		expected = "안녕 나는 류주현이라고 해";
		result = nstr.toString();
		Assertions.assertEquals(expected, result);
	}

	@Test
	public void testArrayAccess() {
		List<User> users = List.of(new User("류주성", 26, "남"), new User("류주현", 29, "남"));
		NotString nstr = NotString.of("안녕 나는 ${users[0].name}이고, 그는 ${users[1].name}의 형제야.");
		nstr.put(Map.of("users", users));

		String usersName = null;
		String secondUserName = null;
		try {
			usersName = nstr.getVar("users[0].name");
			secondUserName = nstr.getVar("users[1].name");
		} catch (IndexOutOfBoundsException e) {
			Assertions.fail("IndexOutOfBoundsException thrown: " + e.getMessage());
		}
		Assertions.assertEquals("류주성", usersName);
		Assertions.assertEquals("류주현", secondUserName);

		String expected = "안녕 나는 류주성이고, 그는 류주현의 형제야.";
		String result = nstr.toString();
		Assertions.assertEquals(expected, result);
	}

	@Test
	public void testArrayModification() {
		NotString nstr = NotString.of("안녕 나는 ${users[0].name}이고, 그는 ${users[1].name}의 형제야.");
		nstr.put(Map.of("users", List.of(new User("류주현", 29, "남"))));

		String expected = "안녕 나는 류주현이고, 그는 null의 형제야.";
		String result = nstr.toString();
		Assertions.assertEquals(expected, result);
	}

	@Test
	public void testOutOfBoundsAccess() {
		NotString nstr = NotString.of("안녕 나는 ${users[0].name}이고, 그는 ${users[1].name}의 형제야.");
		nstr.put(Map.of("users", List.of(new User("류주현", 29, "남"))));

		try {
			nstr.getVar("users[1].name");
			Assertions.fail("Expected IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException e) {
			Assertions.assertEquals("Index 1 is out of range for array users", e.getMessage());
		}
	}

	@Test
	public void testNullField() {
		NotString nstr = NotString.of("안녕 나는 ${user.name}이고, 내 나이는 ${user.age}이고, 내 성별은 ${user.gender}야.");
		nstr.put(new User("류주성", 26, null));

		String expected = "안녕 나는 류주성이고, 내 나이는 26이고, 내 성별은 null야.";
		String result = nstr.toString();
		Assertions.assertEquals(expected, result);
	}
}

