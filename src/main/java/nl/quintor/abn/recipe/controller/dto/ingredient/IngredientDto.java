package nl.quintor.abn.recipe.controller.dto.ingredient;


import lombok.Data;

@Data
public class IngredientDto {

    private long id;
    private String name;
    private boolean vegetarian;

}
