package nl.quintor.abn.recipe.controller.dto.recipe;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class PatchRecipeDto {

    @Size(min = 2, message = "Name should be at least 2 characters")
    private String name;

    @Min(value = 1, message = "There should be at least 1 serving")
    private Integer numberOfServings;
}
