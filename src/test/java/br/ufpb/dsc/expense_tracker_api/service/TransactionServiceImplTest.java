package br.ufpb.dsc.expense_tracker_api.service;

import br.ufpb.dsc.expense_tracker_api.dto.TransactionRequestDTO;
import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.model.Category;
import br.ufpb.dsc.expense_tracker_api.model.Transaction;
import br.ufpb.dsc.expense_tracker_api.model.User;
import br.ufpb.dsc.expense_tracker_api.repository.CategoryRepository;
import br.ufpb.dsc.expense_tracker_api.repository.TransactionRepository;
import br.ufpb.dsc.expense_tracker_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionRequestDTO transactionDTO;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setId(1);
        transaction.setAmount(200.0);
        transaction.setNote("Test transaction");
        transaction.setTransactionDate(System.currentTimeMillis());
        transaction.setRemind(false);

        transactionDTO = new TransactionRequestDTO();
        transactionDTO.setAmount(200.0);
        transactionDTO.setNote("Updated transaction");
        transactionDTO.setTransactionDate(System.currentTimeMillis());
        transactionDTO.setRemind(false);
    }

    @Test
    public void testUpdateTransaction() {
        // Criação do DTO para simular a atualização
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setAmount(200.0);
        dto.setNote("Updated transaction");
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setRemind(false);

        // Chamar o método updateTransaction
        transaction.setAmount(dto.getAmount());
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction updatedTransaction = transactionService.updateTransaction(dto, transaction);

        // Verificar se o retorno da atualização está correto
        assertNotNull(updatedTransaction);
        assertEquals(200.0, updatedTransaction.getAmount());
        assertEquals("Updated transaction", updatedTransaction.getNote());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testFetchTransactionById() {
        when(transactionRepository.findByUserIdAndCategoryIdAndId(1, 1, 1))
                .thenReturn(transaction); // Ajustado para retornar o transaction diretamente

        Transaction fetchedTransaction = transactionService.fetchTransactionById(1, 1, 1);

        assertNotNull(fetchedTransaction);
        assertEquals(transaction.getAmount(), fetchedTransaction.getAmount());
        verify(transactionRepository, times(1)).findByUserIdAndCategoryIdAndId(1, 1, 1);
    }

    @Test
    public void testDeleteTransaction() {
        when(transactionRepository.findByUserIdAndCategoryIdAndId(1, 1, 1))
                .thenReturn(transaction); // Ajustado para retornar o transaction diretamente

        transactionService.removeTransactionById(1, 1, 1);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    public void testSaveTransaction() {
        Category category = new Category();
        category.setId(1);

        User user = new User();
        user.setId(1);

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transactionDTO, 1, 1);

        assertNotNull(savedTransaction);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testTransactionNotFoundException() {
        when(transactionRepository.findByUserIdAndCategoryIdAndId(1, 1, 999))
                .thenThrow(new EtResourceNotFoundException("Transaction not found"));

        assertThrows(EtResourceNotFoundException.class, () -> {
            transactionService.fetchTransactionById(1, 1, 999);
        });
    }
}
