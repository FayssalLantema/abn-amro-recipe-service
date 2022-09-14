package nl.quintor.abn.recipe.controller.dto.instruction;

import lombok.Data;
import nl.quintor.abn.recipe.controller.dto.ingredient.IngredientDto;

@Data
public class InstructionDto {

    private Long id;

    private String wayOfPreperation;

    private IngredientDto ingredient;

}
