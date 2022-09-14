package nl.quintor.abn.recipe.controller.dto.ingredient;

import nl.quintor.abn.recipe.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientMapper {

    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    IngredientDto toIngredientDto(Ingredient ingredient);

}
