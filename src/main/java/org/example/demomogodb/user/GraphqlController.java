package org.example.demomogodb.user;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
public class GraphqlController {
    private final UserService service;

    public GraphqlController(UserService service) {
        this.service = service;
    }

    @QueryMapping
    public UserPage users(@Argument int page, @Argument int size) {
        int pageNumber = Math.max(page, 0);
        int pageSize = (size <= 0) ? 10 : size;
        return service.listUser(pageNumber, pageSize);
    }

    @MutationMapping
    public User createUser(@Argument("input") UserReq req) {
        return service.saveUser(req);
    }

    @MutationMapping
    public User updateUser(@Argument("input") UserReq req) {
        return service.update(req);
    }

    @MutationMapping
    public void deleteUser(@Argument("idUser") String idUser) {
        service.delete(idUser);
    }
}
