package br.ufpb.dsc.expense_tracker_api.dto;

import javax.validation.constraints.NotBlank;

public class CategoryRequestDTO {

    @NotBlank(message = "O título da categoria não pode estar em branco")
    private String title;

    private String description;

    // Removido o NotNull, já que o userId será definido no Controller

    // Getters e Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
