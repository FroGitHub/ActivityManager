package back.activitymanager;

import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(ActivityManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
