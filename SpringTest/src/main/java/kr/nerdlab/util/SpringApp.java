package kr.nerdlab.util;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"kr.nerdlab.util"})
public class SpringApp {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		SpringApplication.run(SpringApp.class, args);
	}
}
