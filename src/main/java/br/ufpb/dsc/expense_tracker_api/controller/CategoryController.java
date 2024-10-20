package br.ufpb.dsc.expense_tracker_api.controller;

import br.ufpb.dsc.expense_tracker_api.dto.CategoryRequestDTO;
import br.ufpb.dsc.expense_tracker_api.model.Category;
import br.ufpb.dsc.expense_tracker_api.model.User;
import br.ufpb.dsc.expense_tracker_api.service.CategoryService;
import br.ufpb.dsc.expense_tracker_api.service.UserService;
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
@Api(tags = "2. Categorias")
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
        Category category = categoryService.fetchCategoryById(categoryId, userId);

        if (category == null || !category.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar esta categoria.");
        }

        return ResponseEntity.ok(category);
    }

    @ApiOperation(value = "Cria uma nova categoria.")
    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(HttpServletRequest request, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        int userId = (Integer) request.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        Category category = new Category();
        category.setTitle(categoryRequestDTO.getTitle());
        category.setDescription(categoryRequestDTO.getDescription());
        category.setUser(user); // Define o usuário da categoria com base no token JWT

        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @ApiOperation(value = "Atualiza os dados de uma categoria específica.")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(HttpServletRequest request,
                                            @PathVariable("categoryId") Integer categoryId,
                                            @Valid @RequestBody CategoryRequestDTO categoryUpdateDTO) {
        int userId = (Integer) request.getAttribute("userId");

        Category existingCategory = categoryService.fetchCategoryById(categoryId, userId);
        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada.");
        }

        if (!existingCategory.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para modificar esta categoria.");
        }

        existingCategory.setTitle(categoryUpdateDTO.getTitle());
        existingCategory.setDescription(categoryUpdateDTO.getDescription());

        categoryService.saveCategory(existingCategory);

        return ResponseEntity.ok(existingCategory);
    }

    @ApiOperation(value = "Exclui uma categoria do sistema.")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");
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
