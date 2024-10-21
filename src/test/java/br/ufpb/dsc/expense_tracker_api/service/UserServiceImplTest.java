package br.ufpb.dsc.expense_tracker_api.service;

import br.ufpb.dsc.expense_tracker_api.model.User;
import br.ufpb.dsc.expense_tracker_api.repository.UserRepository;
import br.ufpb.dsc.expense_tracker_api.exception.EtAuthException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.registerUser(user);

        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1);

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUser() {
        // Simulando que o usuário existe no repositório
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Atualizando o e-mail do usuário
        user.setEmail("updated@example.com");
        User updatedUser = userService.updateUser(user.getId(), user);

        // Verificando se o e-mail foi atualizado corretamente
        assertEquals("updated@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testDeleteUser() {
        // Simulando que o usuário existe no repositório
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Executando a deleção do usuário
        userService.deleteUser(user.getId());

        // Verificando se o método de deletar foi chamado
        verify(userRepository, times(1)).delete(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testRegisterUser_EmailAlreadyInUse() {
        // Simulando que o e-mail já está em uso
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        assertThrows(EtAuthException.class, () -> {
            userService.registerUser(user);
        });

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testRegisterUser_InvalidEmailFormat() {
        // Definindo um e-mail inválido
        user.setEmail("invalid-email");

        assertThrows(EtAuthException.class, () -> {
            userService.registerUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
