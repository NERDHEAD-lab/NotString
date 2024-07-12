package kr.nerdlab.util;

import kr.nerdlab.util.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@Autowired
	TestService service;

	TestClazz testClazz;

	@GetMapping( "/hello")
	public String hello() {
		testClazz.hello();
		return service.hello();
	}

	@RequestMapping(
			value = "/",
			produces = "application/json;charset=UTF-8",
			method = { RequestMethod.GET, RequestMethod.POST }
	)
	public String index() {
		System.out.println("Hello World!");
		return "Hello World!";
	}
}

