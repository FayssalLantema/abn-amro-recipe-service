package nl.quintor.abn.recipe.controller.dto.ingredient;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateIngredientDto {


    @NotEmpty
    @Size(min = 2, message = "Name should be at least 2 characters")
    private String name;

    @NotNull
    private boolean vegetarian;
}
