package br.ufpb.dsc.expense_tracker_api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterRequestDTO {

    @NotBlank(message = "O primeiro nome não pode estar em branco")
    private String firstName;

    @NotBlank(message = "O sobrenome não pode estar em branco")
    private String lastName;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Por favor, forneça um email válido")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    private String password;

    // Getters e Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
