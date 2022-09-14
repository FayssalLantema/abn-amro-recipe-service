package nl.quintor.abn.recipe.exception;

public class IngredientDoesNotExistException extends RuntimeException {

    public IngredientDoesNotExistException(String name) {
        super("Ingredient with name " + name + " does not exist");
    }
}
