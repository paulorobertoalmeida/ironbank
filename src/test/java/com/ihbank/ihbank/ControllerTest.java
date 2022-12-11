package com.ihbank.ihbank;

import com.ihbank.ihbank.dto.AccountHolderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihbank.ihbank.dto.AccountDTO;
import com.ihbank.ihbank.models.accounts.Account;
import com.ihbank.ihbank.models.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
/*
    User user;
    Account account;

        GsonBuilder gsonBuilder;
            {
                gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            }

            Gson gson = gsonBuilder.setPrettyPrinting().create();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createAccount_test() throws Exception {
        AccountDTO account = new AccountDTO(new BigDecimal("2000"), "secretkey", "checking");
        String body = gson.toJson(account);
        System.out.println(body);
        MvcResult mvcResult = mockMvc.perform(post("/create-account-one-holder?holderId=1")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("paulo1"));
    }

    @Test
    void createAccount_2_HOLDERS_test() throws Exception {
        AccountDTO account = new AccountDTO(new BigDecimal("2000"), "secretkey", "checking");
        String body = gson.toJson(account);
        System.out.println(body);
        MvcResult mvcResult = mockMvc.perform(post("/create-account-two-holders?holderId=1&secondaryHolderId=2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("secondary"));
    }
*/
    @Test
    void get_all_accounts_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/all-accounts"))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("paulo"));
    }

    @Test
    void get_admin_check_balance_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin-check-balance?id=2"))
                .andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("500"));
    }

    @Test
    void patch_admin_change_balance_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch("/admin-change-balance?accountId=3&newBalance=2000"))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("2000"));
    }


    @Test
    void delete_account_test() throws Exception {
        MvcResult mvcResult1 = mockMvc.perform(delete("/delete-account/4"))
                .andExpect(status().isAccepted()).andReturn();
        MvcResult mvcResult2 = mockMvc.perform(delete("/delete-account/4"))
                .andExpect(status().isNotFound()).andReturn();

        assertTrue(mvcResult2.getResolvedException().getMessage().contains("The account does not exist"));
    }



    @Test
    void put_transfer_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/transfer?senderName=Sergio&senderId=12&amount=5&receiveId=3"))
                .andExpect(status().isOk()).andReturn();
        System.out.println("RESULT  " + mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("transferred") );
    }


    @Test
    @WithUserDetails("paulo")
    void get_my_balance_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/my-balance?accountId=1"))
                .andExpect(status().isOk()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("2008"));
    }


    @Test
    @WithUserDetails("paulo")
    void put_new_transfer_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/new-transfer?sendingId=12&amount=50&receiveId=2"))
                .andExpect(status().isOk()).andReturn();
        System.out.println("RESULT  " + mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("transferred") );
    }



    @Test
    void put_third_party_transfer_test() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/third-party-transfer?sendingAccountId=2&amount=50&receiveAccountId=3&secretKeySendingAccount=pepepe888")
                        .header("hashedKey", "hashed"))
                .andExpect(status().isOk()).andReturn();
        System.out.println("RESULT  " + mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("transferred") );
    }



/*
    @Test
    @WithUserDetails("admin1111")
    void createHolder_test() throws Exception {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("test88", "password", "testeo", LocalDate.of(2000, 3, 29), "calle", 8, 8015, "barcelona", "espania", "calle correo", 88, 8025, "ciudad correo", "pais correo");
        String body = gson.toJson(accountHolderDTO);
        System.out.println(body);
        MvcResult mvcResult = mockMvc.perform(post("/create-holder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("testeo"));
    }

*/

}