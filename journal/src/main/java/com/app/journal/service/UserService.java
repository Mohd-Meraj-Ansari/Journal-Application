package com.app.journal.service;

import com.app.journal.entity.JournalEntry;
import com.app.journal.entity.User;
import com.app.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void saveNewUser(User user)
    {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void saveUser(User user)
    {
        userRepository.save(user);
    }


    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId getId) {
        return userRepository.findById(getId);
    }

    public void deleteById(ObjectId id)
    {
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName)
    {
        return userRepository.findByUserName(userName);
    }

    public void saveAdmin(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }
}
