package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.IngredientAlreadyExistException;
import nl.quintor.abn.recipe.exception.IngredientDoesNotExistException;
import nl.quintor.abn.recipe.model.Ingredient;
import nl.quintor.abn.recipe.repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Method for finding an ingredient by name
     *
     * @param ingredientName the name of the ingredient
     * @return the found ingredient
     */
    public Ingredient findByName(String ingredientName) {
        if (doesIngredientExist(ingredientName)) {
            LOG.info("Finding ingredient " + ingredientName);
            return ingredientRepository.findByName(ingredientName).get();
        } else {
            LOG.error("Ingredient " + ingredientName + " does not exist");
            throw new IngredientDoesNotExistException(ingredientName);
        }
    }

    /**
     * Method for creating a new ingredient
     *
     * @param name       the name for the new ingredient
     * @param vegetarian specifies if the ingredient is vegetarian
     * @return the created ingredient
     */
    public Ingredient createIngredient(String name, boolean vegetarian) {
        if (!doesIngredientExist(name)) {
            LOG.info("Creating an ingredient with the name " + name);
            return ingredientRepository.save(new Ingredient(name, vegetarian));
        } else {
            LOG.error("The ingredient " + name + " already exists");
            throw new IngredientAlreadyExistException(name);
        }
    }

    /**
     * Private method to check if the ingredient exist
     *
     * @param name the name of the ingredient that needs to be checked
     * @return a boolean value if the ingredient exist
     */
    private boolean doesIngredientExist(String name) {
        LOG.info("Checking if the ingredient " + name + "exist");
        return ingredientRepository.findByName(name).isPresent();
    }


}
