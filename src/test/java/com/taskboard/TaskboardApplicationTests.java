package com.taskboard;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Set;

@SpringBootTest
public class TaskboardApplicationTests {

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Test
	public void contextLoads() {
		System.out.println(passwordEncoder.encode("qwerty"));
	}

}
