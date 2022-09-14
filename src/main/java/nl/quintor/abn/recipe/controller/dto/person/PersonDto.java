package nl.quintor.abn.recipe.controller.dto.person;

import lombok.Data;
import nl.quintor.abn.recipe.controller.dto.recipe.RecipeDto;

import java.util.List;

@Data
public class PersonDto {

    private Long id;

    private String username;

    private List<RecipeDto> createdRecipes;

}
