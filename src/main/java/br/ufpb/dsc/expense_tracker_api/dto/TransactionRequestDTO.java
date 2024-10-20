package br.ufpb.dsc.expense_tracker_api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TransactionRequestDTO {

    @NotNull(message = "O valor da transação não pode ser nulo")
    @Positive(message = "O valor da transação deve ser positivo")
    private Double amount;

    private String note;

    @NotNull(message = "A data da transação não pode ser nula")
    private Long transactionDate;

    private boolean remind;

    // Getters e Setters
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
}
