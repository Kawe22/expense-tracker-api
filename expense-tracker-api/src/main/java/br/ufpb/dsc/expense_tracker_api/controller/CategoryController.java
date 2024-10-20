package br.ufpb.dsc.expense_tracker_api.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufpb.dsc.expense_tracker_api.entity.Category;
import br.ufpb.dsc.expense_tracker_api.entity.Transaction;
import br.ufpb.dsc.expense_tracker_api.entity.User;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.service.CategoryService;
import br.ufpb.dsc.expense_tracker_api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@Api(tags = "2. Categorias") // Definindo a seção no Swagger
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "Retorna a lista de todas as categorias do usuário autenticado.")
    @GetMapping("/categories")
    public List<Category> getCategoriesOfUser(HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");
        return categoryService.fetchCategoriesOfUser(userId);
    }

    @ApiOperation(value = "Retorna os detalhes de uma categoria específica.")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");

        // Buscar a categoria
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        // Verificar se a categoria pertence ao usuário autenticado
        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta categoria.");
        }

        return ResponseEntity.ok(category);
    }

    @ApiOperation(value = "Cria uma nova categoria.")
    @PostMapping("/categories")
    public Category addCategory(HttpServletRequest request, @RequestBody Category category) {
        int userId = (Integer) request.getAttribute("userId");
        User user = userService.getUserById(userId);
        category.setUser(user);
        return categoryService.saveCategory(category);
    }

    @ApiOperation(value = "Atualiza os dados de uma categoria específica.")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(HttpServletRequest request,
                                            @PathVariable("categoryId") Integer categoryId,
                                            @RequestBody Category categoryUpdate) {
        int userId = (Integer) request.getAttribute("userId");

        // Recuperar a categoria existente do banco de dados
        Category existingCategory = categoryService.fetchCategoryById(categoryId, userId);

        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
        }

        if (!existingCategory.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para modificar esta categoria.");
        }

        // Atualizar os campos da categoria
        existingCategory.setTitle(categoryUpdate.getTitle());
        existingCategory.setDescription(categoryUpdate.getDescription());

        // Reatribuir as transações
        existingCategory.setTransactions(existingCategory.getTransactions());

        // Salvar a categoria atualizada
        categoryService.saveCategory(existingCategory);

        return ResponseEntity.ok(existingCategory);
    }

    @ApiOperation(value = "Exclui uma categoria do sistema.")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");

        // Recuperar a categoria existente do banco de dados
        Category existingCategory = categoryService.fetchCategoryById(categoryId, userId);

        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
        }

        if (!existingCategory.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir esta categoria.");
        }

        categoryService.removeCategoryById(categoryId, userId);
        return ResponseEntity.ok("Categoria deletada com sucesso!");
    }
}
