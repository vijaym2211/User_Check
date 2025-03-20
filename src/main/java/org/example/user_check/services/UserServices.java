package org.example.user_check.services;


import org.example.user_check.Exception.UserAlreadyPresent;
import org.example.user_check.model.User;
import org.example.user_check.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    private UserRepository userRepository;
    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password) throws UserAlreadyPresent {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyPresent("User preset already");
        }
        User dbuser = new User();
        dbuser.setEmail(email);
        dbuser.setName(name);
        dbuser.setPassword(bcryptPasswordEncoder.encode(password));
        dbuser.setRoles(Arrays.asList("ROLE_USER"));
        userRepository.save(dbuser);
        return dbuser;
    }

    public Page<User> getAllList(int pageSize, int pageNum){
        Page<User> users = userRepository.findAll(PageRequest.of(pageNum,pageSize));
        return users;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(String email) throws UserAlreadyPresent {
        Optional<User> dbuser = userRepository.findByEmail(email);
        User user = new User();
        user.setEmail(email);
        user.setName(dbuser.get().getName());
        user.setPassword(dbuser.get().getPassword());
        return userRepository.save(user);
    }
}
