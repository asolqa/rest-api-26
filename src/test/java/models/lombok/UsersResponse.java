package models.lombok;

import lombok.Data;

@Data
public class UsersResponse {

    private UserData data;

    private SupportInformation support;
}
