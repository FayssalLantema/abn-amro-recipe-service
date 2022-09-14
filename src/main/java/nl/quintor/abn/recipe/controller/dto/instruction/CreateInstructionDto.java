package nl.quintor.abn.recipe.controller.dto.instruction;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateInstructionDto {

    @NotEmpty
    @Size(min = 8, message = "The way of preperation should be at least 8 characters")
    private String wayOfPreperation;

    @NotEmpty
    @Size(min = 2, message = "Ingredient Name should be at least 2 characters")
    private String ingredientName;

}
