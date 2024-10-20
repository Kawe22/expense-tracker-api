package br.ufpb.dsc.expense_tracker_api.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O título não pode estar em branco")
    @Size(min = 3, message = "O título deve ter no mínimo 3 caracteres")
    private String title;

    @NotBlank(message = "A descrição não pode estar em branco")
    private String description;

    @JsonIgnore
    @JoinColumn()
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    // Construtores
    public Category() {
    }

    public Category(Integer id, String title, String description, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Category(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
