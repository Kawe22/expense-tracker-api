package br.ufpb.dsc.expense_tracker_api.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpb.dsc.expense_tracker_api.entity.Category;
import br.ufpb.dsc.expense_tracker_api.entity.Transaction;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.service.CategoryService;
import br.ufpb.dsc.expense_tracker_api.service.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "3. Transações") // Definindo a seção no Swagger
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

        // Verifica se a categoria pertence ao usuário autenticado
        Category category = categoryService.fetchCategoryById(categoryId, userId);
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta categoria.");
        }

        // Se a categoria pertencer ao usuário, buscar as transações
        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return ResponseEntity.ok(transactions);
    }

    @ApiOperation(value = "Retorna os detalhes de uma transação específica.")
    @GetMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> getTransactionById(HttpServletRequest request,
                                                @PathVariable("categoryId") Integer categoryId,
                                                @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se a categoria pertence ao usuário autenticado
        Category category = categoryService.fetchCategoryById(categoryId, userId);
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta transação.");
        }

        // Verifica se a transação existe na categoria
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
                                            @RequestBody Transaction transaction) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se a categoria pertence ao usuário autenticado
        Category category = categoryService.fetchCategoryById(categoryId, userId);
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para adicionar uma transação a esta categoria.");
        }

        // Criar a transação
        Transaction newTransaction = transactionService.saveTransaction(transaction, categoryId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @ApiOperation(value = "Atualiza os dados de uma transação específica.")
    @PutMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> updateTransaction(HttpServletRequest request,
                                               @PathVariable("categoryId") Integer categoryId,
                                               @PathVariable("transactionId") Integer transactionId,
                                               @RequestBody Transaction transaction) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se a categoria pertence ao usuário autenticado
        Category category = categoryService.fetchCategoryById(categoryId, userId);
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar uma transação nesta categoria.");
        }

        // Verifica se a transação existe
        Transaction existingTransaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        if (existingTransaction == null) {
            throw new EtResourceNotFoundException("Transação não encontrada");
        }

        // Atualizar a transação existente
        transaction.setId(transactionId);
        transaction.setUser(existingTransaction.getUser());
        transaction.setCategory(existingTransaction.getCategory());
        Transaction updatedTransaction = transactionService.saveTransaction(transaction, categoryId, userId);
        return ResponseEntity.ok(updatedTransaction);
    }

    @ApiOperation(value = "Exclui uma transação do sistema.")
    @DeleteMapping("/categories/{categoryId}/transactions/{transactionId}")
    public ResponseEntity<?> deleteTransaction(HttpServletRequest request,
                                               @PathVariable("categoryId") Integer categoryId,
                                               @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se a categoria pertence ao usuário autenticado
        Category category = categoryService.fetchCategoryById(categoryId, userId);
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir uma transação nesta categoria.");
        }

        // Excluir a transação
        transactionService.removeTransactionById(userId, categoryId, transactionId);
        return ResponseEntity.ok("Transação deletada com sucesso!");
    }
}
