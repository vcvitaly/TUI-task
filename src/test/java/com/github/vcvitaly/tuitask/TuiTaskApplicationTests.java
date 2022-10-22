package com.github.vcvitaly.tuitask;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class TuiTaskApplicationTests {

	@MockBean
	private GitHub gitHub;

	@Test
	void contextLoads() {
	}

}
