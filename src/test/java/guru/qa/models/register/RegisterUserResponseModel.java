package guru.qa.models.register;

import lombok.Data;

@Data
public class RegisterUserResponseModel {
    String token;
    int id;
    String error;
}