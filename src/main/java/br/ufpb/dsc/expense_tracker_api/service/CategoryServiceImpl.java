package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpb.dsc.expense_tracker_api.repository.CategoryRepository;
import br.ufpb.dsc.expense_tracker_api.model.Category;
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
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new EtResourceNotFoundException("Resource not found"));
    }

    @Override
    public Category saveCategory(Category category) throws EtBadRequestException {
        try {
            if (category.getId() != null) {
                Category existingCategory = categoryRepository.findByIdAndUserId(category.getId(), category.getUser().getId())
                        .orElseThrow(() -> new EtResourceNotFoundException("Category not found."));
            }

            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request", e);
        }
    }

    @Override
    public void removeCategoryById(Integer categoryId, Integer userId) throws EtResourceNotFoundException {
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new EtResourceNotFoundException("Resource not found"));
        categoryRepository.delete(category);
    }
}
