package org.example.demomogodb.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@Data
public class UserPage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<User> content;
    private int totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

}
