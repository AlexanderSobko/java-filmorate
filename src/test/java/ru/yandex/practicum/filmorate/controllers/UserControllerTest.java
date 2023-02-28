package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final String basePath = "http://localhost:8080/users";
    private final MockMvc mockMvc;

    @Autowired
    UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get(basePath)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controllers/user-test-data/successfullySaveUser.csv", delimiter = '|')
    public void successfullySaveUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(post(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controllers/user-test-data/failSaveUser.csv", delimiter = '|')
    public void failSaveUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(post(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/controllers/user-test-data/successfullyUpdateUser.csv", delimiter = '|')
    void successfullyUpdateUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(post(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
        mockMvc.perform(put(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString(expectedResponse)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/controllers/user-test-data/failUpdateUser.csv", delimiter = '|')
    void failUpdateUser(String fileJson, String expectedResponse) throws Exception {
        mockMvc.perform(put(basePath)
                        .content(fileJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}