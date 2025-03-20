package org.example.user_check.services;

import org.example.user_check.model.User;
import org.example.user_check.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail((username));

        System.out.println("loadUsermethod called ************************" + user.isPresent());


        if(user.isPresent()){
            System.out.println("Print user Name **********" + user.get().getName());
            System.out.println("Print password ************" + user.get().getPassword());
            System.out.println(" password Match***********" + bCryptPasswordEncoder.matches("fuku5460" ,user.get().getPassword()));

           UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                   .username(user.get().getEmail())
                   .password(user.get().getPassword())
                   .roles(user.get().getRoles().toArray(new String[0]))
                   .build();
           return userDetails;
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
//        return null;
    }
}
