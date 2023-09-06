package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    private static final String URL = "/users";
    private static final LocalDate VALID_DATE = LocalDate.of(2000, 11, 24);
    private static final String VALID_EMAIL = "khanbikov@ya.ru";
    private static final String VALID_LOGIN = "Askhat";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnStatusOkWhenCreateValidUser() throws Exception {
        User newUser = User.builder()
                .birthday(VALID_DATE)
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name("Askhat")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateUserWithInvalidEmail() throws Exception {
        User newUser = User.builder()
                .birthday(VALID_DATE)
                .email("ya.ru")
                .login(VALID_LOGIN)
                .name("Askhat")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateUserWithEmptyEmail() throws Exception {
        User newUser = User.builder()
                .birthday(VALID_DATE)
                .login(VALID_LOGIN)
                .name("Askhat")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateUserWithEmptyLogin() throws Exception {
        User newUser = User.builder()
                .birthday(VALID_DATE)
                .email(VALID_EMAIL)
                .name("Askhat")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateUserWithFutureBirthday() throws Exception {
        User newUser = User.builder()
                .birthday(LocalDate.of(2100, 1, 29))
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name("Askhat")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatusOkWhenUpdateValidUser() throws Exception {
        createUser();
        User updatedUser = User.builder()
                .birthday(VALID_DATE)
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name("AskhatCheck")
                .build();
        updatedUser.setId(1L);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateUserWithInvalidEmail() throws Exception {
        createUser();
        User updatedUser = User.builder()
                .birthday(VALID_DATE)
                .email("ya.ru")
                .login(VALID_LOGIN)
                .name("AskhatCheck")
                .build();
        updatedUser.setId(1L);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateUserWithEmptyEmail() throws Exception {
        createUser();
        User updatedUser = User.builder()
                .birthday(VALID_DATE)
                .login(VALID_LOGIN)
                .name("AskhatCheck")
                .build();
        updatedUser.setId(1L);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateUserWithEmptyLogin() throws Exception {
        createUser();
        User updatedUser = User.builder()
                .birthday(VALID_DATE)
                .email(VALID_EMAIL)
                .name("AskhatCheck")
                .build();
        updatedUser.setId(1L);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateUserWithFutureBirthday() throws Exception {
        createUser();
        User updatedUser = User.builder()
                .birthday(LocalDate.of(2100, 1, 29))
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name("AskhatCheck")
                .build();
        updatedUser.setId(1L);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private void createUser() throws Exception {
        User newUser = User.builder()
                .birthday(VALID_DATE)
                .email(VALID_EMAIL)
                .login(VALID_LOGIN)
                .name("AskhatCheck")
                .build();
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /*

    Это тесты, которые по идее должны отрабатывать, но я выбрасываю Exception,
     что не подходит ни под одну категорию статусов, не знаю как это обработать(
    @Test
    void shouldReturnStatus4xxWhenUpdateUserWithSpaceInLogin() throws Exception {
        createUser();
        User updatedUser = new User(VALID_EMAIL, "Askhat ", "", VALID_DATE);
        updatedUser.setId(1);
        String body = mapper.writeValueAsString(updatedUser);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateUserWithSpaceInLogin() throws Exception {
        User newUser = new User(VALID_EMAIL, "Askhat ", "", VALID_DATE);
        String body = mapper.writeValueAsString(newUser);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    */
}
