package br.ufpb.dsc.expense_tracker_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpb.dsc.expense_tracker_api.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    public List<Transaction> findByUserIdAndCategoryId(Integer userId, Integer categoryId);

    public Transaction findByUserIdAndCategoryIdAndId(Integer userId, Integer categoryId, Integer transactionId);
}
