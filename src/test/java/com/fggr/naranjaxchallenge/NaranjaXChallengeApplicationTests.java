package com.fggr.naranjaxchallenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fggr.naranjaxchallenge.dto.AccountDto;
import com.fggr.naranjaxchallenge.repository.AccountRepository;
import com.fggr.naranjaxchallenge.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NaranjaXChallengeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@BeforeEach
	public void deleteAllBeforeTests() {
		accountRepository.deleteAll();
		transactionRepository.deleteAll();
	}

	@Test
	@Order(1)
	public void shouldReturnNewAccount() throws Exception {

		executeAccountPost("Juan Perez", 1234567, "true", null, content().json(
				"{" +
						"\"id\": 1," +
						"\"name\": \"Juan Perez\"," +
						"\"dni\": 1234567," +
						"\"active_card\": true," +
						"\"available_amount\": 0," +
						"\"transactions\":[]" +
						"}"
		));
	}

	@Test
	@Order(2)
	public void shouldReturnDuplicateError() throws Exception {

		executeAccountPost("Juan Perez", 1234567, "true", null, status().isOk());

		executeAccountPost("Juan Perez", 1234567, "true", null, content().json(
				"{" +
						"\"code\": 400," +
						"\"message\":\"rules have not passed\"," +
						"\"violations\": [account-already-created]" +
						"}"));

	}


	@Test
	@Order(3)
	public void shouldReturnAccountWithOneTransaction() throws Exception {

		MvcResult mvcResult = executeAccountPost("Juan Perez", 1234567, "true", new BigInteger("20"), status().isOk());
		ObjectMapper mapper = new ObjectMapper();
		AccountDto accountDto = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), AccountDto.class);
		executeTransactionPost(accountDto.getId().toString(), "withdraw",  "red_link", "20", "2019-02-13T10:00:00.000Z", content().json("{" +
				"\"id\": " + accountDto.getId().toString() + "," +
				"\"name\": \"Juan Perez\"," +
				"\"dni\": 1234567," +
				"\"active_card\": true," +
				"\"available_amount\": 0," +
				"\"transactions\":[" +
					"{" +
					"\"type\":\"withdraw\"," +
					"\"commerce\":\"red_link\"," +
					"\"account_id\": " + accountDto.getId().toString() + "," +
					"\"amount\": 20," +
					"\"time\": \"2019-02-13T10:00:00.000Z\"" +
					"}]" +
				"}"));
	}

	@Test
	@Order(4)
	public void shouldReturnErrorList() throws Exception {

		MvcResult mvcResult = executeAccountPost("Juan Perez", 1234567, "true", new BigInteger("5900"), status().isOk());
		ObjectMapper mapper = new ObjectMapper();
		AccountDto accountDto = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), AccountDto.class);

		String violations = "account-already-created";
		executeAccountPost("Juan Perez", 1234567, "true", null, content().json("{\n" +
				"\"code\": 400,\n" +
				"\"message\":\"rules have not passed\",\n" +
				"\"violations\": ["+violations+"]\n" +
				"}"));


		for(int count = 0; count < 9; count++){

			executeTransactionPost(accountDto.getId().toString(), "deposit",  "red_link", "1" + count, getNowFormatted(), status().isOk());

		}

		//First transaction
		executeTransactionPost(accountDto.getId().toString(), "withdraw",  "red_link", "2499", getNowFormatted(), status().isOk());

		//second transaction
		executeTransactionPost(accountDto.getId().toString(), "withdraw",  "red_link", "2499", getNowFormatted(), status().isOk());

		violations = "insufficient-amount, high-frequency, doubled-transaction, allowed-amount-exceeds";
		//third transaction error dubled-transaction with same data in less than 2 minutes
		executeTransactionPost(accountDto.getId().toString(), "withdraw",  "red_link", "2499", getNowFormatted(), content().json("{\n" +
				"\"code\": 400,\n" +
				"\"message\":\"rules have not passed\",\n" +
				"\"violations\": [" + violations + "]\n" +
				"}"));

	}

	@Test()
	@Order(5)
	public void shouldReturnErrorAccountIdNotFound() throws Exception {

		executeTransactionPost("1", "withdraw",  "red_link", "20", "2019-02-13T10:00:00.000Z",
				content().json("{\n" +
						"\"code\": 500,\n" +
						"\"message\":\"account id not found\",\n" +
						"\"violations\": null\n" +
						"}"));
	}

	@Test()
	@Order(6)
	public void shouldReturnAccountWithTransaction() throws Exception {

		mockMvc.perform(
				post("/account")
						.content(
								"{" +
										"\"id\": 1," +
										"\"name\": \"Juan Perez\"," +
										"\"dni\": 1234567," +
										"\"active_card\": true," +
										"\"available_amount\": 0," +
										"\"transactions\":[" +
										"{" +
										"\"type\":\"withdraw\"," +
										"\"commerce\":\"red_link\"," +
										"\"account_id\": 1," +
										"\"amount\": 20," +
										"\"time\": \"2019-02-13T10:00:00.000Z\"" +
										"}]" +
										"}"
								)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test()
	@Order(7)
	public void shouldReturnMainApplication() throws Exception {
		String[] args = {"1","1"};
		NaranjaXChallengeApplication.main(args);
	}

	@Test
	@Order(8)
	public void shouldReturnErrorMaxDeposit() throws Exception {
		MvcResult mvcResult = executeAccountPost("Juan Perez", 1234567, "true", new BigInteger("10000"), status().isOk());
		ObjectMapper mapper = new ObjectMapper();
		AccountDto accountDto = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), AccountDto.class);

		//third transaction error dubled-transaction with same data in less than 2 minutes
		executeTransactionPost(accountDto.getId().toString(), "deposit",  "red_link", "10000", getNowFormatted(),
				status().isOk()
				);

		executeTransactionPost(accountDto.getId().toString(), "deposit",  "red_link", "10000", getNowFormatted(), content().json("{\n" +
				"\"code\": 400,\n" +
				"\"message\":\"rules have not passed\",\n" +
				"\"violations\": [allowed-amount-exceeds]\n" +
				"}"));
	}

	private String getNowFormatted() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		return simpleDateFormat.format(now);
	}

	private MvcResult executeAccountPost(String name, Integer dni, String activeCard, BigInteger availableAmount, ResultMatcher resultMatcher) throws Exception {

		if(availableAmount != null) {
			return mockMvc.perform(
					post("/account")
							.content("{\"name\":\"" + name + "\", \"dni\": " + dni + ", \"active_card\": " + activeCard + ", \"available_amount\": " + availableAmount + "}")
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(resultMatcher).andReturn();
		} else {
			return mockMvc.perform(
					post("/account")
							.content("{\"name\":\"" + name + "\", \"dni\": " + dni + ", \"active_card\": " + activeCard + "}")
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(resultMatcher).andReturn();
		}
	}

	private void executeTransactionPost(String accountId, String type, String commerce, String amount, String time, ResultMatcher resultMatcher) throws Exception {
		mockMvc.perform(
				post("/account/" + accountId + "/transaction")
						.content("{\"type\":\"" + type + "\", \"commerce\": \"" + commerce + "\",\"account_id\":" + accountId+ ", \"amount\": " + amount + ", \"time\": \"" + time + "\"}")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(resultMatcher);

	}


}
