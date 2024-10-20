package br.ufpb.dsc.expense_tracker_api.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufpb.dsc.expense_tracker_api.dao.UserRepository;
import br.ufpb.dsc.expense_tracker_api.entity.User;
import br.ufpb.dsc.expense_tracker_api.exception.EtAuthException;
import br.ufpb.dsc.expense_tracker_api.exception.EtResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if (email == null || password == null) throw new EtAuthException("Invalid credentials");

        email = email.toLowerCase();
        User user = userRepository.findByEmail(email);

        if (user == null) throw new EtAuthException("Invalid email address");
        if (!passwordEncoder.matches(password, user.getPassword())) throw new EtAuthException("Incorrect password");

        return user;
    }

    @Override
    public User registerUser(User user) throws EtAuthException {
        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null)
            throw new EtAuthException("Fill all the details");

        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty())
            throw new EtAuthException("Fill all the details");

        // Validating email
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        user.setEmail(user.getEmail().toLowerCase());
        if (!pattern.matcher(user.getEmail()).matches()) throw new EtAuthException("Invalid email format");

        // Check if email is already in use
        if (userRepository.findByEmail(user.getEmail()) != null) throw new EtAuthException("Email already in use");

        // Encoding password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) throws EtAuthException {
        return userRepository.findById(id).orElseThrow(() -> new EtAuthException("User doesn't exist"));
    }

    @Override
    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new EtResourceNotFoundException("User not found"));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        // Atualize mais campos conforme necessário

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EtResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
