package nl.quintor.abn.recipe.controller.dto.recipe;

import nl.quintor.abn.recipe.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeDto toRecipeDto(Recipe recipe);

    List<RecipeDto> toListRecipeDto(List<Recipe> recipes);

}
