package br.ufpb.dsc.expense_tracker_api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;
import br.ufpb.dsc.expense_tracker_api.model.Category;
import br.ufpb.dsc.expense_tracker_api.model.User;
import br.ufpb.dsc.expense_tracker_api.repository.CategoryRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1);

        category = new Category();
        category.setId(1);
        category.setTitle("Food");
        category.setDescription("All food-related expenses");
        category.setUser(user);
    }

    @Test
    public void testGetCategoryById() {
        // Simulando o retorno de um Optional<Category>
        when(categoryRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.of(category));

        Category fetchedCategory = categoryService.fetchCategoryById(1, 1);

        assertNotNull(fetchedCategory);
        assertEquals("Food", fetchedCategory.getTitle());
    }

    @Test
    public void testDeleteCategory() {
        // Simulando o retorno de um Optional<Category>
        when(categoryRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.of(category));

        categoryService.removeCategoryById(1, 1);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void testCategoryNotFoundException() {
        // Simulando que a categoria não foi encontrada
        when(categoryRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.empty());

        assertThrows(EtResourceNotFoundException.class, () -> {
            categoryService.fetchCategoryById(1, 1);
        });
    }

    @Test
    public void testCreateCategory() {
        // Simulando a criação de uma nova categoria (sem ID)
        category.setId(null);  // Definindo o ID como nulo para simular uma nova categoria
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.saveCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Food", createdCategory.getTitle());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testBadRequestException() {
        // Simulando uma tentativa de salvar uma categoria com título inválido
        category.setTitle("");  // Título vazio deve gerar exceção

        assertThrows(EtBadRequestException.class, () -> {
            categoryService.saveCategory(category);
        });

        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory() {
        // Simulando a atualização de uma categoria existente
        when(categoryRepository.findByIdAndUserId(1, 1)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        category.setTitle("Updated Title");
        Category updatedCategory = categoryService.saveCategory(category);

        assertNotNull(updatedCategory);
        assertEquals("Updated Title", updatedCategory.getTitle());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
