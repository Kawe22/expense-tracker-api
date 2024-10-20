package br.ufpb.dsc.expense_tracker_api.service;

import java.util.List;

import br.ufpb.dsc.expense_tracker_api.entity.User;
import br.ufpb.dsc.expense_tracker_api.exception.EtAuthException;

public interface UserService {

    public User validateUser(String email, String password) throws EtAuthException;

    public User registerUser(User user) throws EtAuthException;

    public User getUserById(Integer id) throws EtAuthException;

    public User updateUser(Integer id, User userDetails);

    public void deleteUser(Integer id);
}
