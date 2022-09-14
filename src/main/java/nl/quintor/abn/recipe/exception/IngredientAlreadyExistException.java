package nl.quintor.abn.recipe.exception;

public class IngredientAlreadyExistException extends RuntimeException {

    public IngredientAlreadyExistException(String name) {
        super("Ingredient with name " + name + " already exist");
    }


}