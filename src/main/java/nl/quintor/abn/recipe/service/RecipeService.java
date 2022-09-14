package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.RecipeNotFoundException;
import nl.quintor.abn.recipe.exception.UnauthorizedException;
import nl.quintor.abn.recipe.model.Instruction;
import nl.quintor.abn.recipe.model.Person;
import nl.quintor.abn.recipe.model.Recipe;
import nl.quintor.abn.recipe.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;
    private final InstructionService instructionService;

    private final PersonService personService;

    public RecipeService(RecipeRepository recipeRepository, @Lazy InstructionService instructionService, @Lazy PersonService personService) {
        this.recipeRepository = recipeRepository;
        this.instructionService = instructionService;
        this.personService = personService;
    }

    /**
     * Method to find a recipe by the recipe ID
     *
     * @param recipeId the ID of the wanted recipe
     * @return the found recipe belonging to the recipe ID
     */
    public Recipe findById(long recipeId) {
        LOG.info("Checking if recipe with id " + recipeId + " does exist");
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            LOG.info("Recipe with id " + recipeId + " does not exist");
            throw new RecipeNotFoundException(recipeId);
        }
    }

    /**
     * Method to find all the recipes belonging to the person, based on the person wishes
     *
     * @param personId          the ID of the requesting person
     * @param vegetarian        boolean if the dish needs to be vegetarian
     * @param servings          the amount of servings the dish needs to have
     * @param includeIngredient include specific ingredients
     * @param excludeIngredient exclude specific ingredients
     * @param instruction       filter for specific text in instructions
     * @return list of found recipes matching the wishes for the correlated person
     */
    public List<Recipe> search(long personId, Optional<Boolean> vegetarian, Optional<Integer> servings, Optional<List<String>> includeIngredient, Optional<List<String>> excludeIngredient, Optional<String> instruction) {
        //Check if person with the ID exists
        personService.getById(personId);

        return recipeRepository.searchAll(
                personId,
                vegetarian.orElse(null),
                servings.orElse(null),
                includeIngredient.orElse(null),
                excludeIngredient.orElse(null),
                instruction.orElse(null)
        );
    }

    /**
     * Method to create a new recipe
     *
     * @param name             the name of the wanted recipe
     * @param numberOfServings the amount of servings out of the recipe
     * @param personId         the person that has created this recipe
     * @return the object of the created recipe
     */
    public Recipe createRecipe(String name, int numberOfServings, long personId) {

        Person person = personService.getById(personId);

        LOG.info("Created new Recipe with name " + name);
        return recipeRepository.save(new Recipe(name, numberOfServings, person));
    }

    /**
     * Method to modify a recipe
     *
     * @param recipeId the recipe that needs to be modified
     * @param personId the person that called the modify method
     * @param map      a map of the params that needs to be modified
     * @return the updated recipe object
     */
    public Recipe modifyRecipe(long recipeId, long personId, HashMap<Object, Object> map) {
        checkIfOwnerOfRecipe(personId, recipeId);

        Recipe recipe = findById(recipeId);

        if (map.containsKey("numberOfServings")) {
            recipe.setNumberOfServings((int) map.get("numberOfServings"));
        }
        if (map.containsKey("name")) {
            recipe.setName((String) map.get("name"));
        }

        LOG.info("Modifying recipe with ID " + recipeId);
        return recipeRepository.save(recipe);
    }

    /**
     * Method for deleting a recipe
     *
     * @param personId the person that is calling the delete method
     * @param recipeId the recipe that has to be deleted
     */
    public void delete(long personId, long recipeId) {
        checkIfOwnerOfRecipe(personId, recipeId);

        LOG.info("Deleting recipe with id " + recipeId);
        recipeRepository.delete(findById(recipeId));
    }

    /**
     * Method to create a new instruction
     *
     * @param wayOfPreperation the way the ingredient needs to be prepared
     * @param ingredientName   the name of the correlated ingredient
     * @param recipeId         the ID of the recipe where the instruction belongs to
     * @param personId         the person who called the create instruction method
     * @return the created instruction object
     */
    public Instruction createInstruction(String wayOfPreperation, String ingredientName, long recipeId, long personId) {
        checkIfOwnerOfRecipe(personId, recipeId);

        LOG.info("Creating instruction for recipe with ID " + recipeId);
        return instructionService.create(wayOfPreperation, ingredientName, recipeId);
    }

    /**
     * Method to delete an instruction
     *
     * @param recipeId      the ID of the recipe related to the instruction
     * @param instructionId the ID of the instruction that has to be deleted
     * @param personId      the ID of the person that called the method
     */
    public void deleteInstruction(long recipeId, long instructionId, long personId) {
        checkIfOwnerOfRecipe(personId, recipeId);
        Recipe recipe = findById(recipeId);

        LOG.info("Deleting recipe with ID " + recipeId);

        instructionService.delete(instructionId, recipe);
    }

    /**
     * Private method that checks if the given person is the owner of the recipe
     *
     * @param personId the ID of the given person which called the method
     * @param recipeId the ID of the correlated recipe
     */
    private void checkIfOwnerOfRecipe(long personId, long recipeId) {
        LOG.info("Checking if " + personId + " is the owner of recipe with id " + recipeId);
        if (!findById(recipeId).getCreatedBy().getId().equals(personId)) {

            LOG.error(personId + " is not the owner of " + recipeId);
            throw new UnauthorizedException("Not the owner of the recipe");
        }
    }

}
