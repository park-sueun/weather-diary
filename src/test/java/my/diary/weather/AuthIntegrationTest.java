package my.diary.weather;

import my.diary.weather.auth.dto.LoginRequest;
import my.diary.weather.auth.dto.SignupRequest;
import my.diary.weather.user.AppUser;
import my.diary.weather.user.AppUserRepository;
import my.diary.weather.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AppUserRepository userRepository;

    @Test
    void signup_success() throws Exception {

        var req = new SignupRequest(
                "user@test.com",
                "password123",
                "nick"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        assertThat(userRepository.existsByEmail("user@test.com")).isTrue();
    }

    @Test
    void login_success_returns_token() throws Exception {

        // given
        SignupRequest signup = new SignupRequest(
                "login@test.com",
                "password123",
                "nick"
        );

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)));

        LoginRequest login = new LoginRequest(
                "login@test.com",
                "password123"
        );

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void user_can_access_user_endpoint() throws Exception {

        String token = obtainAccessTokenByUser("user1@test.com");

        mockMvc.perform(get("/api/test/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void user_cannot_access_admin_endpoint() throws Exception {

        String token = obtainAccessTokenByUser("user2@test.com");

        mockMvc.perform(get("/api/test/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_can_access_admin_endpoint() throws Exception {

        String token = obtainAccessTokenByAdmin("admin@test.com");

        mockMvc.perform(get("/api/test/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String obtainAccessTokenByAdmin(String email) throws Exception {

        AppUser admin = AppUser.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode("admin123!"))
                .nickname("admin")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);

        // 2. login -> get access token
        return loginAndGetAccessToken(email, "admin123!");
    }

    private String obtainAccessTokenByUser(String email) throws Exception {

        // 1.signup
        SignupRequest signup = new SignupRequest(
                email,
                "password123!",
                "nick"
        );

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isCreated());

        // 2. login -> get access token
        return loginAndGetAccessToken(email, "password123!");
    }

    private String loginAndGetAccessToken(String email, String password) throws Exception {

        LoginRequest login = new LoginRequest(email, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        return objectMapper.readTree(body)
                .get("accessToken")
                .asText();
    }
}
