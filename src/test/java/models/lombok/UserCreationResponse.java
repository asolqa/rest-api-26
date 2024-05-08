package models.lombok;

import lombok.Data;


@Data
public class UserCreationResponse {

    private String name;

    private String job;

    private String id;

    private String createdAt;
}
