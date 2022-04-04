package io.chilborne.filmfanatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class FilmFanaticApplicationTests {

	@Test
	void contextLoads() {
	}

}
