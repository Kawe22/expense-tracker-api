package br.ufpb.dsc.expense_tracker_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpb.dsc.expense_tracker_api.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public List<Category> findByUserId(Integer id);

    // Alterar o retorno para Optional<Category>
    public Optional<Category> findByIdAndUserId(Integer categoryId, Integer userId);

}
