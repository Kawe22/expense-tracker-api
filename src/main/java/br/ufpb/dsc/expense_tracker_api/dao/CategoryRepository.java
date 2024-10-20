package br.ufpb.dsc.expense_tracker_api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufpb.dsc.expense_tracker_api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public List<Category> findByUserId(Integer id);

    public Category findByIdAndUserId(Integer categoryId, Integer userId);

}
