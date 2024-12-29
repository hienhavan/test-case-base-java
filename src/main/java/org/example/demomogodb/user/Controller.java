package org.example.demomogodb.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.demomogodb.exception.UnauthorizedException;
import org.example.demomogodb.exception.UserNotFoundException;
import org.example.demomogodb.utils.JsonSchemaValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {
    String checkAuth = "123456a";

    private final UserService service;
    private final JsonSchemaValidator jsonSchemaValidator;
    ObjectMapper objectMapper = new ObjectMapper();

    public Controller(UserService service, JsonSchemaValidator jsonSchemaValidator) {
        this.service = service;
        this.jsonSchemaValidator = jsonSchemaValidator;
    }

    @PostMapping("/api/v1/users/add")
    @SneakyThrows
    public User addUser(@RequestBody String userJson) {
        if (!jsonSchemaValidator.validateJson(userJson)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format or schema");
        }
        UserReq userReq = objectMapper.readValue(userJson, UserReq.class);
        return service.saveUser(userReq);
    }

    @PutMapping("/api/v1/users/update")
    public User update(@RequestBody UserReq req) {
        if (req.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing or invalid user ID");
        }
        return service.update(req);
    }

    @DeleteMapping("/api/v1/delete/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("idUser") String idUser) {
        service.delete(idUser);
    }

    @GetMapping("/api/v1/list-users")
    public Page<User> listUsers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return service.listUsers(page, size);
    }

    @GetMapping("api/v1/get-user/{idUser}")
    public User getUser(@PathVariable("idUser") String idUser, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.equals(checkAuth)) {
            throw new UnauthorizedException("Invalid or missing authorization");
        }
        try {
            return service.getUser(idUser);
        } catch (RuntimeException ex) {
            throw new UserNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new UserNotFoundException("An error occurred");
        }
    }

}
