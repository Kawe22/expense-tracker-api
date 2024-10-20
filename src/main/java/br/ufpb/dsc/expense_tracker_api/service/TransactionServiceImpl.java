package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpb.dsc.expense_tracker_api.dao.TransactionRepository;
import br.ufpb.dsc.expense_tracker_api.dao.CategoryRepository;
import br.ufpb.dsc.expense_tracker_api.dao.UserRepository;
import br.ufpb.dsc.expense_tracker_api.entity.Category;
import br.ufpb.dsc.expense_tracker_api.entity.Transaction;
import br.ufpb.dsc.expense_tracker_api.entity.User;
import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  CategoryRepository categoryRepository,
                                  UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        try {
            return transactionRepository.findByUserIdAndCategoryIdAndId(userId, categoryId, transactionId);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Resource not found");
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction, Integer categoryId, Integer userId)
            throws EtBadRequestException {
        try {
            // Recupera a categoria e o usuário do banco de dados
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EtResourceNotFoundException("Category not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EtResourceNotFoundException("User not found"));

            // Vincula a categoria e o usuário à transação
            transaction.setCategory(category);
            transaction.setUser(user);

            // Salva a transação no banco de dados
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request", e);
        }
    }

    @Override
    public void removeTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        try {
            Transaction transaction = transactionRepository.findByUserIdAndCategoryIdAndId(userId, categoryId, transactionId);
            transactionRepository.delete(transaction);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Resource not found");
        }
    }
}
