package br.ufpb.dsc.expense_tracker_api.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "O valor não pode ser nulo")
    @Positive(message = "O valor deve ser positivo")
    private Double amount;

    private String note;

    @NotNull(message = "A data da transação é obrigatória")
    private Long transactionDate;

    private boolean remind;

    @JsonIgnore
    @JoinColumn()
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @JoinColumn()
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    // Construtores
    public Transaction() {
    }

    public Transaction(Integer id, Double amount, String note, Long transactionDate, boolean remind) {
        this.id = id;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
        this.remind = remind;
    }

    public Transaction(Double amount, String note, Long transactionDate, boolean remind) {
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
        this.remind = remind;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
