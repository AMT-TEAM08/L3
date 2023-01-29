package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersEndPoint {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> listUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable int id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity user) {
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable int id, @RequestBody UserEntity user) {
        UserEntity userToUpdate = userRepository.findById(id);
        if (userToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}