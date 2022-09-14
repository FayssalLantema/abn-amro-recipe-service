package nl.quintor.abn.recipe.service;


import nl.quintor.abn.recipe.exception.DoesNotBelongException;
import nl.quintor.abn.recipe.exception.InstructionNotFoundException;
import nl.quintor.abn.recipe.model.Ingredient;
import nl.quintor.abn.recipe.model.Instruction;
import nl.quintor.abn.recipe.model.Recipe;
import nl.quintor.abn.recipe.repository.InstructionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstructionService {
    private static final Logger LOG = LoggerFactory.getLogger(InstructionService.class);

    private final InstructionRepository instructionRepository;

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public InstructionService(InstructionRepository instructionRepository, RecipeService recipeService, IngredientService ingredientService) {
        this.instructionRepository = instructionRepository;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    /**
     * Method for finding an instruction by ID
     *
     * @param instructionId the ID of the instruction
     * @return the found instruction by the specified ID
     */
    public Instruction findById(long instructionId) {
        LOG.info("Finding instruction with id " + instructionId);
        Optional<Instruction> instruction = instructionRepository.findById(instructionId);

        if (instruction.isPresent())
            return instruction.get();
        else {
            LOG.error("The instruction with id " + instruction + " has not been found");
            throw new InstructionNotFoundException(instructionId);
        }
    }

    /**
     * Method for creating a new instruction
     *
     * @param wayOfPreperation the way an ingredient needs to be preperated
     * @param ingredientName   the name of the correlated ingredient
     * @param recipeId         the recipe where the instruction belongs to
     * @return the created instruction
     */
    public Instruction create(String wayOfPreperation, String ingredientName, long recipeId) {
        //Check if the ingredient and recipe exist
        Ingredient ingredient = ingredientService.findByName(ingredientName);

        Recipe recipe = recipeService.findById(recipeId);

        LOG.info("Creating instruction based on " + ingredientName);

        return instructionRepository.save(
                new Instruction(
                        wayOfPreperation,
                        ingredient,
                        recipe
                ));
    }

    /**
     * Method for deleting an instruction
     *
     * @param instructionId the ID of the instruction
     * @param recipe        the recipe where the instruction belongs to
     */
    public void delete(long instructionId, Recipe recipe) {
        // Checking if instruction does exist
        Instruction instruction = findById(instructionId);

        // If instruction exist, check if it belongs to the recipe
        if (instruction.getRecipe().equals(recipe)) {
            LOG.info("Deleting the instruction");
            instructionRepository.delete(instruction);
        } else {
            LOG.error("The instruction does not belong to the recipe");
            throw new DoesNotBelongException("The instruction does not belong to the recipe");
        }

    }
}
