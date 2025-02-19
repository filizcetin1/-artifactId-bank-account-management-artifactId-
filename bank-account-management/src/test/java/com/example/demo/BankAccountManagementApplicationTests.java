package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
		"spring.datasource.username=sa",
		"spring.datasource.password=password",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.show-sql=true",
		"spring.jpa.properties.hibernate.format_sql=true",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class BankAccountManagementApplicationTests {

	@Test
	void contextLoads() {
		// Context yükleme testi
	}

}
