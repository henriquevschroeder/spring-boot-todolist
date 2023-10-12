package br.com.henriquevschroeder.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.henriquevschroeder.todolist.models.UserModel;
import br.com.henriquevschroeder.todolist.repositories.IUserRepository;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserModel userModel)
    {
        UserModel user = this.userRepository.findByUsername(userModel.getUsername());
    
        if (user != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(hashedPassword);
    
        UserModel userCreated = this.userRepository.save(userModel);

        String locationHeader = "/users/" + userCreated.getId();
        
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", locationHeader).build();
    }
    
}
