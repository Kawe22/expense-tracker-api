package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpb.dsc.expense_tracker_api.dao.CategoryRepository;
import br.ufpb.dsc.expense_tracker_api.entity.Category;
import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchCategoriesOfUser(Integer userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public Category fetchCategoryById(Integer categoryId, Integer userId) throws EtResourceNotFoundException {
        try {
            return categoryRepository.findByIdAndUserId(categoryId, userId);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Resource not found");
        }
    }

    @Override
    public Category saveCategory(Category category) throws EtBadRequestException {
        try {
            // Verifica se a categoria já existe antes de salvar
            if (category.getId() != null) {
                Category existingCategory = categoryRepository.findByIdAndUserId(category.getId(), category.getUser().getId());
                if (existingCategory == null) {
                    throw new EtResourceNotFoundException("Category not found.");
                }
            }

            // Salva ou atualiza a categoria
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request", e);
        }
    }

    @Override
    public void removeCategoryById(Integer categoryId, Integer userId) throws EtResourceNotFoundException {
        try {
            Category category = categoryRepository.findByIdAndUserId(categoryId, userId);
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Resource not found");
        }
    }
}
