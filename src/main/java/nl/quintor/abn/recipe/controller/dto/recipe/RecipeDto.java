package nl.quintor.abn.recipe.controller.dto.recipe;

import lombok.Data;
import nl.quintor.abn.recipe.controller.dto.instruction.InstructionDto;

import java.util.List;

@Data
public class RecipeDto {

    private long id;
    private String name;
    private int numberOfServings;
    private List<InstructionDto> instructionList;
}
