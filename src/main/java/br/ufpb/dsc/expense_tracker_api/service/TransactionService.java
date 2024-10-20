package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;
import br.ufpb.dsc.expense_tracker_api.entity.Transaction;
import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

public interface TransactionService {

    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;

    public Transaction saveTransaction(Transaction transaction, Integer categoryId, Integer userId) throws EtBadRequestException;

    public void removeTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
}
