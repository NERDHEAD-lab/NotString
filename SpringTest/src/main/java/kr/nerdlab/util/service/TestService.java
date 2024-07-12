package kr.nerdlab.util.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
	public String hello() {
		System.out.println("Hello World!");
		return "Hello World!";
	}
}
