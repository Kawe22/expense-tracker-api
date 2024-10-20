package br.ufpb.dsc.expense_tracker_api.controller;

import br.ufpb.dsc.expense_tracker_api.dto.TransactionRequestDTO;
import br.ufpb.dsc.expense_tracker_api.model.Category;
import br.ufpb.dsc.expense_tracker_api.model.Transaction;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.service.CategoryService;
import br.ufpb.dsc.expense_tracker_api.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "3. Transações")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "Lista todas as transações de uma categoria específica.")
    @GetMapping("/categories/{categoryId}/transactions")
    public ResponseEntity<?> getAllTransactions(HttpServletRequest request,
                                                @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta categoria.");
        }

        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return ResponseEntity.ok(transactions);
    }

    @ApiOperation(value = "Retorna os detalhes de uma transação específica.")
    @GetMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> getTransactionById(HttpServletRequest request,
                                                @PathVariable("categoryId") Integer categoryId,
                                                @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta transação.");
        }

        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        if (transaction == null) {
            throw new EtResourceNotFoundException("Transação não encontrada na categoria especificada.");
        }

        return ResponseEntity.ok(transaction);
    }

    @ApiOperation(value = "Cria uma nova transação em uma categoria.")
    @PostMapping("/categories/{categoryId}/transactions")
    public ResponseEntity<?> addTransaction(HttpServletRequest request,
                                            @PathVariable("categoryId") Integer categoryId,
                                            @Valid @RequestBody TransactionRequestDTO transactionDTO) {
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para adicionar uma transação a esta categoria.");
        }

        Transaction newTransaction = transactionService.saveTransaction(transactionDTO, categoryId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @ApiOperation(value = "Atualiza os dados de uma transação específica.")
    @PutMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> updateTransaction(HttpServletRequest request,
                                               @PathVariable("categoryId") Integer categoryId,
                                               @PathVariable("transactionId") Integer transactionId,
                                               @Valid @RequestBody TransactionRequestDTO transactionDTO) {
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar uma transação nesta categoria.");
        }

        Transaction existingTransaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        if (existingTransaction == null) {
            throw new EtResourceNotFoundException("Transação não encontrada");
        }

        Transaction updatedTransaction = transactionService.updateTransaction(transactionDTO, existingTransaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @ApiOperation(value = "Exclui uma transação do sistema.")
    @DeleteMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> deleteTransaction(HttpServletRequest request,
                                               @PathVariable("categoryId") Integer categoryId,
                                               @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir uma transação nesta categoria.");
        }

        transactionService.removeTransactionById(userId, categoryId, transactionId);
        return ResponseEntity.ok("Transação deletada com sucesso!");
    }
}
