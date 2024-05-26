package com.example.demo.medium;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserCreateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean //test를 실행할 때 MockBean이 주입돼서 실행된다.
	private JavaMailSender javaMailSender;

	private final ObjectMapper objectMapper = new ObjectMapper();
	@Test
	void 사용자는_회원가입을_할_수_있고_회원가입_된_사용자는_PENDING_상태이다() throws Exception {
		UserCreate userCreate = UserCreate.builder()
			.email("testId1")
			.nickname("testNickname1")
			.address("Pangyo")
			.build();

		doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userCreate)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("testId1"))
			.andExpect(jsonPath("$.nickname").value("testNickname1"))
			.andExpect(jsonPath("$.status").value("PENDING"));
	}

}
