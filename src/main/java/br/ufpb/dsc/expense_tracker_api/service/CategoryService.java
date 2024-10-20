package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;

import br.ufpb.dsc.expense_tracker_api.model.Category;
import br.ufpb.dsc.expense_tracker_api.exception.EtBadRequestException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

public interface CategoryService {

    public List<Category> fetchCategoriesOfUser(Integer userId);

    public Category fetchCategoryById(Integer categoryId, Integer userId) throws EtResourceNotFoundException;

    public Category saveCategory(Category category) throws EtBadRequestException;

    public void removeCategoryById(Integer categoryId, Integer userId) throws EtResourceNotFoundException;
}
