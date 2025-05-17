package back.activitymanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class ActivityManagerApplicationTests {

	@MockBean
	private UserDetailsService userDetailsService;

	@Test
	void contextLoads() {
	}

}
