package br.ufpb.dsc.expense_tracker_api.controller;

import br.ufpb.dsc.expense_tracker_api.dto.LoginRequestDTO;
import br.ufpb.dsc.expense_tracker_api.dto.RegisterRequestDTO;
import br.ufpb.dsc.expense_tracker_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private String validToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Simule um token válido
        validToken = "Bearer <seu_token_aqui>";
    }

    @Test
    public void testLoginUser() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password");

        // Simule o serviço de login retornando um token
        when(userService.login(any(LoginRequestDTO.class))).thenReturn(validToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testRegisterUser() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password");

        // Simule o serviço de registro do usuário
        when(userService.register(any(RegisterRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser // Adicione um usuário simulado para este teste
    public void testDeleteUser() throws Exception {
        mockMvc.perform(post("/api/users/1")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // Espera um status 204
                .andDo(print());
    }

    @Test
    @WithMockUser // Adicione um usuário simulado para este teste
    public void testGetUserById() throws Exception {
        mockMvc.perform(post("/api/users/1")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera um status 200
                .andDo(print());
    }
}
