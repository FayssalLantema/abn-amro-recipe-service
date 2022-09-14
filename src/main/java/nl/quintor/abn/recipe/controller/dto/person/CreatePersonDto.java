package nl.quintor.abn.recipe.controller.dto.person;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreatePersonDto {

    @NotEmpty
    @Size(min = 3, message = "Username should be at least 3 characters")
    private String username;

    @NotEmpty
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

}
