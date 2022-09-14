package nl.quintor.abn.recipe.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(long recipeId) {
        super("Recipe with id " + recipeId + " is not found");
    }

}
