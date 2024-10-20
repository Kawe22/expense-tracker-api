package br.ufpb.dsc.expense_tracker_api.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserUpdateRequestDTO {

    @NotBlank(message = "O primeiro nome não pode estar em branco")
    private String firstName;

    @NotBlank(message = "O sobrenome não pode estar em branco")
    private String lastName;

    @Email(message = "O e-mail deve ser válido")
    @NotBlank(message = "O e-mail não pode estar em branco")
    private String email;

    // Getters and Setters

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
}
