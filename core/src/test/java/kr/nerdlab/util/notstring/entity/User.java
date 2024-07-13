package kr.nerdlab.util.notstring.entity;

import java.util.List;
import java.util.Map;

public class User {
	private final String name;
	private final int age;
	private final String gender;

	private List<Map<String, Object>> testList;

	public User(String name, int age, String sex) {
		this.name = name;
		this.age = age;
		this.gender = sex;
	}

	public User(String name, int age, String sex, List<Map<String, Object>> testList) {
		this.name = name;
		this.age = age;
		this.gender = sex;
		this.testList = testList;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getSex() {
		return gender;
	}

	public List<Map<String, Object>> getTestList() {
		return testList;
	}
}
