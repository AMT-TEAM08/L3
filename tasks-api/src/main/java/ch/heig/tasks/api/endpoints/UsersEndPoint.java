package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.UsersApi;
import ch.heig.tasks.api.exceptions.UserIdNotMatchingException;
import ch.heig.tasks.api.exceptions.UserNotFoundException;
import ch.heig.tasks.api.model.TaskResponse;
import ch.heig.tasks.api.model.User;
import ch.heig.tasks.mappers.TaskMapper;
import ch.heig.tasks.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UsersEndPoint implements UsersApi {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Void> addUser(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        UserEntity userEntity = new UserEntity(user);
        UserEntity userAdded = userRepository.save(userEntity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAdded.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<User> getUser(Integer userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get().toUser(), HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(userEntity.toUser());
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> usersUserIdPatch(Integer userId, User user) {
        if (!userId.equals(user.getId())) {
            throw new UserIdNotMatchingException(userId, user.getId());
        }
        Optional<UserEntity> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isPresent()) {
            return updateUser(userId, user, userToUpdate.get());
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public ResponseEntity<Void> usersUserIdPut(Integer userId, User user) {
        if (!userId.equals(user.getId())) {
            throw new UserIdNotMatchingException(userId, user.getId());
        }
        Optional<UserEntity> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isPresent()) {
            return updateUser(userId, user, userToUpdate.get());
        } else {
            return addUser(user);
        }
    }

    /**
     * GET /users/{user_id}/tasks : Retrieve a list of all tasks assigned to a user
     *
     * @param userId The ID of the user (required)
     * @return List of tasks retrieved successfully (status code 200)
     * or User not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<List<TaskResponse>> getUserTasks(Integer userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<TaskResponse> tasks = new ArrayList<>();
            for (TaskEntity taskEntity : user.get().getTasks()) {
                tasks.add(TaskMapper.mapTaskEntityToTaskResponse(taskEntity));
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }

    }

    private ResponseEntity<Void> updateUser(Integer userId, User user, UserEntity userToUpdate) {
        userToUpdate.setId(userId);
        userToUpdate.setName(user.getName());
        userRepository.save(userToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}