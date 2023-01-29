package ch.heig.tasks.mappers;

import ch.heig.tasks.Entities.UserEntity;
import ch.heig.tasks.api.model.UserRequest;
import ch.heig.tasks.api.model.UserResponse;

public class UserMapper {

    public static UserEntity mapUserRequestToUserEntity(UserRequest userRequest) {
        UserEntity user = new UserEntity();
        user.setName(userRequest.getName());
        return user;
    }

    public static UserResponse mapUserEntityToUserResponse(UserEntity userEntity) {
        UserResponse user = new UserResponse();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        return user;
    }

    public static UserEntity mapUserResponseToUserEntity(UserResponse userResponse) {
        UserEntity user = new UserEntity();
        user.setId(userResponse.getId());
        user.setName(userResponse.getName());
        return user;
    }
}