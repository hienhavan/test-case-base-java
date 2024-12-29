package org.example.demomogodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.demomogodb.user.User;
import org.example.demomogodb.user.UserRepository;
import org.example.demomogodb.user.UserReq;
import org.example.demomogodb.user.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();
    private UserReq userRequest;
    private String userReq;
    private User userRes;

    private void mockSaveUserResponse(User userRes) {
        when(userService.saveUser(ArgumentMatchers.any())).thenReturn(userRes);
    }

    private void performPostRequest(String content, int expectedStatus, String expectedJsonPath, String expectedJsonValue) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus))
                .andExpect(MockMvcResultMatchers.jsonPath(expectedJsonPath).value(expectedJsonValue));
    }

    private void mockUpdateUserResponse(User res) {
        when(userService.update(ArgumentMatchers.any())).thenReturn(res);
    }

    private void performPutRequest(String req, int expectedStatus, String expectedJsonPath, String expectedJsonValue) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus))
                .andExpect(MockMvcResultMatchers.jsonPath(expectedJsonPath).value(expectedJsonValue));
    }

    private void performDeleteRequest(String idUser, int expectedStatus) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/delete/{idUser}", idUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus));
    }

    @Test
    @Order(1)
    void createUser_validJsonA() throws Exception {
        userReq = "{\"name\": \"TestUser\"}";
        userRes = User.builder()
                .name("TestUser")
                .build();
        mockSaveUserResponse(userRes);
        performPostRequest(userReq, 200, "name", "TestUser");
    }

    @Test
    @Order(2)
    void createUser_validJsonB() throws Exception {
        userReq = "{\"name\": \"Test User\"}";
        userRes = User.builder()
                .name("Test User")
                .build();
        String invalidUserRequest = "{\"name\": \"Test User\"}";
        mockSaveUserResponse(userRes);
        performPostRequest(invalidUserRequest, 200, "name", "Test User");
    }

    @Test
    @Order(3)
    void createUser_missingName() throws Exception {
        String invalidUserRequest = "{}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(invalidUserRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Invalid request: 400 BAD_REQUEST")));
    }

    @Test
    @Order(4)
    void createUser_invalidJson() throws Exception {
        String invalidUserRequest = "{\"name\": \"123123\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(invalidUserRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Invalid request: 400 BAD_REQUEST \"Invalid JSON format or schema\"")));
    }

    @Test
    @Order(5)
    void updateUser_validJson() throws Exception {
        userRequest = UserReq.builder()
                .id("123123a")
                .name("TestUser")
                .build();
        userRes = User.builder()
                .id("123123a")
                .name("TestUser")
                .build();

        mockUpdateUserResponse(userRes);
        String userReqJson = mapper.writeValueAsString(userRequest);
        performPutRequest(userReqJson, 200, "id", "123123a");
    }

    @Test
    @Order(6)
    void updateUser_missingId() throws Exception {
        userReq = "{\"name\": \"Test User\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userReq))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Invalid request: 400 BAD_REQUEST \"Missing or invalid user ID\"")));
    }

    @Test
    @Order(7)
    void updateUser_nameIsNull() throws Exception {
        userRequest = UserReq.builder()
                .id("123123a")
                .name(null)
                .build();
        userRes = User.builder()
                .id("123123a")
                .name("TestUser")
                .build();

        mockUpdateUserResponse(userRes);
        String userReqJson = mapper.writeValueAsString(userRequest);
        performPutRequest(userReqJson, 200, "id", "123123a");
    }

    @Test
    @Order(8)
    void deleteUser_validId() throws Exception {
        String userId = "123123a";
        performDeleteRequest(userId, 204);
    }

    @Test
    @Order(9)
    void deleteUser_missingId() throws Exception {
        performDeleteRequest(null, 500);
    }
}