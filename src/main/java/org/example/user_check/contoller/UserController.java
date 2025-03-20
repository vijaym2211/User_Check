package org.example.user_check.contoller;


import org.example.user_check.Exception.UserAlreadyPresent;
import org.example.user_check.dto.UserRequestDto;
import org.example.user_check.dto.UserResponseDto;
import org.example.user_check.model.User;
import org.example.user_check.repository.UserRepository;
import org.example.user_check.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//@Controller
@RestController
@RequestMapping("/userapi")
public class UserController {

    private final UserRepository userRepository;
    private UserServices userServices;

    @Autowired
    public UserController(UserServices userServices, UserRepository userRepository) {
        this.userServices = userServices;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) throws UserAlreadyPresent {
        try {
            User user = userServices.registerUser(userRequestDto.getName(),
                    userRequestDto.getEmail(), userRequestDto.getPassword());
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setName(user.getName());
            userResponseDto.setEmail(user.getEmail());
            return ResponseEntity.ok(userResponseDto);
        }
        catch(UserAlreadyPresent e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("wrong");
        }
    }

    @GetMapping("/getAllList")
    public ResponseEntity<Page<User>> getAllList(
            @RequestParam(value= "pageSize", defaultValue = "2") int pageSize,
            @RequestParam(value= "pageNum", defaultValue = "0") int pageNum)
    {
        Page<User> users = userServices.getAllList(pageSize, pageNum);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userServices.getAllUsers();
    }


//    @GetMapping("/updateUser")
//    public ResponseEntity<?> updateUser(){
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto userRequestDto) throws UserAlreadyPresent {
        System.out.println("initate the update process***************************");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userServices.updateUser(email);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
//        return ResponseEntity.ok("SUCECC ********************************************");
        return ResponseEntity.ok(userResponseDto);
    }
}
