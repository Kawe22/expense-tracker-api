package br.ufpb.dsc.expense_tracker_api.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufpb.dsc.expense_tracker_api.entity.Transaction;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.service.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "3. Transações") // Definindo a seção no Swagger
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @ApiOperation(value = "Lista todas as transações de uma categoria específica.")
    @GetMapping("/categories/{categoryId}/transactions")
    public List<Transaction> getAllTransactions(HttpServletRequest request,
                                                @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");
        return transactionService.fetchAllTransactions(userId, categoryId);
    }

    @ApiOperation(value = "Retorna os detalhes de uma transação específica.")
    @GetMapping("/categories/{categoryId}/transactions/{transactionId}")
    public Transaction getTransactionById(HttpServletRequest request,
                                          @PathVariable("categoryId") Integer categoryId,
                                          @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        return transactionService.fetchTransactionById(userId, categoryId, transactionId);
    }

    @ApiOperation(value = "Cria uma nova transação em uma categoria.")
    @PostMapping("/categories/{categoryId}/transactions")
    public Transaction addTransaction(HttpServletRequest request,
                                      @PathVariable("categoryId") Integer categoryId,
                                      @RequestBody Transaction transaction) {
        int userId = (Integer) request.getAttribute("userId");
        return transactionService.saveTransaction(transaction, categoryId, userId);
    }

    @ApiOperation(value = "Atualiza os dados de uma transação específica.")
    @PutMapping("/categories/{categoryId}/transactions/{transactionId}")
    public Transaction updateTransaction(HttpServletRequest request,
                                         @PathVariable("categoryId") Integer categoryId,
                                         @PathVariable("transactionId") Integer transactionId,
                                         @RequestBody Transaction transaction) {
        int userId = (Integer) request.getAttribute("userId");

        // Verificar se a transação existe
        Transaction existingTransaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        if (existingTransaction == null) {
            throw new EtResourceNotFoundException("Transaction not found");
        }

        // Atualizar a transação existente
        transaction.setId(transactionId);
        transaction.setUser(existingTransaction.getUser());
        transaction.setCategory(existingTransaction.getCategory());
        return transactionService.saveTransaction(transaction, categoryId, userId);
    }

    @ApiOperation(value = "Exclui uma transação do sistema.")
    @DeleteMapping("/categories/{categoryId}/transactions/{transactionId}")
    public String deleteTransaction(HttpServletRequest request,
                                    @PathVariable("categoryId") Integer categoryId,
                                    @PathVariable("transactionId") Integer transactionId) {
        int userId = (Integer) request.getAttribute("userId");
        transactionService.removeTransactionById(userId, categoryId, transactionId);
        return "Transação deletada com sucesso!";
    }
}
