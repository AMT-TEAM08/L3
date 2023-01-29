package ch.heig.tasks.api.endpoints;

import ch.heig.tasks.Entities.TaskEntity;
import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.UsersApi;
import ch.heig.tasks.api.exceptions.UserNotFoundException;
import ch.heig.tasks.api.model.UserRequest;
import ch.heig.tasks.api.model.UserResponse;
import ch.heig.tasks.mappers.UserMapper;
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

    /**
     * POST /users : Add a new user
     *
     * @param userRequest (required)
     * @return User created successfully (status code 201)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<Void> addUser(UserRequest userRequest) {
        UserEntity userEntity = UserMapper.mapUserRequestToUserEntity(userRequest);
        UserEntity userAdded = userRepository.save(userEntity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAdded.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    /**
     * GET /users/{user_id} : Retrieve a user by ID
     *
     * @param userId The ID of the user (required)
     * @return User retrieved successfully (status code 200)
     * or Bad request (status code 400)
     * or User not found (status code 404)
     */
    @Override
    public ResponseEntity<UserResponse> getUser(Integer userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(UserMapper.mapUserEntityToUserResponse(user.get()), HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * GET /users : Retrieve a list of all users
     *
     * @return List of users retrieved successfully (status code 200)
     */
    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserResponse> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            users.add(UserMapper.mapUserEntityToUserResponse(userEntity));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * PATCH /users/{user_id} : Update a user
     *
     * @param userId      The ID of the user (required)
     * @param userRequest (required)
     * @return User updated successfully (status code 200)
     * or User not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<UserResponse> usersUserIdPatch(Integer userId, UserRequest userRequest) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setId(userId);
            userEntity.setName(userRequest.getName());
            userRepository.save(userEntity);
            return new ResponseEntity<>(UserMapper.mapUserEntityToUserResponse(userEntity), HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * PUT /users/{user_id} : Replace a user
     *
     * @param userId      The ID of the user (required)
     * @param userRequest (required)
     * @return User replaced successfully (status code 200)
     * or User not found (status code 404)
     * or Bad request (status code 400)
     */
    @Override
    public ResponseEntity<Void> usersUserIdPut(Integer userId, UserRequest userRequest) {
        Optional<UserEntity> userToUpdate = userRepository.findById(userId);
        if (userToUpdate.isPresent()) {
            return new ResponseEntity<>(usersUserIdPatch(userId, userRequest).getStatusCode());
        } else {
            return addUser(userRequest);
        }
    }
}