package br.ufpb.dsc.expense_tracker_api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpb.dsc.expense_tracker_api.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}
