package ch.heig.tasks.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIdNotMatchingException extends RuntimeException {

    public UserIdNotMatchingException(Integer id, Integer id2) {
        super("User " + id + " and user " + id2 + " should match but are not");
    }
}
