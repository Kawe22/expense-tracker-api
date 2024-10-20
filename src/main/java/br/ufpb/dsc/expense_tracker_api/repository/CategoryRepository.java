package br.ufpb.dsc.expense_tracker_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpb.dsc.expense_tracker_api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public List<Category> findByUserId(Integer id);

    public Category findByIdAndUserId(Integer categoryId, Integer userId);

}
